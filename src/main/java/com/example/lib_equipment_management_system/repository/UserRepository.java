package com.example.lib_equipment_management_system.repository;

import com.example.lib_equipment_management_system.entity.EquipmentUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<EquipmentUserEntity, Integer>, JpaSpecificationExecutor<EquipmentUserEntity> {

    boolean existsByAccount(String account);

    EquipmentUserEntity findIdByAccount(String account);


    EquipmentUserEntity findByAccount(String account);

}
