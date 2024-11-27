package com.javaweb.jobconnectionsystem.controller.custom;

import com.javaweb.jobconnectionsystem.model.dto.LoginDTO;
import com.javaweb.jobconnectionsystem.model.response.LoginResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.AccountService;
import com.javaweb.jobconnectionsystem.service.AdminService;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import com.javaweb.jobconnectionsystem.service.impl.AuthServiceImpl;
import com.javaweb.jobconnectionsystem.service.impl.JwtUtils;
import com.javaweb.jobconnectionsystem.service.impl.UserDetailserviceImpl;

import jakarta.validation.Valid;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserDetailserviceImpl userDetailservice;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AccountService accountService;


@PostMapping()
public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
    ResponseDTO responseDTO = new ResponseDTO();
    try {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            responseDTO.setMessage("Validation failed");
            responseDTO.setDetail(errorMessages);
            return ResponseEntity.badRequest().body(responseDTO);
        }

        // Lấy thông tin userId từ database
        Long userId = accountService.getIdAccountByUsername(loginDTO.getUsername());

        // Tạo token chứa cả role và id
        String accessToken = jwtUtils.generateToken(
                userDetailservice.loadUserByUsername(loginDTO.getUsername()),
                String.valueOf(userId)); // Chuyển Long thành String
        String refreshToken = jwtUtils.generateRefreshToken(loginDTO.getUsername());

        // Tạo cookie chứa refreshToken
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(30 * 60)
                .secure(true)
                .sameSite("Strict")
                .build();

        // Chuẩn bị dữ liệu phản hồi
        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);
        user.put("role", jwtUtils.role(accessToken));

        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("user", user);
        response.put("expiresIn", 3600); // Thời gian hết hạn token (giây)

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    } catch (BadCredentialsException ex) {
        responseDTO.setMessage("Invalid credentials");
        responseDTO.setDetail(Collections.singletonList("Username or password is incorrect"));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }
}
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", jwtCookie.toString())
                .body(new ResponseDTO("Logout successful")); // Chỉ truyền message
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            if (!jwtUtils.validateToken(refreshToken)) {
                responseDTO.setMessage("Refresh token is invalid or expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
            }

            String username = jwtUtils.extractUsername(refreshToken);
            String newAccessToken = jwtUtils.generateRefreshToken(username); // Sử dụng generateRefreshToken thay vì
                                                                             // generateToken

            Map<String, String> tokens = new HashMap<String, String>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception ex) {
            responseDTO.setMessage("Error refreshing token");
            responseDTO.setDetail(Collections.singletonList(ex.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

}