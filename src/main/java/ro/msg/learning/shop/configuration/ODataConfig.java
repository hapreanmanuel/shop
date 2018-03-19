package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ro.msg.learning.shop.exception.InvalidRequestException;
import ro.msg.learning.shop.odata.ShopODataServiceFactory;
import ro.msg.learning.shop.odata.ShopODataServlet;

import javax.persistence.EntityManagerFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Configuration
public class ODataConfig{

    private static final String ROOT = "/odata/";

    private static final String SERVICE_URL = ROOT +"*";

    private static final List<String> ENDPOINTS = Arrays.asList("$metadata","Orders", "OrderDetails", "Products");

    @Bean
    public ServletRegistrationBean odataServlet(@Value("${shop.persistence.unit.name}") String namespace,
                                                EntityManagerFactory entityManagerFactory){
        return new ServletRegistrationBean(
                new ShopODataServlet(new ShopODataServiceFactory(entityManagerFactory, namespace)), SERVICE_URL);
    }

    static class ODataServiceSecurityFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            String url = ((HttpServletRequest)request).getRequestURL().toString();
            if(url.contains(ROOT)){
                if(ENDPOINTS.parallelStream().anyMatch(url::contains)) {
                    chain.doFilter(request, response);
                }else {
                    throw new InvalidRequestException();
                }
            }else{
                chain.doFilter(request, response);
            }
        }
        @Override
        public void destroy() {
        }
    }

    @Bean
    public ODataServiceSecurityFilter oDataServiceSecurityFilter(){
        return new ODataServiceSecurityFilter();
    }
}
