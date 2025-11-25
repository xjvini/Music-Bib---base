package model;
import exception.RegraNegocioException;
import java.util.UUID;

public class Musica extends Midia {
    private final UUID id;
    private String artista;
    private String album;

    // Construtor padrão (gera UUID novo)
    public Musica(String titulo, String artista, String album, String genero, int duracaoSegundos) {
        this(UUID.randomUUID(), titulo, artista, album, genero, duracaoSegundos);
    }

    // Construtor usado pela persistência (mantém o mesmo UUID do arquivo)
    public Musica(UUID id, String titulo, String artista, String album, String genero, int duracaoSegundos) {
        super(titulo, genero, duracaoSegundos); // chama a superclasse Midia
        this.id = (id == null ? UUID.randomUUID() : id);
        setArtista(artista);
        setAlbum(album);
    }

    // --- Getters/Setters específicos de Musica ---
    public UUID getId() { return id; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) {
        if (artista == null || artista.isBlank()) {
            throw new RegraNegocioException("ARTISTA não pode estar vazio.");
        }
        this.artista = artista.trim();
    }

    public String getAlbum() { return album; }
    public void setAlbum(String album) {
        this.album = (album == null ? "" : album.trim());
    }

    // --- Polimorfismo: sobrescrita do método da superclasse ---
    @Override
    public String descricao() {
        // Usa getters herdados de Midia: getTitulo(), getGenero(), getDuracaoSegundos()
        return "Música: " + getTitulo() + " - " + getArtista()
                + " (" + getGenero() + ") [" + getDuracaoSegundos() + "s]";
    }

    public String toLinha() {
        return String.format("%s - %s (%s) [%ds]", getTitulo(), getArtista(), getGenero(), getDuracaoSegundos());
    }

    @Override
    public String toString() { return toLinha(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Musica)) return false;
        Musica outra = (Musica) obj;
        // duplicidade: título + artista + álbum (case-insensitive)
        return getTitulo().equalsIgnoreCase(outra.getTitulo())
            && getArtista().equalsIgnoreCase(outra.getArtista())
            && getAlbum().equalsIgnoreCase(outra.getAlbum());
    }

    @Override
    public int hashCode() {
        return (getTitulo().toLowerCase() + "|" + getArtista().toLowerCase() + "|" + getAlbum().toLowerCase()).hashCode();
    }
}
