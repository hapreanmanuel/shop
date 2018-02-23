package ro.msg.learning.shop.configuration;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;
import ro.msg.learning.shop.utility.strategy.ClosestLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.SingleLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.StrategySelectionAlgorithm;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class StrategyConfig {

    public enum Strategy {
        SINGLE,
        CLOSEST
    }

    @Bean
    public StrategySelectionAlgorithm getAlgorithm(@Value("${shop.strategy}") Strategy strategy){

        switch(strategy){
            case SINGLE:
                return new SingleLocationAlgorithm();
            case CLOSEST:
                return new ClosestLocationAlgorithm();
            default: return new SingleLocationAlgorithm();
        }
    }

    @Bean
    public DistanceCalculator distanceCalculator(@Value("${shop.strategy.closest.key}") String key,
                                                 @Value("${shop.proxy.domain}") String domain,
                                                 @Value("${shop.proxy.port}") int port){
        GeoApiContext geoApiContext = new GeoApiContext();
        geoApiContext.setApiKey(key);
        geoApiContext.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(domain, port)));

        return new DistanceCalculator(geoApiContext);
    }

}

