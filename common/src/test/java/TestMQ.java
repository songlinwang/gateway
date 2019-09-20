import com.wsl.CommonApplication;
import com.wsl.mq.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/5/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApplication.class)
public class TestMQ {

    @Autowired
    private HelloSender helloSender;

    @Test
    public void testSend() {
        helloSender.send();
    }
}
