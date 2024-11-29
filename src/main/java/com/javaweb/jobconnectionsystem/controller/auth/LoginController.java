package com.javaweb.jobconnectionsystem.controller.auth;

import com.javaweb.jobconnectionsystem.model.dto.LoginDTO;
import com.javaweb.jobconnectionsystem.model.response.LoginResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.AccountService;
import com.javaweb.jobconnectionsystem.service.auth.AuthService;
import com.javaweb.jobconnectionsystem.component.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AccountService accountService;
   @PostMapping()
    public ResponseEntity<?> loginHere(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            // Kiểm tra lỗi validate từ DTO
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());

                responseDTO.setMessage("Validation failed");
                responseDTO.setDetail(errorMessages);
                return ResponseEntity.badRequest().body(responseDTO);
            }
            LoginResponse loginReponse = new LoginResponse();
            loginReponse.setToken(authService.login(loginDTO));
            loginReponse.setRole(jwtUtils.getRole(loginReponse.getToken()));
            loginReponse.setId(accountService.getIdAccountByUsername(loginDTO.getUsername()));
            return ResponseEntity.ok(loginReponse);
        }  catch (BadCredentialsException ex) {
            responseDTO.setMessage("Invalid credentials");
            responseDTO.setDetail(Collections.singletonList("Username or password is incorrect"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
    }

}