package com.habitlab.backend.service;

import com.habitlab.backend.dto.AuthLoginRequestDTO;
import com.habitlab.backend.dto.AuthRegisterUserRequestDTO;
import com.habitlab.backend.dto.AuthResponseDTO;

public interface IUserDetailService {
    AuthResponseDTO login(AuthLoginRequestDTO request);
    AuthResponseDTO register(AuthRegisterUserRequestDTO request);
}
