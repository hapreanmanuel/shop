package ro.msg.learning.shop.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LoggingBean implements InitializingBean{

    private final List<String> availableStrategies = Arrays.asList("SINGLE", "ABUNDANT", "GREEDY");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("=================================================");
        try{
            logEnvVariables();
            logActiveSpringProfiles();
            logRelevantProperties();
        }catch(Exception e){
            logger.error("Logging Bean failed: ",e);
        }
        logger.info("=================================================");
    }

    private void logEnvVariables() {
        String strategyName = getValueOfProperty(environment, "shop.strategy" , "SINGLE", availableStrategies);
        logger.info("{} = {}","shop.strategy", strategyName);
    }

    private void logActiveSpringProfiles() {
        final String key = "spring.profiles.active";
        final String value = getValueOfProperty(environment,key,"local", null);
        logger.info("{} = {}", key, value);
    }

    private void logRelevantProperties(){
        final String key = "spring.datasource.url";
        final String value = getValueOfProperty(environment,key ,null , null);
        logger.info("{} = {}", key,value);
    }

    private String getValueOfProperty(final Environment environment,
                                      final String propertyKey,
                                      final String propertyDefaultValue,
                                      final List<String> acceptablePropertyValues){
        String propValue = environment.getProperty(propertyKey);
        if(propValue == null && propertyDefaultValue != null){
            propValue = propertyDefaultValue;
            logger.warn("The {} doesn't have an explicit value; default value is = {}", propertyKey, propertyDefaultValue);
        }

        if(acceptablePropertyValues != null){
            if(!acceptablePropertyValues.contains(propValue)){
                logger.warn("The property = {} has an invalid value = {}",propertyKey, propValue);
            }
        }

        if(propValue == null){
            logger.warn("The property = {} is null", propertyKey);
        }
        return propValue;
    }
}
