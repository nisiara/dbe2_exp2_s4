package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	Optional<Client> findByEmail(String email);
	boolean existsByEmail(String email);
}
