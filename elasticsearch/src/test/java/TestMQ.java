import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wsl
 * @date 2019/5/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMQ {

    @Resource
    private RabbitTemplate rabbitTemplate;


}
