
package br.escola.bibliotec.repository;

import br.escola.bibliotec.model.Emprestimo;
import br.escola.bibliotec.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findAllByOrderByDataEmprestimoDescIdDesc();

    long countByLeitorIdAndStatus(Long leitorId, StatusEmprestimo status);
}
