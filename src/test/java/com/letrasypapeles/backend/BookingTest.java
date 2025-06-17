package com.letrasypapeles.backend;

import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.entity.Booking;
import com.letrasypapeles.backend.repository.ProductRepository;
import com.letrasypapeles.backend.repository.BookingRepository;
import com.letrasypapeles.backend.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingTest {


	@Mock
	private ProductRepository productoRepositoryMock;
	@Mock
	private BookingRepository reservaRepositoryMock;


	@InjectMocks
	private BookingService reservaService;


	@Test
	public void crearReserva_CuandoHayStock() {

		User user = User.builder()
			.id(1L)
			.name("Juanito Test")
			.build();

		Product productoConStock = Product.builder()
			.id(1L)
			.name("Nombre del producto para testear")
			.description("Un libro muy bonito para testear")
			.price(new BigDecimal("100.00"))
			.stock(5)
			.build();

		Booking reservaInput = Booking.builder()
			.user(user)
			.product(productoConStock)
			.build();


		Booking reservaGuardadaEsperada = Booking.builder()
			.id(100L)
			.user(user)
			.product(productoConStock)
			.dateBooking(LocalDateTime.now())
			.status("Confirmada")
			.build();

		// ACT - CONFIGURACION DE COMPORTAMIENTO DATOS SIMULADOS (MOCKS)
		when(productoRepositoryMock.findById(1L)).thenReturn(Optional.of(productoConStock));

		when(productoRepositoryMock.save(any(Product.class))).thenReturn(productoConStock);

		when(reservaRepositoryMock.save(any(Booking.class))).thenReturn(reservaGuardadaEsperada);

		Booking reservaResult = reservaService.crearReserva(reservaInput);


		//ASSERT - VERIFICACION RESULTADOS
		assertNotNull(reservaResult);

		assertEquals("Confirmada", reservaResult.getStatus());

	}

	@Test
	public void rechazarReserva_CuandoNoHayStock(){
		User cliente = User.builder()
			.id(2L)
			.name("Juanito Otro Test")
			.build();

		Product productoSinStock = Product.builder()
			.id(2L)
			.name("Otro producto para testear")
			.description("Una descripcion para producto testeado")
			.price(new BigDecimal("300.00"))
			.stock(0)
			.build();

		Booking reservaInput = Booking.builder()
			.user(cliente)
			.product(productoSinStock)
			.build();

		// ACT - CONFIGURACION DE COMPORTAMIENTO DATOS SIMULADOS (MOCKS)
		when(productoRepositoryMock.findById(2L)).thenReturn(Optional.of(productoSinStock));

		//ASSERT - VERIFICACION RESULTADOS
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			reservaService.crearReserva(reservaInput);
		}, "Reserva no realizada");

		assertEquals("No hay stock disponible para el producto: " + productoSinStock.getName(), exception.getMessage(), "El mensaje de error no coincide");

	}

	@Test
	void mantenerRelacionProductoProveedor() {
		// ARRANGE
		Supplier proveedor = Supplier.builder()
			.id(10L)
			.name("Proveedor de Libros S.A.")
			.contact("proveedor@libros.com")
			.build();

		Product productoConProveedor = Product.builder()
			.id(100L)
			.name("Libro de Fantasía")
			.description("Una aventura épica")
			.price(new BigDecimal("25.00"))
			.stock(5)
			.supplier(proveedor)
			.build();

		User cliente = User.builder()
			.id(1L)
			.name("Ana Garcia")
			.build();

		Booking reservaInput = Booking.builder()
			.user(cliente)
			.product(productoConProveedor)
			.build();

		// Configurar mocks:
		when(productoRepositoryMock.findById(productoConProveedor.getId())).thenReturn(Optional.of(productoConProveedor));

		when(productoRepositoryMock.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			return savedProduct;
		});

		Booking reservaGuardadaEsperada = Booking.builder()
			.id(1L) // ID simulado para la reserva guardada
			.user(cliente)
			.product(productoConProveedor) // El producto original con su proveedor
			.dateBooking(LocalDateTime.now()) // Será establecida por el servicio
			.status("Reserva Confirmada :)") // Será establecida por el servicio
			.build();
		when(reservaRepositoryMock.save(any(Booking.class))).thenReturn(reservaGuardadaEsperada);

		// ACT
		Booking resultReserva = reservaService.crearReserva(reservaInput);

		// ASSERT
		assertNotNull(resultReserva);
		assertEquals("Reserva Confirmada :)", resultReserva.getStatus());

		assertNotNull(resultReserva.getProduct());

		assertNotNull(resultReserva.getProduct().getSupplier());
		assertEquals(proveedor.getId(), resultReserva.getProduct().getSupplier().getId());
		assertEquals(proveedor.getName(), resultReserva.getProduct().getSupplier().getName());
		assertEquals(proveedor.getContact(), resultReserva.getProduct().getSupplier().getContact());


	}

}