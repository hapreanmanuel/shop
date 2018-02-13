package ro.msg.learning.shop.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import ro.msg.learning.shop.utility.converters.CsvHttpMessageConverter;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurationSupport{


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(createCsvMessageConverter());

        super.addDefaultHttpMessageConverters(converters);
    }

    @Bean
    public CsvHttpMessageConverter createCsvMessageConverter(){
        return new CsvHttpMessageConverter();
    }

    /*
    		if (ClassUtils.isPresent("com.jayway.jsonpath.DocumentContext", context.getClassLoader())
				&& ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", context.getClassLoader())) {

			ObjectMapper mapper = getUniqueBean(ObjectMapper.class, context);
			mapper = mapper == null ? new ObjectMapper() : mapper;

			ProjectingJackson2HttpMessageConverter converter = new ProjectingJackson2HttpMessageConverter(mapper);
			converter.setBeanClassLoader(context.getClassLoader());
			converter.setBeanFactory(context);

			converters.add(0, converter);
		}

		if (ClassUtils.isPresent("org.xmlbeam.XBProjector", context.getClassLoader())) {
			converters.add(0, new XmlBeamHttpMessageConverter());
		}
     */
}
