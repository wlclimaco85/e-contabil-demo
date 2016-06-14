package br.com.emmanuelneri.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({"br.com.emmanuelneri.app.service"})
public class AppConfig {


}
