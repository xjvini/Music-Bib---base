package persistence;

import model.Musica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Persistência simples em arquivo texto (CSV com delimitador ';').
 * Escapes: ';' -> '\;' e '\n' -> '\n' (literal).
 */
public class FileStorage {
    private final Path arquivo;

    public FileStorage(Path arquivo) {
        this.arquivo = arquivo;
    }

    public List<Musica> carregar() {
        List<Musica> lista = new ArrayList<>();
        if (!Files.exists(arquivo)) return lista;
        try {
            List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
            for (String ln : linhas) {
                if (ln == null || ln.isBlank()) continue;
                String[] parts = splitCsvLine(ln);
                if (parts.length < 6) continue;
                UUID id = UUID.fromString(parts[0]);
                String titulo = unescape(parts[1]);
                String artista = unescape(parts[2]);
                String album = unescape(parts[3]);
                String genero = unescape(parts[4]);
                int duracao = Integer.parseInt(parts[5]);
                lista.add(new Musica(id, titulo, artista, album, genero, duracao));
            }
        } catch (Exception e) {
            System.err.println("Falha ao carregar arquivo: " + e.getMessage());
        }
        return lista;
    }

    public void salvar(List<Musica> musicas) {
        try {
            if (!Files.exists(arquivo.getParent())) {
                Files.createDirectories(arquivo.getParent());
            }
            List<String> linhas = new ArrayList<>();
            for (Musica m : musicas) {
                String ln = String.join(";",
                        m.getId().toString(),
                        escape(m.getTitulo()),
                        escape(m.getArtista()),
                        escape(m.getAlbum()),
                        escape(m.getGenero()),
                        String.valueOf(m.getDuracaoSegundos())
                );
                linhas.add(ln);
            }
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Falha ao salvar arquivo: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n");
    }

    private static String unescape(String s) {
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) {
                if (c == 'n') out.append('\n');
                else out.append(c);
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    private static String[] splitCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (esc) {
                cur.append('\\').append(c); // preserva escape para o unescape()
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else if (c == ';') {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }
}
