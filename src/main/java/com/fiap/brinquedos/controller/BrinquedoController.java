package com.fiap.brinquedos.controller;

import com.fiap.brinquedos.entity.Brinquedo;
import com.fiap.brinquedos.repository.BrinquedoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/brinquedos")
@RequiredArgsConstructor
public class BrinquedoController {

    private final BrinquedoRepository brinquedoRepository;

    // GET - Listar todos os brinquedos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Brinquedo>>> getAllBrinquedos() {
        List<Brinquedo> brinquedos = brinquedoRepository.findAll();
        
        List<EntityModel<Brinquedo>> brinquedosModel = brinquedos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Brinquedo>> collectionModel = CollectionModel.of(brinquedosModel);
        collectionModel.add(linkTo(methodOn(BrinquedoController.class).getAllBrinquedos()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }

    // GET - Buscar brinquedo por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> getBrinquedoById(@PathVariable Long id) {
        Optional<Brinquedo> brinquedo = brinquedoRepository.findById(id);
        
        if (brinquedo.isPresent()) {
            EntityModel<Brinquedo> brinquedoModel = toEntityModel(brinquedo.get());
            return ResponseEntity.ok(brinquedoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Criar novo brinquedo
    @PostMapping
    public ResponseEntity<EntityModel<Brinquedo>> createBrinquedo(@RequestBody Brinquedo brinquedo) {
        Brinquedo savedBrinquedo = brinquedoRepository.save(brinquedo);
        EntityModel<Brinquedo> brinquedoModel = toEntityModel(savedBrinquedo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(brinquedoModel);
    }

    // PUT - Atualizar brinquedo completo
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> updateBrinquedo(@PathVariable Long id, @RequestBody Brinquedo brinquedoDetails) {
        Optional<Brinquedo> optionalBrinquedo = brinquedoRepository.findById(id);
        
        if (optionalBrinquedo.isPresent()) {
            Brinquedo brinquedo = optionalBrinquedo.get();
            brinquedo.setNome(brinquedoDetails.getNome());
            brinquedo.setTipo(brinquedoDetails.getTipo());
            brinquedo.setClassificacao(brinquedoDetails.getClassificacao());
            brinquedo.setTamanho(brinquedoDetails.getTamanho());
            brinquedo.setPreco(brinquedoDetails.getPreco());
            
            Brinquedo updatedBrinquedo = brinquedoRepository.save(brinquedo);
            EntityModel<Brinquedo> brinquedoModel = toEntityModel(updatedBrinquedo);
            
            return ResponseEntity.ok(brinquedoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH - Atualizar brinquedo parcialmente
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> patchBrinquedo(@PathVariable Long id, @RequestBody Brinquedo brinquedoDetails) {
        Optional<Brinquedo> optionalBrinquedo = brinquedoRepository.findById(id);
        
        if (optionalBrinquedo.isPresent()) {
            Brinquedo brinquedo = optionalBrinquedo.get();
            
            if (brinquedoDetails.getNome() != null) {
                brinquedo.setNome(brinquedoDetails.getNome());
            }
            if (brinquedoDetails.getTipo() != null) {
                brinquedo.setTipo(brinquedoDetails.getTipo());
            }
            if (brinquedoDetails.getClassificacao() != null) {
                brinquedo.setClassificacao(brinquedoDetails.getClassificacao());
            }
            if (brinquedoDetails.getTamanho() != null) {
                brinquedo.setTamanho(brinquedoDetails.getTamanho());
            }
            if (brinquedoDetails.getPreco() != null) {
                brinquedo.setPreco(brinquedoDetails.getPreco());
            }
            
            Brinquedo updatedBrinquedo = brinquedoRepository.save(brinquedo);
            EntityModel<Brinquedo> brinquedoModel = toEntityModel(updatedBrinquedo);
            
            return ResponseEntity.ok(brinquedoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Deletar brinquedo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrinquedo(@PathVariable Long id) {
        if (brinquedoRepository.existsById(id)) {
            brinquedoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Método auxiliar para criar EntityModel com HATEOAS
    private EntityModel<Brinquedo> toEntityModel(Brinquedo brinquedo) {
        return EntityModel.of(brinquedo)
                .add(linkTo(methodOn(BrinquedoController.class).getBrinquedoById(brinquedo.getId())).withSelfRel())
                .add(linkTo(methodOn(BrinquedoController.class).getAllBrinquedos()).withRel("brinquedos"))
                .add(linkTo(methodOn(BrinquedoController.class).updateBrinquedo(brinquedo.getId(), null)).withRel("update"))
                .add(linkTo(methodOn(BrinquedoController.class).deleteBrinquedo(brinquedo.getId())).withRel("delete"));
    }
}

