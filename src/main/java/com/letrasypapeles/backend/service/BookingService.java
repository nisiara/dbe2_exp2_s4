package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Booking;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.BookingRepository;
import com.letrasypapeles.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

	private BookingRepository bookingRepository;
	private ProductRepository productRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository, ProductRepository productRepository) {
		this.bookingRepository = bookingRepository;
		this.productRepository = productRepository;
	}

	public List<Booking> obtenerTodas() {
		return bookingRepository.findAll();
	}

	public Optional<Booking> obtenerPorId(Long id) {
		return bookingRepository.findById(id);
	}

	public Booking guardar(Booking reserva) {
		return bookingRepository.save(reserva);
	}

	public void eliminar(Long id) {
		bookingRepository.deleteById(id);
	}

	public List<Booking> obtenerPorUserId(Long userId) {
		return bookingRepository.findByuserId(userId);
	}

	public List<Booking> obtenerPorProductoId(Long productoId) {
		return bookingRepository.findByProductId(productoId);
	}

	public List<Booking> obtenerPorEstado(String estado) {
		return bookingRepository.findByStatus(estado);
	}

	public Booking crearReserva(Booking reserva) {
		// Verificar que el producto existe
		Optional<Product> productoOpt = productRepository.findById(reserva.getProduct().getId());


		if (productoOpt.isEmpty()) {
			throw new RuntimeException("Producto no encontrado");
		}

		Product producto = productoOpt.get();

		// Verificar stock disponible
		if (producto.getStock() > 0) {
			reserva.setStatus("Reserva Confirmada :)");
			reserva.setDateBooking(LocalDateTime.now());

			// Actualizar stock del producto
			producto.setStock(producto.getStock() - 1);
			productRepository.save(producto);

		} else {
			// Lanzar excepción si no hay stock
			throw new RuntimeException("No hay stock disponible para el producto: " + producto.getName());
		}

		return bookingRepository.save(reserva);
	}
}
