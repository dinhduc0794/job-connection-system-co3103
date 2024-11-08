package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "postdate")
@Entity
public class PostDateEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;
}
