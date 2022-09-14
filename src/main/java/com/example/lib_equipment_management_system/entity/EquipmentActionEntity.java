package com.example.lib_equipment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "equipment_action")
public class EquipmentActionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "userId")
    private int userId;
    @Basic
    @Column(name = "record")
    private String record;
    @Basic
    @Column(name = "level")
    private Byte level;
    @Basic
    @Column(name = "addTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;
}
