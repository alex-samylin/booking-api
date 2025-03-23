package com.hostfully.booking.repository;

import com.hostfully.booking.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("""
                SELECT bl FROM Block bl
                WHERE bl.rentalPropertyId = :propertyId
                  AND bl.startDate <= :endDate
                  AND bl.endDate >= :startDate
            """)
    List<Block> findBlocks(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
