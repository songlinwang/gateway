import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;

import  java.lang.Boolean;

/**
 * @author wsl
 * @date 2019/3/20
 */
public class TestMyRetryListener implements RetryListener {

    @Override
    public <Boolean> void onRetry(Attempt<Boolean> attempt) {

        System.out.println(attempt.getAttemptNumber());

        System.out.println(attempt.hasException());

        System.out.println(attempt.hasResult());

        if(attempt.hasException()){
            System.out.println(attempt.getExceptionCause().toString());
        }else {
            System.out.println(attempt.getResult());
        }

        try {
            Boolean result = attempt.get();
            System.out.print(",rude get=" + result);
        }catch (Exception e){
            System.out.println(e.getCause().toString());
        }


    }
}
