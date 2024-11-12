package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "admin")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AdminEntity extends AccountEntity{
}
