package com.freddiemac.housing;

import com.freddiemac.housing.config.props.DataApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = DataApiProperties.class)
public class HousingApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(HousingApplication.class, args);
    }

}
