package ro.msg.learning.shop.distance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.utility.distance.DistanceCalculator;

import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleDistanceTest {

    @Autowired
    private DistanceCalculator distanceCalculator;

    private final Address delivery = Address.builder()
            .fullAddress("To the mooon!")
            .city("Sebes")
            .region("AB")
            .country("Romania")
            .build();

    private final List<Location> locations = Arrays.asList(
            Location.builder()
                    .name("Main Location")
                    .address(Address.builder()
                            .fullAddress("Str. Dorobantilor 77")
                            .city("Cluj-Napoca")
                            .country("Romania")
                            .region("CJ")
                            .build())
                    .build(),
            Location.builder()
                    .name("Secondary Location")
                    .address(Address.builder()
                            .fullAddress("Str. Traian 23B7")
                            .city("Bucuresti")
                            .country("Romania")
                            .region("B")
                            .build())
                    .build(),
            Location.builder()
                    .name("Third Location")
                            .address(Address.builder()
                                    .fullAddress("Str. Decebal7")
                                    .city("Timisoara")
                                    .country("Romania")
                                    .region("TM")
                                    .build())
                    .build());

    @Test
    public void sortLocationsByDistance(){

        log.info("=========================================================================");
        log.info("Test: Sort locations based on google distance matrix ");

        List<Location> sortedLocations = distanceCalculator.sortLocationsByDistance(locations,delivery);

        //                                              TO : Sebes, AB, Romania
        List<Location> expected = Arrays.asList(
                locations.get(0),                       // Cluj-Napoca, CJ, Romania     ~110 km
                locations.get(2),                       // Timisoara, TM, Romania       ~200 km
                locations.get(1)                        // Bucuresti, B, Romania        ~330 km
        );

        log.info("Delivery address: {}", delivery);
        log.info("Available locations: {}", locations);
        log.info("Sorted locations: {}", sortedLocations);

        assertThat(sortedLocations).isEqualTo(expected);
    }
}
