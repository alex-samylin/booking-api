package com.hostfully.booking.repository;

import com.hostfully.booking.model.RentalProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalPropertyRepository extends JpaRepository<RentalProperty, Long> {
}
