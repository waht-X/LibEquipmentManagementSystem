package com.example.lib_equipment_management_system.repository;

import com.example.lib_equipment_management_system.entity.EquipmentActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<EquipmentActionEntity, Integer> {
}
