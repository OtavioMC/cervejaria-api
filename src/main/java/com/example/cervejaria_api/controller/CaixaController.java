
package com.example.cervejaria_api.controller;

import com.example.cervejaria_api.model.Caixa;
import com.example.cervejaria_api.service.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/caixas")
public class CaixaController {

    @Autowired
    private CaixaService caixaService;

    @GetMapping
    public ResponseEntity<List<Caixa>> findAll() {
        List<Caixa> caixas = caixaService.listarTodos();
        return ResponseEntity.ok(caixas);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Caixa>> findAtivos() {
        List<Caixa> caixas = caixaService.listarAtivos();
        return ResponseEntity.ok(caixas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caixa> findById(@PathVariable Integer id) {
        Caixa caixa = caixaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado"));
        return ResponseEntity.ok(caixa);
    }

    @PostMapping
    public ResponseEntity<Caixa> create(@RequestBody Caixa caixa) {
        Caixa novoCaixa = caixaService.criar(caixa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCaixa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caixa> update(@PathVariable Integer id, @RequestBody Caixa caixa) {
        Caixa caixaAtualizado = caixaService.atualizar(id, caixa);
        return ResponseEntity.ok(caixaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        caixaService.deletar(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Caixa excluído com sucesso");
        return ResponseEntity.ok(response);
    }
}