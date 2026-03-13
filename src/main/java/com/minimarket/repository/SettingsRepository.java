package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.model.SettingsEntity;

public interface SettingsRepository extends JpaRepository<SettingsEntity,Integer> {
}