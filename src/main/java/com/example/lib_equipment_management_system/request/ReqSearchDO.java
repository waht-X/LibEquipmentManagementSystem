package com.example.lib_equipment_management_system.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReqSearchDO {

    //设备类别
    @Nullable
    private Integer category;
    //表单类型
    @Nullable
    private Integer formType;
    //申请部门
    @Nullable
    private Integer department;
    //搜索的表单的时间范围
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    private LocalDateTime endTime;
    @Nullable
    private Byte formStatus;
    //搜索的设备名称
    @Nullable
    private String name;
    //分页查询
    //当前页码
    private int pageIndex;
    //可分页总数
//    private int pageTotal;
//    每页查询量
    private int pageSize;


}
