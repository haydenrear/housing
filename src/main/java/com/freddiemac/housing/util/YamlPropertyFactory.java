package com.freddiemac.housing.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;


public class YamlPropertyFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(
            String s,
            EncodedResource encodedResource
    )
    {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties properties = factory.getObject();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
        return propertiesPropertySource;
    }


}
