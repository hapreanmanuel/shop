package ro.msg.learning.shop.odata.order;

import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.service.ShopService;

@Configuration
public class OConfig{
    @Bean
    public ServletRegistrationBean odataOrderServlet(OServlet servlet){
        return new ServletRegistrationBean(servlet, "/ood/*");
    }

    @Bean
    public OFactory oFactory(ShopService shopService){
        return new OFactory(shopService);
    }

    @Bean
    public OServlet oServlet(@Qualifier("oFactory") ODataServiceFactory oDataServiceFactory){
        return new OServlet(oDataServiceFactory);
    }

}