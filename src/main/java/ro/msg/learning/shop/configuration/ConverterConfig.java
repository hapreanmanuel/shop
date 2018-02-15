package ro.msg.learning.shop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.utility.converter.CsvHttpMessageConverter;

@Configuration
public class ConverterConfig {

    @Bean
    public CsvHttpMessageConverter csvHttpMessageConverter(){
        return new CsvHttpMessageConverter();
    }
}
