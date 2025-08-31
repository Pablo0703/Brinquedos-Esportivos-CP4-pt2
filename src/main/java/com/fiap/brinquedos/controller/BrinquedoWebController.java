package com.fiap.brinquedos.controller;

import com.fiap.brinquedos.entity.Brinquedo;
import com.fiap.brinquedos.repository.BrinquedoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/brinquedos")
@RequiredArgsConstructor
public class BrinquedoWebController {

    private final BrinquedoRepository repository;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("brinquedos", repository.findAll());
        return "brinquedos/listar";
    }

    // FORMULÁRIO DE CADASTRO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("brinquedo", new Brinquedo());
        return "brinquedos/form";
    }

    // SALVAR (CREATE ou UPDATE)
    @PostMapping
    public String salvar(@ModelAttribute Brinquedo brinquedo) {
        repository.save(brinquedo);
        return "redirect:/web/brinquedos";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Brinquedo brinquedo = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("brinquedo", brinquedo);
        return "brinquedos/form";
    }

    // DELETAR
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/web/brinquedos";
    }
}
