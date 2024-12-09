package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "admin")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AdminEntity extends AccountEntity{
    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private List<ReportEntity> reports;
}
