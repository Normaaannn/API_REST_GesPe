package com.example.GesPeSpring.Tablas.InfoEmpresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info_empresa")
public class InfoEmpresaController {

    @Autowired
    private InfoEmpresaRepository infoEmpresaRepository;

    @Autowired
    private InfoEmpresaService infoEmpresaService;

    @GetMapping
    public Optional<InfoEmpresa> obtenerInfoEmpresa() {
        return infoEmpresaService.obtenerInfoEmpresa();
    }

    @PatchMapping
    public String actualizarInfoEmpresa(@RequestBody Map<String, Object> updates) {
        InfoEmpresa infoEmpresa = infoEmpresaService.obtenerInfoEmpresa()
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.updateValue(infoEmpresa, updates);  // Solo se actualizan los campos enviados
            infoEmpresaRepository.save(infoEmpresa);
            return "Información actualizada";  // Solo se devuelve un mensaje de texto
        } catch (Exception e) {
            return "Error actualizando información: " + e.getMessage();  // Si hay error, se devuelve un mensaje de error
        }
    }

    @PatchMapping("/actualizarLogo")
    public ResponseEntity<String> actualizarLogo(@RequestBody Map<String, String> update) {

        if (update == null || update.isEmpty()) {
            return ResponseEntity.badRequest().body("Los datos no pueden estar vacíos.");
        }

        String logoUrl = update.get("logoUrl");

        if (logoUrl == null) {
            return ResponseEntity.badRequest().body("Debes proporcionar la url del logo.");
        }

        InfoEmpresa infoEmpresa = infoEmpresaService.obtenerInfoEmpresa()
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        infoEmpresa.setLogoUrl(logoUrl);
        infoEmpresaRepository.save(infoEmpresa);
        return ResponseEntity.ok("Logo actualizado");
    }

    @GetMapping("/obtenerLogo")
    public ResponseEntity<String> obtenerLogo() {
        InfoEmpresa infoEmpresa = infoEmpresaService.obtenerInfoEmpresa()
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        return ResponseEntity.ok(infoEmpresa.getLogoUrl());
    }

    @GetMapping("/{id}")
    public Optional<InfoEmpresa> obtenerInfoEmpresaPorId(@PathVariable Long id) {
        return infoEmpresaRepository.findById(id);
    }
}
