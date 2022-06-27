import com.hyb.serviceorder.OrderApplication;
import com.hyb.serviceorder.entity.TOrder;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = OrderApplication.class)
public class RabbitmqTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void  t1(){
        TOrder tOrder = new TOrder();
        tOrder.setMobile("18277486571");
        tOrder.setGmtCreate(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",tOrder);
    }

    @Test
    public void setRedissonClient(){
        System.out.println(redissonClient);
    }


}
