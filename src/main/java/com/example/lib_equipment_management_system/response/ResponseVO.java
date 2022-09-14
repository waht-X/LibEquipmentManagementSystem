package com.example.lib_equipment_management_system.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseVO {

    /**
     * 状态码
     */
    private boolean success;

    private Object data;

    private String msg;

    private int errorCode = -1;



}
