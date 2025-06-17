package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public List<User> obtenerTodos(){
    return userRepository.findAll();
  }

  public Optional<User> obtenerPorId(Long id){
    return userRepository.findById(id);
  }

  public Optional<User> obtenerPorUsername(String username){
    return userRepository.findByUsername(username);
  }
  
}
