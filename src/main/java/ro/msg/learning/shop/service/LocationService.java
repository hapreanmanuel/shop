package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) { this.locationRepository = locationRepository;}

    public Location getLocation(int locationId) {return locationRepository.findOne(locationId);}

    public List<Location> getAllLocations() { return locationRepository.findAll();}

    public void addLocation(Location location) { locationRepository.save(location);}

    public void updateLocation(Location location) {locationRepository.save(location);}

    public void deleteLocation(Location location) { locationRepository.delete(location);}
}
