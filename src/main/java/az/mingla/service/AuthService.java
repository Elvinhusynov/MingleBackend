package az.mingla.service;

import az.mingla.dto.LoginRequest;
import az.mingla.dto.AuthResponse;
import az.mingla.dto.UserRegisterRequest;

public interface AuthService {

    AuthResponse register(UserRegisterRequest request);

    AuthResponse login(LoginRequest request);
}
