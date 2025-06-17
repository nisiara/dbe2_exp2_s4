package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserEntity user = userRepository.findByUsername(username).orElseThrow(() ->
//			new UsernameNotFoundException("Usuario no encotrado con el : " + username));
//			return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()) );
//	}
//
//	private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roles) {
//		return roles.stream()
//			.map(role -> new SimpleGrantedAuthority(role.getName()))
//			.collect(Collectors.toList());
//	}

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	User user = userRepository.findByUsername(username)
		.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	return new org.springframework.security.core.userdetails.User(
		user.getUsername(),
		user.getPassword(),
		getAuthorities(user)
	);
}
	private Collection<GrantedAuthority> getAuthorities(User user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return new ArrayList<>(authorities);
	}
}
