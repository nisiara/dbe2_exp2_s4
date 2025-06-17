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

	// PASO 1 -> Declarar los datos simulados para las dependencias del servicio
	// Crear instancias simuladas de las interfaces ProductoRepository y ReservaRepository.
	@Mock
	private ProductRepository productoRepositoryMock;
	@Mock
	private BookingRepository reservaRepositoryMock;

	// PASO 2 -> Injectar los datos simulados en el servicio
	// Esta anotación indica que se debe crear una instancia de ReservaService y que los mocks definidos anteriormente
	// deben ser inyectados en esta instancia. Esto permite que reservaService use los repositorios simulados en lugar
	// de los reales.
	@InjectMocks
	private BookingService reservaService;

	// PASO 3 -> Opcional:
	// Si necesitas inicializar algo antes de cada test, usa @BeforeEach
	// @BeforeEach
	// void setUp() {
	//     // Por ejemplo, resetear mocks o crear datos comunes
	// }

	@Test
	public void crearReserva_CuandoHayStock() {

		// ARRANGE - PREPARACION DE DATOS
		// Inicializar y configurar todos los objetos y datos necesarios para la prueba.
		// Esto incluye crear instancias de las clases a probar (System Under Test - SUT) y cualquier dependencia que necesiten.
		// Si se usa la libreria de Mockito, aquí es donde configura el comportamiento esperado de tus mocks es decir,
		// cuando un metodo de un mock es llamado, qué valor debe devolver.


		User user = User.builder()
			.id(1L)
			.name("Juanito Test")
			.build();

		Product productoConStock = Product.builder()
			.id(1L)
			.name("Nombre del producto para testear")
			.description("Un libro muy bonito para testear")
			.price(new BigDecimal("100.00"))
			.stock(5) // Importante: stock > 0
			.build();

		// Se crea un objeto Reserva que se utilizará como argumento para el metodo que se está probando.
		Booking reservaInput = Booking.builder()
			.user(user)
			.product(productoConStock)
			.build();

		// Se define el resultado esperado de la reserva que se guardará en la base de datos.
		Booking reservaGuardadaEsperada = Booking.builder()
			.id(100L) // Opcional, pero simula un ID asignado por la DB
			.user(user)
			.product(productoConStock)
			.dateBooking(LocalDateTime.now())
			.status("Confirmada")
			.build();

		// ACT - CONFIGURACION DE COMPORTAMIENTO DATOS SIMULADOS (MOCKS)
		// Cuando se llame a productoRepositoryMock.findById(1L), devuelve el productoConStock
		when(productoRepositoryMock.findById(1L)).thenReturn(Optional.of(productoConStock));

		// Cuando se llame a productoRepositoryMock.save(cualquier Producto), devuelve el productoConStock actualizado
		when(productoRepositoryMock.save(any(Product.class))).thenReturn(productoConStock);

		// Cuando se llame al metodo save con cualquier objeto de tipo Reserva, se devolverá el objeto reservaGuardadaEsperada.
		when(reservaRepositoryMock.save(any(Booking.class))).thenReturn(reservaGuardadaEsperada);

		// Aquí se llama al metodo crearReserva del servicio, pasando la reserva de entrada.
		// El resultado se almacena en reservaResult.
		Booking reservaResult = reservaService.crearReserva(reservaInput);


		//ASSERT - VERIFICACION RESULTADOS
		// Verificar que la reserva no es nula
		assertNotNull(reservaResult);

		// Verificar que el estado de la reserva sea Confirmada
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
		// 1. Crear un Proveedor
		Supplier proveedor = Supplier.builder()
			.id(10L)
			.name("Proveedor de Libros S.A.")
			.contact("proveedor@libros.com")
			.build();

		// 2. Crear un Producto que esté asociado a ese Proveedor
		Product productoConProveedor = Product.builder()
			.id(100L)
			.name("Libro de Fantasía")
			.description("Una aventura épica")
			.price(new BigDecimal("25.00"))
			.stock(5)
			.supplier(proveedor)
			.build();

		// 3. Crear un Cliente
		User cliente = User.builder()
			.id(1L)
			.name("Ana Garcia")
			.build();

		// 4. Crear la Reserva inicial (con el producto que tiene proveedor)
		Booking reservaInput = Booking.builder()
			.user(cliente)
			.product(productoConProveedor)
			.build();

		// Configurar mocks:
		// Cuando se busque el producto por ID, devuelve el producto con el proveedor asociado
		when(productoRepositoryMock.findById(productoConProveedor.getId())).thenReturn(Optional.of(productoConProveedor));

		// Cuando se guarde el producto (actualización de stock), devuelve el mismo producto modificado
		when(productoRepositoryMock.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			return savedProduct;
		});

		// Cuando se guarde la reserva, devuelve la reserva que el servicio debería haber construido
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
		// Verificar que la reserva se creó y está confirmada
		assertNotNull(resultReserva);
		assertEquals("Reserva Confirmada :)", resultReserva.getStatus());

		// Verificar que el producto dentro de la reserva resultante no es nulo
		assertNotNull(resultReserva.getProduct());

		// Verificar que el proveedor asociado al producto dentro de la reserva es correcto
		assertNotNull(resultReserva.getProduct().getSupplier());
		assertEquals(proveedor.getId(), resultReserva.getProduct().getSupplier().getId());
		assertEquals(proveedor.getName(), resultReserva.getProduct().getSupplier().getName());
		assertEquals(proveedor.getContact(), resultReserva.getProduct().getSupplier().getContact());


	}

}