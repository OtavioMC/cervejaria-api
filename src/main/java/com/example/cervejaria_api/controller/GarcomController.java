package com.example.cervejaria_api.controller;

import com.example.cervejaria_api.model.Garcom;
import com.example.cervejaria_api.service.GarcomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/garcons")
@CrossOrigin(origins = "*")
public class GarcomController {

    @Autowired
    private GarcomService garcomService;

    /**
     * GET /api/garcons - Lista todos os garçons
     */
    @GetMapping
    public ResponseEntity<List<Garcom>> findAll() {
        List<Garcom> garcons = garcomService.findAll();
        return ResponseEntity.ok(garcons);
    }

    /**
     * GET /api/garcons/ativos - Lista garçons ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<Garcom>> findAtivos() {
        List<Garcom> garcons = garcomService.findAtivos();
        return ResponseEntity.ok(garcons);
    }

    /**
     * GET /api/garcons/{id} - Busca garçom por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Garcom> findById(@PathVariable Integer id) {
        Garcom garcom = garcomService.findById(id);
        return ResponseEntity.ok(garcom);
    }

    /**
     * GET /api/garcons/cpf/{cpf} - Busca garçom por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Garcom> findByCpf(@PathVariable String cpf) {
        Optional<Garcom> garcom = garcomService.findByCpf(cpf);
        return garcom.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/garcons/email/{email} - Busca garçom por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Garcom> findByEmail(@PathVariable String email) {
        Optional<Garcom> garcom = garcomService.findByEmail(email);
        return garcom.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/garcons/buscar?nome= - Busca por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Garcom>> findByNome(@RequestParam String nome) {
        List<Garcom> garcons = garcomService.findByNome(nome);
        return ResponseEntity.ok(garcons);
    }

    /**
     * POST /api/garcons - Cria novo garçom
     */
    @PostMapping
    public ResponseEntity<Garcom> create(@RequestBody Garcom garcom) {
        Garcom novoGarcom = garcomService.create(garcom);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGarcom);
    }

    /**
     * PUT /api/garcons/{id} - Atualiza garçom
     */
    @PutMapping("/{id}")
    public ResponseEntity<Garcom> update(@PathVariable Integer id, @RequestBody Garcom garcom) {
        Garcom garcomAtualizado = garcomService.update(id, garcom);
        return ResponseEntity.ok(garcomAtualizado);
    }

    /**
     * DELETE /api/garcons/{id} - Inativa garçom
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        garcomService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Garçom inativado com sucesso");
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/garcons/{id}/permanente - Deleta permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Map<String, String>> deletePermanente(@PathVariable Integer id) {
        garcomService.deletePermanente(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Garçom excluído permanentemente");
        return ResponseEntity.ok(response);
    }
}
