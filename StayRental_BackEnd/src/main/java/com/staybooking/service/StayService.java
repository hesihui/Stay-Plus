package com.staybooking.service;

import com.staybooking.exception.StayDeleteException;
import com.staybooking.model.*;
import com.staybooking.repository.LocationRepository;
import com.staybooking.repository.ReservationRepository;
import com.staybooking.repository.StayRepository;
import com.staybooking.service.GeoEncodingService;
import com.staybooking.service.ImageStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;
    private LocationRepository locationRepository;
    private ReservationRepository reservationRepository;
    private ImageStorageService imageStorageService;
    private GeoEncodingService geoEncodingService;

    @Autowired
    public StayService(StayRepository stayRepository, LocationRepository locationRepository, ReservationRepository reservationRepository, ImageStorageService imageStorageService, GeoEncodingService geoEncodingService) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.imageStorageService = imageStorageService;
        this.geoEncodingService = geoEncodingService;
    }

    //listing all the stays uploaded by the current log-in host
    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    //find stay by stay id
    public Stay findByIdAndHost(Long stayId) {
        return stayRepository.findById(stayId).orElse(null);
    }

    // the upload functionality for the host
    public void add(Stay stay, MultipartFile[] images) {
        LocalDate date = LocalDate.now().plusDays(1);

        //generate availabity dates
        // (from the date of uploading the stay to the next 30 days)
        List<StayAvailability> availabilities = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            availabilities.add(new StayAvailability.Builder().setId(new StayAvailabilityKey(stay.getId(), date)).setStay(stay).setState(StayAvailabilityState.AVAILABLE).build());
            date = date.plusDays(1);
        }
        stay.setAvailabilities(availabilities);

        //reads images of stays from urls
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        stayRepository.save(stay);

        Location location = geoEncodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }

    // the delete functionality for the host
    public void delete(Long stayId) throws StayDeleteException {
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(new Stay.Builder().setId(stayId).build(), LocalDate.now());
       // if the stay is booked by some guest, the host can't delete the stay
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
        stayRepository.deleteById(stayId);
    }
}