package ro.msg.learning.shop.distance;

import com.google.maps.model.DistanceMatrix;
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
@SpringBootTest(properties="spring.jpa.hibernate.ddl-auto=none")
public class GoogleDistanceTest {

    private final Class targetClass = DistanceCalculator.class;

    @Autowired
    private DistanceCalculator distanceCalculator;

    private final Address testAddress = Address.builder()
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
    public void checkAddressToGoogleLocationConversion(){
        final String methodName = "googleLocationFromAddress";

        try{
            Method method =
                    targetClass.getDeclaredMethod(methodName, Address.class);

            method.setAccessible(true);


            final String expected = "Sebes, AB, Romania";
            Object instance = targetClass.newInstance();

            Object result = method.invoke(instance,testAddress);

            assertThat(result.toString()).isEqualTo(expected);

            log.info("Expected value: " + expected);
            log.info("Actual value: " + result.toString());

        } catch(NoSuchMethodException e){
            log.error("Method '" + methodName +"' was not found for class" + targetClass.getName(), e);
        } catch (Exception e){
            log.error("Something went wrong.", e);
        }
    }

    @Test
    public void stringBuilderTest(){
        StringBuilder builder = new StringBuilder();

        String fromAddress1 = "Sebes, AB, Romania";
        String fromAddress2 = "Cluj-Napoca, CJ, Romania";
        String toAddress = "Stockholm, Sweden";

        builder.append(fromAddress1).append(fromAddress2);

        System.out.println(builder.toString());
        System.out.println(builder);

        builder = new StringBuilder();

        System.out.println(builder.append(toAddress).toString());
        System.out.println(builder);

        String[] origins;
        String[] destinations;

    }

    /*
    @Test
    public void checkDistanceMatrixApiRequest(){

        try{
            DistanceMatrix matrix = distanceCalculator.getRequest(locations, testAddress);

            System.out.println(matrix.toString());

            for(int i = 0 ; i < matrix.rows.length; i++){
                System.out.println(matrix.rows[i].elements[0].distance);
            }


        }
        catch(Exception e){
            log.error("Something went wrong. Can't wait this long.", e);
        }
    }

    @Test
    public void checkLocationSortingByDistance(){

        Map<Location, Integer> distanceMap = new LinkedHashMap<>();

        distanceMap.put(locations.get(0), 100);
        distanceMap.put(locations.get(1), 50);
        distanceMap.put(locations.get(2), 80);

        List<Map.Entry<Location, Integer>> sortedList = new LinkedList<>(distanceMap.entrySet());


        List<Location> sorted = DistanceCalculator.sortLocations(sortedList);

        System.out.println("Unsorted: " + locations.toString());
        System.out.println("Sorted:   " + sorted.toString());

        assertThat(sorted).isEqualTo(Arrays.asList(locations.get(1),locations.get(2), locations.get(0)));

    }
    */
}
