package com.letrasypapeles.backend;

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.repository.SupplierRepository;
import com.letrasypapeles.backend.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierTest {
	@Mock
	private SupplierRepository proveedorRepositoryMock;

	@InjectMocks
	private SupplierService proveedorService;

	@Test
	public void crearProveedor_CuandoTieneTodosLosDatos() {
		Supplier proveedor = new Supplier(1L, "Editorial Pinguino", "pinguino@mail.com");

		when(proveedorRepositoryMock.save(any(Supplier.class))).thenReturn(proveedor);

		// ACT
		Supplier savedProveedor = proveedorService.crearProveedor(proveedor);

		// ASSERT
		assertNotNull(savedProveedor);
		assertEquals("Editorial Pinguino", savedProveedor.getName());
		assertEquals("pinguino@mail.com", savedProveedor.getContact());

	}

	@Test
	public void rechazarCracion_CuandoProveedorSinNombre() {
		Supplier proveedor = new Supplier(2L, "", "pinguino@mail.com");

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			proveedorService.crearProveedor(proveedor);
		});

		assertEquals("El nombre del proveedor es obligatorio.", thrown.getMessage());
	}

	@Test
	void rechazarCreacion_CuandoProveedorSinContacto() {
		// ARRANGE
		Supplier proveedor = new Supplier(3L, "Editorial Pinguino", "");

		// ACT & ASSERT
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			proveedorService.crearProveedor(proveedor);
		}, "Debería lanzar IllegalArgumentException cuando el contacto es vacío");

		assertEquals("El contacto del proveedor es obligatorio.", thrown.getMessage(), "El mensaje de error no coincide");
		verify(proveedorRepositoryMock, never()).save(any(Supplier.class));
	}

	@Test
	void rechazarCreacion_CuandoProveedorEsNull() {
		// ARRANGE
		Supplier proveedor = null;

		// ACT & ASSERT
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			proveedorService.crearProveedor(null);
		});

		assertEquals("El proveedor no puede ser nulo.", thrown.getMessage());
		verify(proveedorRepositoryMock, never()).save(any(Supplier.class));
	}

}
