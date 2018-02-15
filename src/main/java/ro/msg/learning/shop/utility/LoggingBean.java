package ro.msg.learning.shop.utility;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LoggingBean implements InitializingBean{

    private final List<String> availableStrategies = Arrays.asList("SINGLE", "ABUNDANT", "GREEDY");

    private Environment environment;

    @Autowired
    public LoggingBean(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet(){
        log.info("=================================================");
        try{
            logEnvVariables();
            logActiveSpringProfiles();
            logRelevantProperties();
        }catch(Exception e){
            log.error("Logging Bean failed: ",e);
        }
        log.info("=================================================");
    }

    private void logEnvVariables() {
        String strategyName = getValueOfProperty(environment, "shop.strategy" , "SINGLE", availableStrategies);
        log.info("{} = {}","shop.strategy", strategyName);
    }

    private void logActiveSpringProfiles() {
        final String key = "spring.profiles.active";
        final String value = getValueOfProperty(environment,key,"local", null);
        log.info("{} = {}", key, value);
    }

    private void logRelevantProperties(){
        final String key = "spring.datasource.url";
        final String value = getValueOfProperty(environment,key ,null , null);
        log.info("{} = {}", key,value);
    }

    private String getValueOfProperty(final Environment environment,
                                      final String propertyKey,
                                      final String propertyDefaultValue,
                                      final List<String> acceptablePropertyValues){
        String propValue = environment.getProperty(propertyKey);
        if(propValue == null && propertyDefaultValue != null){
            propValue = propertyDefaultValue;
            log.warn("The {} doesn't have an explicit value; default value is = {}", propertyKey, propertyDefaultValue);
        }

        if(acceptablePropertyValues != null && !acceptablePropertyValues.contains(propValue)){
           log.warn("The property = {} has an invalid value = {}",propertyKey, propValue);
        }

        if(propValue == null){
            log.warn("The property = {} is null", propertyKey);
        }
        return propValue;
    }
}
