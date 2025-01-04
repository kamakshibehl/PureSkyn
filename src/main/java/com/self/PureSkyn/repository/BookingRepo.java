package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepo extends MongoRepository<Booking, Integer> {
    List<Booking> getPendingBookings();

    List<Booking> findByDate(LocalDate date);

    List<Booking> findByAssigned(boolean b);
}
