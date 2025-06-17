package com.letrasypapeles.backend.dto;

import lombok.Data;

@Data
public class RegisterDTO {
	private String username;
	private String password;
	private String name;
	private String email;
}
