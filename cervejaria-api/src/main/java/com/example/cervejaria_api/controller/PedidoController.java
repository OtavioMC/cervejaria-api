package com.example.cervejaria_api.controller;

import com.example.cervejaria_api.model.Pedido;
import com.example.cervejaria_api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * GET /api/pedidos - Lista todos os pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        List<Pedido> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/{id} - Busca pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Integer id) {
        Pedido pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * GET /api/pedidos/{id}/completo - Busca pedido com itens
     */
    @GetMapping("/{id}/completo")
    public ResponseEntity<Pedido> findByIdWithItens(@PathVariable Integer id) {
        Pedido pedido = pedidoService.findByIdWithItens(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * GET /api/pedidos/status/{status} - Lista por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> findByStatus(@PathVariable String status) {
        List<Pedido> pedidos = pedidoService.findByStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/mesa/{numeroMesa} - Lista por mesa
     */
    @GetMapping("/mesa/{numeroMesa}")
    public ResponseEntity<List<Pedido>> findByMesa(@PathVariable Integer numeroMesa) {
        List<Pedido> pedidos = pedidoService.findByMesa(numeroMesa);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/mesa/{numeroMesa}/abertos - Lista pedidos abertos da mesa
     */
    @GetMapping("/mesa/{numeroMesa}/abertos")
    public ResponseEntity<List<Pedido>> findAbertosbyMesa(@PathVariable Integer numeroMesa) {
        List<Pedido> pedidos = pedidoService.findAbertosbyMesa(numeroMesa);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/garcom/{garcomId} - Lista por garçom
     */
    @GetMapping("/garcom/{garcomId}")
    public ResponseEntity<List<Pedido>> findByGarcom(@PathVariable Integer garcomId) {
        List<Pedido> pedidos = pedidoService.findByGarcom(garcomId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/periodo?inicio=&fim= - Lista por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<Pedido>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<Pedido> pedidos = pedidoService.findByPeriodo(inicio, fim);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/status/{status}/count - Conta por status
     */
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Map<String, Long>> countByStatus(@PathVariable String status) {
        Long count = pedidoService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/pedidos - Cria novo pedido
     */
    @PostMapping
    public ResponseEntity<Pedido> create(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.create(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    /**
     * PUT /api/pedidos/{id} - Atualiza pedido
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Pedido pedidoAtualizado = pedidoService.update(id, pedido);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * PATCH /api/pedidos/{id}/fechar - Fecha pedido
     */
    @PatchMapping("/{id}/fechar")
    public ResponseEntity<Pedido> fechar(@PathVariable Integer id) {
        Pedido pedido = pedidoService.fechar(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * PATCH /api/pedidos/{id}/pagar - Paga pedido
     */
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Pedido> pagar(@PathVariable Integer id) {
        Pedido pedido = pedidoService.pagar(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * PATCH /api/pedidos/{id}/cancelar - Cancela pedido
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(@PathVariable Integer id) {
        Pedido pedido = pedidoService.cancelar(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * DELETE /api/pedidos/{id} - Remove pedido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        pedidoService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pedido excluído com sucesso");
        return ResponseEntity.ok(response);
    }
}
