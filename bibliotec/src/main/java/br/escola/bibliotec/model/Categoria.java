
package br.escola.bibliotec.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres")
    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    @Column(length = 255)
    private String descricao;

    public Categoria() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
