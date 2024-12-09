package com.javaweb.jobconnectionsystem.service.auth;

import com.javaweb.jobconnectionsystem.model.dto.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
}
