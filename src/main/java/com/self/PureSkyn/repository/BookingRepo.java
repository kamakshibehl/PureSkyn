package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.Model.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepo extends MongoRepository<Booking, String> {

    List<Booking> findByDate(LocalDate date);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findAllByOrderByDateDesc();

    List<Booking> findByUserId(String userId);

}
