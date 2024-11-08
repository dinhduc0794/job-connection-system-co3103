package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "admin")
@Entity
public class AdminEntity extends AccountEntity{
}
