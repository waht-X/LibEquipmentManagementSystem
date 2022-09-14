package com.example.lib_equipment_management_system.controller.test;

import com.example.lib_equipment_management_system.repository.TTTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    TTTRepository tttRepository;

    @RequestMapping("test1")
    public Object test1() {
        return tttRepository.findAll();
    }

    @RequestMapping("test2")
    public Object tets2() {
        return "hello";
    }

}
