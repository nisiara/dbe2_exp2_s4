package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AuthResponseDTO;
import com.letrasypapeles.backend.dto.LoginDTO;
import com.letrasypapeles.backend.dto.RegisterDTO;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.RoleRepository;
import com.letrasypapeles.backend.repository.UserRepository;
import com.letrasypapeles.backend.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtGenerator jwtGenerator;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtGenerator.generateToken(authentication);
			return ResponseEntity.ok(new AuthResponseDTO(token));
	}

	@PostMapping("registro")
	public ResponseEntity<String> registro(@RequestBody RegisterDTO registerDTO) {
		if(userRepository.existsByUsername(registerDTO.getUsername())) {
			return ResponseEntity.badRequest().body("El usuario ya existe");
		}
		User user = new User();
		user.setUsername(registerDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

		Role role = roleRepository.findByName("CLIENTE").get();
		user.setRoles(Collections.singletonList(role));

		user.setName(registerDTO.getName());
		user.setEmail(registerDTO.getEmail());

		userRepository.save(user);

		return new ResponseEntity<>("Usuario registrado de forma exitosa.", HttpStatus.CREATED);

	}

}
