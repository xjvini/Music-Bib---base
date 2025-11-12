package repository;

import model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private final List<Usuario> usuarios = new ArrayList<>();

    public boolean cadastrar(Usuario u) {
        if (u == null) return false;
        if (existePorEmail(u.getEmail())) return false;
        return usuarios.add(u);
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        return usuarios.stream()
                .filter(u -> u.autenticar(email, senha))
                .findFirst();
    }

    public boolean existePorEmail(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios);
    }
}
