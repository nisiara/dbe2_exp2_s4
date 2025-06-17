package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByuserId(Long clienteId);
	List<Booking> findByProductId(Long productoId);
	List<Booking> findByStatus(String estado);
}
