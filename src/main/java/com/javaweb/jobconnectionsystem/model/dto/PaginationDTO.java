package com.javaweb.jobconnectionsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaginationDTO<T> implements Serializable {
    private static final long serialVersionUID = 7213600440729202783L;

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int maxPageItems = 9;   // so luong item tren 1 trang
    private int page = 1;            //current page, mac dinh nhay vao khi vao lan dau tien
    private List<T> listResult = new ArrayList<>();
    private Integer limit;
    private Integer totalPage;  // tong so trang, co the tinh duoc tu totalItem va maxPageItems
    private Integer totalItem;  // tong so item

    public void setTotalPage() {
        this.totalPage = (int) Math.ceil((double) this.totalItem / this.maxPageItems);
    }
}
