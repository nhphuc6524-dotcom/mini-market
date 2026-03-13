package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.minimarket.model.SettingsEntity;
import com.minimarket.repository.SettingsRepository;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class SettingsController {

    private final SettingsRepository repository;

    public SettingsController(SettingsRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public SettingsEntity getSettings(){

        List<SettingsEntity> list = repository.findAll();

        if(list.isEmpty()){
            SettingsEntity s = new SettingsEntity();
            s.setStoreName("MiniMarket");
            s.setReceiptPaperSize("80mm");
            s.setLowStockAlert(10);

            repository.save(s);
            return s;
        }

        return list.get(0);
    }

    @PutMapping("/{id}")
    public SettingsEntity update(@PathVariable Integer id,
                                 @RequestBody SettingsEntity s){

        s.setId(id);
        return repository.save(s);
    }

}