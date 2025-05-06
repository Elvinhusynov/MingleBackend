package az.mingle.service;

import az.mingle.dto.LoginRequest;
import az.mingle.dto.AuthResponse;
import az.mingle.dto.UserRegisterRequest;

public interface AuthService {

    AuthResponse register(UserRegisterRequest request);

    AuthResponse login(LoginRequest request);
}
