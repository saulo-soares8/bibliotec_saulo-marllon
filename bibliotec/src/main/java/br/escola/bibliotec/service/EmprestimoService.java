
package br.escola.bibliotec.service;

import br.escola.bibliotec.exception.RegraNegocioException;
import br.escola.bibliotec.model.*;
import br.escola.bibliotec.repository.EmprestimoRepository;
import br.escola.bibliotec.repository.LeitorRepository;
import br.escola.bibliotec.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private static final int LIMITE_ATIVOS = 3;
    private static final int PRAZO_EM_DIAS = 7;

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final LeitorRepository leitorRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            LivroRepository livroRepository,
            LeitorRepository leitorRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.leitorRepository = leitorRepository;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAllByOrderByDataEmprestimoDescIdDesc();
    }

    @Transactional
    public void emprestar(Long livroId, Long leitorId) {

        if (livroId == null) {
            throw new RegraNegocioException("Escolha um livro");
        }

        if (leitorId == null) {
            throw new RegraNegocioException("Escolha um leitor");
        }

        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RegraNegocioException("Livro não encontrado"));

        Leitor leitor = leitorRepository.findById(leitorId)
                .orElseThrow(() -> new RegraNegocioException("Leitor não encontrado"));

        // RN01: verificar se existem exemplares disponíveis
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RegraNegocioException(
                    "Sem exemplares disponíveis: " + livro.getTitulo());
        }

        // RN02: verificar o limite de empréstimos ativos
        long emprestimosAtivos = emprestimoRepository
                .countByLeitorIdAndStatus(leitorId, StatusEmprestimo.ATIVO);

        if (emprestimosAtivos >= LIMITE_ATIVOS) {
            throw new RegraNegocioException(
                    "Limite de 3 empréstimos ativos atingido");
        }

        // Diminuir a quantidade disponível
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);

        // Registrar o empréstimo
        LocalDate hoje = LocalDate.now();

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setLeitor(leitor);
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(hoje.plusDays(PRAZO_EM_DIAS));
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        emprestimoRepository.save(emprestimo);
    }
}
