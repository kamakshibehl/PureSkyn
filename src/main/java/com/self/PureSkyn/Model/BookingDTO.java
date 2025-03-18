package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
public class BookingDTO {
    private String bookingId;
    private String userId;
    private String technicianId;
    private String technicianName;
    private BookingUserInfoDTO userInfo;
    private List<BookingServiceInfoDTO> servicesBooked;

    private Payment payment;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public BookingUserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BookingUserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public List<BookingServiceInfoDTO> getServicesBooked() {
        return servicesBooked;
    }

    public void setServicesBooked(List<BookingServiceInfoDTO> servicesBooked) {
        this.servicesBooked = servicesBooked;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
