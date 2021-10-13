package com.staybooking.service;

import com.staybooking.model.Stay;
import com.staybooking.repository.LocationRepository;
import com.staybooking.repository.StayAvailabilityRepository;
import com.staybooking.repository.StayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private StayRepository stayRepository;
    private StayAvailabilityRepository stayAvailabilityRepository;
    private LocationRepository locationRepository;

    //step1: filtering based on location
    @Autowired
    public SearchService(StayRepository stayRepository, StayAvailabilityRepository stayAvailabilityRepository, LocationRepository locationRepository) {
        this.stayRepository = stayRepository;
        this.stayAvailabilityRepository = stayAvailabilityRepository;
        this.locationRepository = locationRepository;
    }

    // step2 : filtering based on availability
    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        //allow same day check-in and check-out
        long duration = Duration.between(checkinDate.atStartOfDay(), checkoutDate.atStartOfDay()).toDays();
        List<Long> filteredStayIds = stayAvailabilityRepository.findByDateBetweenAndStateIsAvailable(stayIds, checkinDate, checkoutDate.minusDays(1), duration);
        //step3: filtering based on stay id and guest number
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }
}