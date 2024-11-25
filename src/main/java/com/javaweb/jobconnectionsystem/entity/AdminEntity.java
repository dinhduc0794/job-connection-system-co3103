package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Table(name = "admin")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AdminEntity extends AccountEntity{
}
