package app;

import model.Musica;
import model.Usuario;
import model.Midia;
import repository.BibliotecaMusical;
import repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static final BibliotecaMusical repo = new BibliotecaMusical();
    private static final UsuarioRepository users = new UsuarioRepository();

    private static Usuario currentUser = null; // sessão atual

    public static void main(String[] args) {
        autenticarOuRegistrar();   // Etapa obrigatória de login
        loopMusicas();             // Menu CRUD de músicas
    }

    // ================== FLUXO DE AUTENTICAÇÃO ==================
    private static void autenticarOuRegistrar() {
        int op;
        do {
            System.out.println("==== Acesso ====");
            System.out.println("1 - Registrar novo usuário");
            System.out.println("2 - Fazer login");
            System.out.println("0 - Sair");
            op = lerInt("Opção: ");

            switch (op) {
                case 1 -> registrar();
                case 2 -> login();
                case 0 -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (currentUser == null);
    }

    private static void registrar() {
        System.out.println("-- Registro --");
        String nome = lerStr("Nome: ");
        String email = lerStr("Email: ").toLowerCase();
        String senha = lerStr("Senha (>=4 caracteres): ");

        try {
            if (users.existePorEmail(email)) {
                System.out.println("Já existe usuário com esse email.");
                return;
            }
            Usuario u = new Usuario(nome, email, senha);
            boolean ok = users.cadastrar(u);
            System.out.println(ok ? "Usuário registrado com sucesso." : "Não foi possível registrar.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private static void login() {
        System.out.println("-- Login --");
        String email = lerStr("Email: ").toLowerCase();
        String senha = lerStr("Senha: ");

        Optional<Usuario> opt = users.autenticar(email, senha);
        if (opt.isPresent()) {
            currentUser = opt.get();
            System.out.println("Bem-vindo, " + currentUser.getNome() + "!");
        } else {
            System.out.println("Credenciais inválidas. Tente novamente.");
        }
    }

    // ================== MENU PRINCIPAL ==================
    private static void loopMusicas() {
        int op;
        do {
            menuMusicas();
            op = lerInt("Opção: ");
            switch (op) {
                case 1 -> adicionar();
                case 2 -> editar();
                case 3 -> remover();
                case 4 -> listar();
                case 5 -> buscar();
                case 6 -> demoPolimorfismo(); // << NOVO
                case 0 -> System.out.println("Saindo... valeu!");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (op != 0);
    }

    private static void menuMusicas() {
        System.out.println("==== MINI BIBLIOTECA DE MÚSICAS ====");
        System.out.println("(Usuário logado: " + currentUser.getEmail() + ")");
        System.out.println("1 - Adicionar música");
        System.out.println("2 - Editar música");
        System.out.println("3 - Remover música");
        System.out.println("4 - Listar todas");
        System.out.println("5 - Buscar (título / artista / gênero)");
        System.out.println("6 - Demonstração de polimorfismo"); // << NOVO
        System.out.println("0 - Sair");
    }

    /**
 * Demonstra a herança/polimorfismo:
 * - Cria uma lista do tipo da SUPERCLASSE (Midia)
 * - Adiciona objetos da SUBCLASSE (Musica)
 * - Chama o método POLIMÓRFICO (descricao()), que foi sobrescrito em Musica.
 */
private static void demoPolimorfismo() {
    System.out.println("-- Demonstração de Polimorfismo --");

    java.util.List<Midia> itens = new java.util.ArrayList<>();
    for (var m : repo.listarTodas()) { // repo.listarTodas() retorna List<Musica>
        itens.add(m); // upcasting implícito: Musica -> Midia
    }

    if (itens.isEmpty()) {
        System.out.println("Não há itens para demonstrar. Cadastre algumas músicas primeiro.");
        return;
    }

    for (Midia midia : itens) {
        // Chamada polimórfica: executa Musica.descricao(), não Midia.descricao()
        System.out.println(midia.descricao());
    }
}

    // ================== CRUD ==================
    private static void exigirLogado() {
        if (currentUser == null) {
            throw new IllegalStateException("Ação restrita. Faça login primeiro.");
        }
    }

    private static void adicionar() {
        try { exigirLogado(); } catch (IllegalStateException e) { System.out.println(e.getMessage()); return; }

        System.out.println("-- Nova música --");
        String titulo = lerStr("Título: ");
        String artista = lerStr("Artista: ");
        String album = lerStr("Álbum: ");
        String genero = lerStr("Gênero: ");
        int duracao = lerInt("Duração (em segundos): ");

        try {
            Musica m = new Musica(titulo, artista, album, genero, duracao);
            boolean ok = repo.adicionarMusica(m);
            if (ok) {
                System.out.println("✅ Música adicionada com sucesso!");
                System.out.println("ID: " + m.getId());
            } else {
                System.out.println("⚠️ Já existe uma música idêntica cadastrada.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private static void editar() {
        try { exigirLogado(); } catch (IllegalStateException e) { System.out.println(e.getMessage()); return; }

        System.out.println("-- Editar música --");
        UUID id = lerUUID("Informe o ID: ");
        var opt = repo.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Música não encontrada.");
            return;
        }

        Musica atual = opt.get();
        System.out.println("Atual: " + atual);
        System.out.println("Deixe em branco para manter o valor atual.");

        String novoTitulo = lerStrOpcional("Novo título: ");
        String novoArtista = lerStrOpcional("Novo artista: ");
        String novoAlbum = lerStrOpcional("Novo álbum: ");
        String novoGenero = lerStrOpcional("Novo gênero: ");
        Integer novaDuracao = lerIntOpcional("Nova duração (segundos): ");

        boolean ok = repo.editarMusica(
                id,
                vazioParaNull(novoTitulo),
                vazioParaNull(novoArtista),
                vazioParaNull(novoAlbum),
                vazioParaNull(novoGenero),
                novaDuracao
        );

        System.out.println(ok ? "Música editada com sucesso." : "Não foi possível editar.");
    }

    private static void remover() {
        try { exigirLogado(); } catch (IllegalStateException e) { System.out.println(e.getMessage()); return; }

        System.out.println("-- Remover música --");
        UUID id = lerUUID("Informe o ID: ");
        boolean ok = repo.removerPorId(id);
        System.out.println(ok ? "Música removida com sucesso." : "Nenhuma música encontrada com esse ID.");
    }

    private static void listar() {
        System.out.println("-- Todas as músicas --");
        List<Musica> lista = repo.listarTodas();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma música cadastrada até o momento.");
            return;
        }

        for (Musica m : lista) {
            System.out.println("ID: " + m.getId());
            System.out.println(m);
            System.out.println("----------------------------");
        }
        System.out.println("Total: " + lista.size());
    }

    private static void buscar() {
        System.out.println("-- Buscar música --");
        System.out.println("1 - Por título");
        System.out.println("2 - Por artista");
        System.out.println("3 - Por gênero");

        int tipo = lerInt("Escolha: ");
        String termo = lerStr("Digite o termo de busca: ");
        List<Musica> resultado = switch (tipo) {
            case 1 -> repo.buscarPorTitulo(termo);
            case 2 -> repo.buscarPorArtista(termo);
            case 3 -> repo.buscarPorGenero(termo);
            default -> List.of();
        };

        if (resultado.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        for (Musica m : resultado) {
            System.out.println("ID: " + m.getId());
            System.out.println(m);
            System.out.println("----------------------------");
        }
        System.out.println("Total encontrado: " + resultado.size());
    }

    // ================== UTILIDADES ==================
    private static String lerStr(String label) {
        System.out.print(label);
        return in.nextLine().trim();
    }

    private static String lerStrOpcional(String label) {
        System.out.print(label);
        String s = in.nextLine();
        return s == null ? "" : s.trim();
    }

    private static String vazioParaNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private static int lerInt(String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.println("Informe um número inteiro válido.");
            }
        }
    }

    private static Integer lerIntOpcional(String label) {
        System.out.print(label);
        String s = in.nextLine().trim();
        if (s.isBlank()) return null;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Valor inválido. Mantendo anterior.");
            return null;
        }
    }

    private static UUID lerUUID(String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            try {
                return UUID.fromString(s);
            } catch (Exception e) {
                System.out.println("Formato de ID inválido. Exemplo: 123e4567-e89b-12d3-a456-426614174000");
            }
        }
    }
}
