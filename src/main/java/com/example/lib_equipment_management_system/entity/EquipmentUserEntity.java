package com.example.lib_equipment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@ToString
@Table(name = "equipment_user")
public class EquipmentUserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @Nullable
    private int id;
    @Basic
    @Column(name = "username")
    @Nullable
    private String username;
    @Basic
    @Column(name = "account")
    private String account;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "email")
    @Nullable
    private String email;
    @Basic
    @Column(name = "photo")
    @Nullable
    private String photo;
    @Basic
    @Column(name = "level")
    @Nullable
    private Byte level = 0;
    @Basic
    @Column(name = "addTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime addTime;
    @Basic
    @Column(name = "modifyTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime modifyTime;
    @Basic
    @Column(name = "status")
    @Nullable
    private Byte status = 0;

}
