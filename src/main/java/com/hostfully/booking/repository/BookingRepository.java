package com.hostfully.booking.repository;

import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
                SELECT b FROM Booking b
                WHERE b.rentalPropertyId = :propertyId
                  AND b.status = :status
                  AND b.startDate <= :endDate
                  AND b.endDate >= :startDate
            """)
    List<Booking> findBookings(
            @Param("propertyId") Long propertyId,
            @Param("status") BookingStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
