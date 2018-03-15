package ro.msg.learning.shop.utility.distance;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import lombok.extern.slf4j.Slf4j;
import ro.msg.learning.shop.domain.Address;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.exception.InvalidRequestException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DistanceCalculator {

    private final GeoApiContext geoApiContext;

    public DistanceCalculator(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    public List<Location> sortLocationsByDistance(List<Location> shopLocations, Address deliveryAddress){

        try{
            DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(
                            geoApiContext,
                            getOrigins(shopLocations) ,
                            getDestinations(deliveryAddress)).await();

            return sortLocationsByDistance(shopLocations, matrix);

        } catch (Exception e){
            log.error("Error while getting DistanceMatrix. ", e);
            throw new InvalidRequestException();
        }
    }

    private static List<Location> sortLocationsByDistance(List<Location> targetLocations, DistanceMatrix matrix){

        Map<Location, Long> distanceMap = new LinkedHashMap<>();

        for(int i = 0; i < targetLocations.size(); i ++){
            distanceMap.put(targetLocations.get(i), matrix.rows[i].elements[0].distance.inMeters );
        }

        List<Map.Entry<Location, Long>> sortedList = new LinkedList<>(distanceMap.entrySet());

        sortedList.sort(Comparator.comparing(Map.Entry::getValue));

        return sortedList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private static String[] getDestinations(Address address){
        String[] value = new String[1];
        value[0] = googleLocationFromAddress(address);
        return value;
    }

    private static String[] getOrigins(List<Location> locations){
        String[] value = new String[locations.size()];

        for(int i = 0 ; i < locations.size() ; i++){
            value[i] = googleLocationFromAddress(locations.get(i).getAddress());
        }
        return value;
    }

    private static String googleLocationFromAddress(Address address){
        return address.getCity() +", " + address.getRegion() + ", " + address.getCountry();
    }

    public static boolean checkIfAddressIsValid(Address address){
        return !address.getCountry().equals("") &&
                !address.getRegion().equals("") &&
                !address.getCity().equals("");
    }

}
