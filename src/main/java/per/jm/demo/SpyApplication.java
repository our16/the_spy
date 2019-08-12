package per.jm.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("per.jm.demo.mapper")
public class SpyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpyApplication.class, args);
    }

}
