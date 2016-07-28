import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by FTY on 2016/7/13.
 */
public class SpringTest {

    ApplicationContext applicationContext;

    @Before
    public void init(){
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-*.xml");
    }

    @Test
    public void testUser(){
        IUserService userService = applicationContext.getBean(UserServiceImpl.class);
        User user = new User();
        user.setUsername("fty");
        user.setPassword("111111");
        userService.save(user);
    }
}
