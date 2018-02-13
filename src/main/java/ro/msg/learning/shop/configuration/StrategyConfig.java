package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ro.msg.learning.shop.exceptions.NoSuitableStrategyException;
import ro.msg.learning.shop.utility.strategy.GreedyAlgorithm;
import ro.msg.learning.shop.utility.strategy.MostAbundantAlgorithm;
import ro.msg.learning.shop.utility.strategy.SingleLocationAlgorithm;
import ro.msg.learning.shop.utility.strategy.StrategySelectionAlgorithm;

@Configuration
@PropertySource({"classpath:external/strategy.properties"})
public class StrategyConfig {

    public enum Strategy {
        SINGLE,
        ABUNDANT,
        GREEDY,
    }

    @Bean
    public StrategySelectionAlgorithm getAlgorithm(@Value("${shop.strategy}") Strategy strategy){

        switch(strategy){
            case SINGLE:
                return initAlgorithm(SingleLocationAlgorithm.class);
            case ABUNDANT:
                return initAlgorithm(MostAbundantAlgorithm.class);
            case GREEDY:
                return initAlgorithm(GreedyAlgorithm.class);
            default: return initAlgorithm(SingleLocationAlgorithm.class);
        }
    }

    private StrategySelectionAlgorithm initAlgorithm(Class<? extends StrategySelectionAlgorithm> algorithmClass){
        try {
            return algorithmClass.newInstance();
        } catch(Exception e ) {
            throw new NoSuitableStrategyException("Illegal strategy selection request.", e);
        }
    }

}

