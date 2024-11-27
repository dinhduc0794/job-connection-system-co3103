package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDTO {
    private Object data;
    private String message;

    private List<String> detail;
    public ResponseDTO() {};
     // Thêm constructor mới chỉ nhận message
     public ResponseDTO(String message) {
        this.message = message;
        this.data = null;  // Gán data là null khi chỉ có message
    }

    // Constructor hiện tại vẫn giữ nguyên
    public ResponseDTO(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}
