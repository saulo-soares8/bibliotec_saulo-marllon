
package br.escola.bibliotec.model;

public enum StatusEmprestimo {

    ATIVO("Ativo"),
    DEVOLVIDO("Devolvido");

    private final String rotulo;

    StatusEmprestimo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
