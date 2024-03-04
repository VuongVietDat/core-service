package vn.com.atomi.loyalty.core;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import vn.com.atomi.loyalty.base.config.PersistenceConfig;

@ComponentScan(basePackages = "vn.com.atomi.loyalty")
@SpringBootApplication
@EnableFeignClients
@ConfigurationPropertiesScan
@EnableJpaRepositories(basePackages = "vn.com.atomi.loyalty.core.repository")
@EnableSpringDataWebSupport
@Import({PersistenceConfig.class})
public class LoyaltyCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoyaltyCoreApplication.class, args);
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC+7"));
  }
}
