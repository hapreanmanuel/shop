package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ro.msg.learning.shop.odata.ShopODataServiceFactory;
import ro.msg.learning.shop.odata.ShopODataServlet;

import javax.persistence.EntityManagerFactory;


@Configuration
public class ODataConfig{

    private static final String SERVICE_URL = "/odata/";

    @Bean
    public ServletRegistrationBean odataServlet(@Value("${shop.persistence.unit.name}") String namespace,
                                                @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory){
        return new ServletRegistrationBean(
                new ShopODataServlet(new ShopODataServiceFactory(entityManagerFactory, namespace)), SERVICE_URL +"*");
    }
}
