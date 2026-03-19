package org.example.ordermanagement.service;

import org.example.ordermanagement.dto.request.AuthenticationRequest;
import org.example.ordermanagement.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}