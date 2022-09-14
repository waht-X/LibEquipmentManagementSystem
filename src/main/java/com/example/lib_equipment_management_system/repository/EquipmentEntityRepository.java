package com.example.lib_equipment_management_system.repository;

import com.example.lib_equipment_management_system.entity.EquipmentFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentEntityRepository extends JpaRepository<EquipmentFormEntity, Integer>
        , JpaSpecificationExecutor<EquipmentFormEntity> {
}
