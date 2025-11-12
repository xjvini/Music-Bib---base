package model;

/**
 * Superclasse genérica para itens da biblioteca.
 */
public abstract class Midia {
    private String titulo;
    private String genero;
    private int duracaoSegundos;

    public Midia(String titulo, String genero, int duracaoSegundos) {
        setTitulo(titulo);
        setGenero(genero);
        setDuracaoSegundos(duracaoSegundos);
    }

    // (atributos privados, com validação) ---
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        this.titulo = titulo.trim();
    }

    public String getGenero() { return genero; }
    public void setGenero(String genero) {
        this.genero = (genero == null ? "" : genero.trim());
    }

    public int getDuracaoSegundos() { return duracaoSegundos; }
    public void setDuracaoSegundos(int duracaoSegundos) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero (em segundos).");
        }
        this.duracaoSegundos = duracaoSegundos;
    }

    /**
     * Método polimórfico: será sobrescrito nas subclasses.
     */
    public String descricao() {
        return "Mídia: " + getTitulo() + " [" + getGenero() + "] - " + getDuracaoSegundos() + "s";
    }
}
