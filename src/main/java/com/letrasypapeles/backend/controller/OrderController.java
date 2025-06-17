package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {

	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<List<Order>> obtenerTodos() {
		List<Order> orders = orderService.obtenerTodos();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> obtenerPorId(@PathVariable Long id) {
		return orderService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/byUser/{userId}")
	public ResponseEntity<List<Order>> obtenerPorUserId(@PathVariable Long userId) {
		List<Order> ordersByUser = orderService.obtenerPorUserId(userId);
		return ResponseEntity.ok(ordersByUser);
	}

	@PostMapping("/create")
	public ResponseEntity<Order> crearPedido(@RequestBody Order order) {
		Order newOrder = orderService.guardar(order);
		return ResponseEntity.ok(newOrder);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Order> actualizarPedido(@PathVariable Long id, @RequestBody Order order) {
		return orderService.obtenerPorId(id)
			.map(o -> {
				order.setId(id);
				Order pedidoActualizado = orderService.guardar(order);
				return ResponseEntity.ok(pedidoActualizado);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
		return orderService.obtenerPorId(id)
			.map(o -> {
				orderService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}


}
