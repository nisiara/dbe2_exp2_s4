package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> obtenerTodos() {
		return orderRepository.findAll();
	}

	public Optional<Order> obtenerPorId(Long id) {
		return orderRepository.findById(id);
	}

	public Order guardar(Order pedido) {
		return orderRepository.save(pedido);
	}

	public void eliminar(Long id) {
		orderRepository.deleteById(id);
	}

	public List<Order> obtenerPorUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	public List<Order> obtenerPorEstado(String estado) {
		return orderRepository.findByStatus(estado);
	}
}
