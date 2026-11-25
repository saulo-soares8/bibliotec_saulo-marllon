
package br.escola.bibliotec.controller;

import br.escola.bibliotec.model.Leitor;
import br.escola.bibliotec.model.TipoLeitor;
import br.escola.bibliotec.service.LeitorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/leitores")
public class LeitorController {

    private final LeitorService leitorService;

    public LeitorController(LeitorService leitorService) {
        this.leitorService = leitorService;
    }

    @ModelAttribute("tipos")
    public TipoLeitor[] tipos() {
        return TipoLeitor.values();
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("leitores", leitorService.listarTodos());
        return "leitores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("leitor", new Leitor());
        return "leitores/form";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("leitor") Leitor leitor,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (leitor.getNome() != null) {
            leitor.setNome(leitor.getNome().trim());
        }

        if (leitor.getMatricula() != null) {
            leitor.setMatricula(leitor.getMatricula().trim());
        }

        if (leitor.getEmail() != null) {
            leitor.setEmail(leitor.getEmail().trim());
            if (leitor.getEmail().isEmpty()) {
                leitor.setEmail(null);
            }
        }

        if (leitor.getMatricula() != null && !leitor.getMatricula().isBlank()
                && leitorService.matriculaExiste(leitor.getMatricula())) {
            resultado.rejectValue(
                    "matricula",
                    "matricula.duplicada",
                    "Já existe um leitor com esta matrícula"
            );
        }

        if (resultado.hasErrors()) {
            return "leitores/form";
        }

        leitorService.salvar(leitor);
        redirectAttributes.addFlashAttribute("sucesso", "Leitor cadastrado com sucesso!");

        return "redirect:/leitores";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model,
                         RedirectAttributes redirectAttributes) {

        return leitorService.buscarPorId(id)
                .map(leitor -> {
                    model.addAttribute("leitor", leitor);
                    return "leitores/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Leitor não encontrado.");
                    return "redirect:/leitores";
                });
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("leitor") Leitor leitor,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        var existente = leitorService.buscarPorId(id);

        if (existente.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Leitor não encontrado.");
            return "redirect:/leitores";
        }

        leitor.setId(id);

        if (leitor.getNome() != null) {
            leitor.setNome(leitor.getNome().trim());
        }

        if (leitor.getMatricula() != null) {
            leitor.setMatricula(leitor.getMatricula().trim());
        }

        if (leitor.getEmail() != null) {
            leitor.setEmail(leitor.getEmail().trim());
            if (leitor.getEmail().isEmpty()) {
                leitor.setEmail(null);
            }
        }

        if (leitor.getMatricula() != null && !leitor.getMatricula().isBlank()
                && leitorService.matriculaExisteParaOutroLeitor(leitor.getMatricula(), id)) {
            resultado.rejectValue(
                    "matricula",
                    "matricula.duplicada",
                    "Já existe um leitor com esta matrícula"
            );
        }

        if (resultado.hasErrors()) {
            return "leitores/form";
        }

        leitorService.salvar(leitor);
        redirectAttributes.addFlashAttribute("sucesso", "Leitor atualizado com sucesso!");

        return "redirect:/leitores";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (leitorService.buscarPorId(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Leitor não encontrado.");
            return "redirect:/leitores";
        }

        leitorService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Leitor excluído com sucesso!");

        return "redirect:/leitores";
    }
}
