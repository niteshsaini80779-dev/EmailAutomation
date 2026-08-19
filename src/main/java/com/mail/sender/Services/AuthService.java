package com.mail.sender.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mail.sender.dto.LoginRequest;

@Service
public class AuthService {
	private final String username;
	private final String password;

	public AuthService(@Value("${app.auth.username}") String username, @Value("${app.auth.password}") String password) {

		this.username = username;
		this.password = password;
	}

	public boolean login(LoginRequest request) {
		if (request.getUsername().equals(username) && request.getPassword().equals(password)) {
			return true;
		}
		return false;

	}

}
