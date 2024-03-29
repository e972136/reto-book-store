/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gaspar.controller;

import com.gaspar.dto.SalesRequest;
import com.gaspar.dto.SaleResponse;
import com.gaspar.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 * @author ds010102
 */
@RestController
@RequestMapping(path="/sales")
@Slf4j
@RequiredArgsConstructor
public class SaleController {
    private final SaleService service;
    
    @PostMapping
    ResponseEntity<SaleResponse> newSale(@Valid @RequestBody SalesRequest salesRequest){
        SaleResponse newSale=null;
        try {
            newSale = service.newSale(salesRequest);
        }
        catch (RuntimeException e){
            log.info(""+e);
            throw e;
        }
        catch (Exception e){
            log.info(""+e);
            throw e;
        }
        return new ResponseEntity<>(newSale, HttpStatus.CREATED);
    }
}
