package com.example.lib_equipment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(name = "equipment_form")
public class EquipmentFormEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @Nullable
    private int id;
    @Basic
    @Column(name = "serial")
    @Nullable
    private String serial;
    @Basic
    @Column(name = "name")
    @Nullable
    private String name;
    @Basic
    @Column(name = "category")
    @Nullable
    private Byte category;
    @Basic
    @Column(name = "type")
    @Nullable
    private String type;
    @Basic
    @Column(name = "specification")
    @Nullable
    private String specification;
    @Basic
    @Column(name = "price")
    @Nullable
    private Double price;
    @Basic
    @Column(name = "number")
    @Nullable
    private Integer number;
    @Basic
    @Column(name = "buyDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime buyDate;
    @Basic
    @Column(name = "supportServer")
    @Nullable
    private String supportServer;
    @Basic
    @Column(name = "expirationDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime expirationDate;
    @Basic
    @Column(name = "applicant")
    @Nullable
    private String applicant;
    @Basic
    @Column(name = "fixDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime fixDate;
    @Basic
    @Column(name = "fixServer")
    @Nullable
    private String fixServer;
    @Basic
    @Column(name = "sumPrice")
    @Nullable
    private Double sumPrice;
    @Basic
    @Column(name = "position")
    @Nullable
    private String position;
    @Basic
    @Column(name = "department")
    private int department;
    @Basic
    @Column(name = "reason")
    @Nullable
    private String reason;
    @Basic
    @Column(name = "note")
    @Nullable
    private String note;
    @Basic
    @Column(name = "addTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime addTime;
    @Basic
    @Column(name = "status")
    @Nullable
    private Byte status;
    @Basic
    @Column(name = "userId")
    @Nullable
    private Integer userId;
    @Basic
    @Column(name = "formType")
    private byte formType;
    @Basic
    @Column(name = "formStatus")
    private Byte formStatus;
    @Basic
    @Nullable
    @Column(name = "level")
    private Byte level;
    @Basic
    @Nullable
    @Column(name = "handlerId")
    private Integer handlerId;
    @Basic
    @Nullable
    @Column(name = "modifyTime")
    private LocalDateTime modifyTime;

}
