package com.example.lib_equipment_management_system;

import com.example.lib_equipment_management_system.repository.EquipmentEntityRepository;
import com.example.lib_equipment_management_system.repository.TTTRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibEquipmentManagementSystemApplicationTests {

    @Test
    void contextLoads(@Autowired TTTRepository repository) {

        System.out.println(repository.findAll());

    }

    @Test
    void test(@Autowired EquipmentEntityRepository equipmentEntityRepository) {

    }
}
