
package com.example.GesPeSpring.Tablas.InfoEmpresa;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoEmpresaService {
    
    @Autowired
    private InfoEmpresaRepository infoEmpresaRepository;
    
    public Optional<InfoEmpresa> obtenerInfoEmpresa() {
        return infoEmpresaRepository.findById(Long.valueOf(1));
    }
}
