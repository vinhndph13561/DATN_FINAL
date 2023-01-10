package com.example.nhom34.controller;

import com.example.nhom34.entities.Bill;
import com.example.nhom34.repository.BillRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class BillController {
    BillRepository billRepository;

    @GetMapping("")
    public List<Bill> getAll(){
        return this.billRepository.findAll();
    }
}
