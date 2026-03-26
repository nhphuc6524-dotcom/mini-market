package com.minimarket.repository;

import com.minimarket.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    @Override
    @NonNull
    List<Unit> findAll();

    @Override
    @NonNull
    Optional<Unit> findById(@NonNull Integer id);

    @Override
    @NonNull
    <S extends Unit> S save(@NonNull S entity);
}