package com.momo.springbootenvironment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.RandomValuePropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.web.context.support.ServletContextPropertySource;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(Application.class, args);

        StringBuilder stringBuilder = new StringBuilder();

        MutablePropertySources propertySources = ac.getEnvironment().getPropertySources();
        propertySources.forEach(propertySource -> {
            printPropertySource(propertySource, stringBuilder.toString());
            stringBuilder.append("\t");
        });

        ConfigurableEnvironment environment = ac.getEnvironment();
        System.out.println("server.port:" + environment.getProperty("server.port"));
        System.out.println("yaml.server.port:" + environment.getProperty("yaml.server.port"));
    }

    private static void printPropertySource(PropertySource propertySource, String tab) {
        if (propertySource instanceof MapPropertySource) {
			MapPropertySource mapPropertySource = (MapPropertySource) propertySource;

            printPropertySource(mapPropertySource, mapPropertySource.getPropertyNames(), tab);
        } else if (propertySource instanceof ServletContextPropertySource) {
            ServletContextPropertySource servletContextPropertySource = (ServletContextPropertySource) propertySource;
			printPropertySource(servletContextPropertySource, servletContextPropertySource.getPropertyNames(), tab);
        } else if (propertySource instanceof SystemEnvironmentPropertySource) {
            SystemEnvironmentPropertySource environmentPropertySource = (SystemEnvironmentPropertySource) propertySource;
            printPropertySource(environmentPropertySource, environmentPropertySource.getPropertyNames(), tab);
        } else if (propertySource instanceof RandomValuePropertySource) {
            RandomValuePropertySource randomValuePropertySource = (RandomValuePropertySource) propertySource;
            printPropertySource(randomValuePropertySource, new String[]{"random.int", "random.long", "random.uuid"}, tab);
        } else if (propertySource instanceof PropertySource.StubPropertySource) {
            PropertySource.StubPropertySource stubPropertySource = (PropertySource.StubPropertySource) propertySource;
        }
    }

    private static void printPropertySource(PropertySource propertySource, String[] names, String tab) {
        Arrays.stream(names).forEach(name -> {
            System.out.println(tab + propertySource.getName() + "\t" + name + "：" + propertySource.getProperty(name));
        });
    }
}
