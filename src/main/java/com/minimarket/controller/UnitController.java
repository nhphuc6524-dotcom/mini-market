package com.minimarket.controller;

import com.minimarket.model.Unit;
import com.minimarket.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@CrossOrigin("*")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @GetMapping
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unit> getUnitById(@PathVariable Integer id) {
        if (id == null) return ResponseEntity.badRequest().build();
        
        return unitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        return unitRepository.save(unit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable Integer id, @RequestBody Unit unitDetails) {
        if (id == null || unitDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        return unitRepository.findById(id).map(existingUnit -> {
            String name = unitDetails.getName();
            if (name != null) {
                existingUnit.setName(name);
            }
            // Cách này giúp IDE nhận diện chắc chắn existingUnit không null
            Unit toSave = existingUnit; 
            if (toSave != null) {
                Unit updated = unitRepository.save(toSave);
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.internalServerError().<Unit>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return unitRepository.findById(id).map(unitToDelete -> {
            // Ép kiểu hoặc kiểm tra null lần cuối để thỏa mãn trình biên dịch
            Unit toDelete = unitToDelete;
            if (toDelete != null) {
                unitRepository.delete(toDelete);
            }
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}