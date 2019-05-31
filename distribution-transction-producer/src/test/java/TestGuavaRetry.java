import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author wsl
 * @date 2019/3/20
 */
public class TestGuavaRetry {

    private static Callable<Boolean> updateCall = new Callable<Boolean>() {
        @Override
        public Boolean call() throws Exception {
            System.out.println("重试");
            //throw new RuntimeException("抛出了异常");
            return false;
        }
    };

    static Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
            // 抛出runtime异常、checked异常都会重试
            .retryIfException()

            // 返回false也要重试
            .retryIfResult(Predicates.equalTo(false))

            //重调策略
            .withWaitStrategy(WaitStrategies.fixedWait(4, TimeUnit.SECONDS))

            // 尝试次数
            .withStopStrategy(StopStrategies.stopAfterAttempt(3)).
                    withRetryListener(new TestMyRetryListener()).
                    build();

    public static void main(String[] args) {
        try{
            retryer.call(updateCall);
        } catch(Exception e){
            System.out.println("出现了异常");
        }
    }




}
