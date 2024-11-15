package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "procompany")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class ProCompanyEntity extends CompanyEntity {
    @Column(name = "regist_date", nullable = false)
    private Date registDate;

    @Column(name = "expire_date")
    private Date expireDate;
}

