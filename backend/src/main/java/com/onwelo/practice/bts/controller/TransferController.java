package com.onwelo.practice.bts.controller;

import com.onwelo.practice.bts.entity.Transfer;
import com.onwelo.practice.bts.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RepositoryRestController
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private TransferService transferService;

    @GetMapping
    public ResponseEntity index() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping(path = "/{id}")
    public Transfer show(@PathVariable("id") Long id) {
        return transferService.getTransferById(id);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Transfer transfer) {
        transferService.addTransfer(transfer);
        return ResponseEntity.ok(transfer);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @Valid @RequestBody Transfer transfer) {
        transfer.setId(id);
        transferService.updateTransfer(transfer);
        return ResponseEntity.ok(transfer);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        transferService.deleteTransfer(id);
        return ResponseEntity.ok().build();
    }
}
