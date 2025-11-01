package repository;

import model.Musica;
import persistence.FileStorage;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repositório em memória com persistência em arquivo.
 * Carrega ao iniciar; salva após operações de mutação.
 */
public class BibliotecaMusical {
    private final List<Musica> listaMusicas;
    private final FileStorage storage;

    public BibliotecaMusical() {
        this(Path.of(System.getProperty("user.home"), ".mini-bib-musicas", "musicas.csv"));
    }

    public BibliotecaMusical(Path arquivo) {
        this.storage = new FileStorage(arquivo);
        this.listaMusicas = new ArrayList<>(storage.carregar());
    }

    public boolean adicionarMusica(Musica m) {
        if (m == null) return false;
        if (existeDuplicada(m)) return false;
        boolean ok = listaMusicas.add(m);
        if (ok) storage.salvar(listaMusicas);
        return ok;
    }

    public boolean editarMusica(UUID id, String novoTitulo, String novoArtista, String novoAlbum, String novoGenero, Integer novaDuracao) {
        Optional<Musica> opt = buscarPorId(id);
        if (opt.isEmpty()) return false;
        Musica alvo = opt.get();

        if (novoTitulo != null) alvo.setTitulo(novoTitulo);
        if (novoArtista != null) alvo.setArtista(novoArtista);
        if (novoAlbum != null) alvo.setAlbum(novoAlbum);
        if (novoGenero != null) alvo.setGenero(novoGenero);
        if (novaDuracao != null) alvo.setDuracaoSegundos(novaDuracao);

        storage.salvar(listaMusicas);
        return true;
    }

    public boolean removerPorId(UUID id) {
        boolean ok = listaMusicas.removeIf(m -> m.getId().equals(id));
        if (ok) storage.salvar(listaMusicas);
        return ok;
    }

    public Optional<Musica> buscarPorId(UUID id) {
        for (Musica m : listaMusicas) {
            if (m.getId().equals(id)) return Optional.of(m);
        }
        return Optional.empty();
    }

    public List<Musica> buscarPorTitulo(String termo) {
        String t = termo == null ? "" : termo.toLowerCase();
        return listaMusicas.stream()
                .filter(m -> m.getTitulo().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Musica> buscarPorArtista(String termo) {
        String t = termo == null ? "" : termo.toLowerCase();
        return listaMusicas.stream()
                .filter(m -> m.getArtista().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Musica> buscarPorGenero(String termo) {
        String t = termo == null ? "" : termo.toLowerCase();
        return listaMusicas.stream()
                .filter(m -> m.getGenero().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Musica> listarTodas() {
        return Collections.unmodifiableList(listaMusicas);
    }

    public boolean existeDuplicada(Musica nova) {
        return listaMusicas.contains(nova);
    }

    public int tamanho() { return listaMusicas.size(); }
}
