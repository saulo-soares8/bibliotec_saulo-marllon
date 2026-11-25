
package br.escola.bibliotec.service;

import br.escola.bibliotec.model.Leitor;
import br.escola.bibliotec.repository.LeitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeitorService {

    private final LeitorRepository leitorRepository;

    public LeitorService(LeitorRepository leitorRepository) {
        this.leitorRepository = leitorRepository;
    }

    public List<Leitor> listarTodos() {
        return leitorRepository.findAllByOrderByNomeAsc();
    }

    public Optional<Leitor> buscarPorId(Long id) {
        return leitorRepository.findById(id);
    }

    public boolean matriculaExiste(String matricula) {
        return leitorRepository.findByMatricula(matricula.trim()).isPresent();
    }

    public boolean matriculaExisteParaOutroLeitor(String matricula, Long id) {
        Optional<Leitor> leitor = leitorRepository.findByMatricula(matricula.trim());

        return leitor.isPresent() && !leitor.get().getId().equals(id);
    }

    public Leitor salvar(Leitor leitor) {
        leitor.setNome(leitor.getNome().trim());
        leitor.setMatricula(leitor.getMatricula().trim());

        if (leitor.getEmail() != null) {
            leitor.setEmail(leitor.getEmail().trim());

            if (leitor.getEmail().isEmpty()) {
                leitor.setEmail(null);
            }
        }

        return leitorRepository.save(leitor);
    }

    public void excluir(Long id) {
        leitorRepository.deleteById(id);
    }
}
