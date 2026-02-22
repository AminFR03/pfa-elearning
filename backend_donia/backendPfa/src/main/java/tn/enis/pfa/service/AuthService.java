package tn.enis.pfa.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.enis.pfa.dto.AuthResponse;
import tn.enis.pfa.dto.LoginRequest;
import tn.enis.pfa.dto.RegisterRequest;
import tn.enis.pfa.entity.User;
import tn.enis.pfa.repository.UserRepository;
import tn.enis.pfa.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email déjà utilisé");
		}
		var role = switch (request.role() != null ? request.role().toUpperCase() : "") {
			case "TEACHER" -> User.Role.TEACHER;
			default -> User.Role.LEARNER;
		};
		var user = User.builder()
				.email(request.email())
				.passwordHash(passwordEncoder.encode(request.password()))
				.fullName(request.fullName())
				.role(role)
				.build();
		user = userRepository.save(user);
		var token = jwtService.generateToken(user.getEmail(), user.getRole().name());
		return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
	}

	public AuthResponse login(LoginRequest request) {
		var user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));
		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new IllegalArgumentException("Email ou mot de passe incorrect");
		}
		var token = jwtService.generateToken(user.getEmail(), user.getRole().name());
		return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
	}
}
