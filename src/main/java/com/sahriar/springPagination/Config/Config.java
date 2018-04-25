package com.sahriar.springPagination.Config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by toufiq on 4/20/18.
 */
@Configuration
public class Config {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
