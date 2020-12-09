import com.offcn.user.ScwUserStart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: lhq
 * @Date: 2020/11/30 10:48
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScwUserStart.class})
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired   //对比redisTemplate  类型单一，再操作时少了序列化；注意：数据不能与redisTemplate互通
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void saveValue(){
        stringRedisTemplate.opsForValue().set("e03646ccbc9e41f891e8c2cf7213979a","8");
    }
}
