
package br.escola.bibliotec.controller.web;

import br.escola.bibliotec.exception.RegraNegocioException;
import br.escola.bibliotec.service.EmprestimoService;
import br.escola.bibliotec.service.LeitorService;
import br.escola.bibliotec.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final LeitorService leitorService;

    public EmprestimoController(
            EmprestimoService emprestimoService,
            LivroService livroService,
            LeitorService leitorService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.leitorService = leitorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        carregarOpcoes(model);
        return "emprestimos/form";
    }

    @PostMapping
    public String emprestar(
            @RequestParam(required = false) Long livroId,
            @RequestParam(required = false) Long leitorId,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            emprestimoService.emprestar(livroId, leitorId);

            redirectAttributes.addFlashAttribute(
                    "mensagem", "Empréstimo registrado com sucesso!");

            return "redirect:/emprestimos";

        } catch (RegraNegocioException e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("livroId", livroId);
            model.addAttribute("leitorId", leitorId);

            carregarOpcoes(model);

            return "emprestimos/form";
        }
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("leitores", leitorService.listarTodos());
    }
}
