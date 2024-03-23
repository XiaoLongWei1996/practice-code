package xlw.test.satoken;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("xlw.test.satoken.dao")
@SpringBootApplication
public class SatokenDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SatokenDemoApplication.class, args);
    }

}
