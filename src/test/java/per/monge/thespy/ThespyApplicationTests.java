package per.monge.thespy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import per.jm.demo.SpyApplication;
import per.jm.demo.util.MenuUtil;

@SpringBootTest(classes = SpyApplication.class)
class ThespyApplicationTests {

    @Test
    void contextLoads() {
        MenuUtil.initMenu();
    }

}
