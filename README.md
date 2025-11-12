## 🎵 Mini Biblioteca de Músicas (Java)

Projeto acadêmico desenvolvido em Java para a disciplina Programação 2 (UFRPE).
O sistema permite gerenciar músicas de forma simples, aplicando conceitos de POO (Programação Orientada a Objetos),
persistência em arquivo e controle de acesso por usuário.

## 🧠 Visão Geral

A Mini Biblioteca de Músicas é um aplicativo de linha de comando (CLI) que possibilita:

👤 Cadastro e autenticação de usuários (nome, e-mail, senha)

🎶 Gerenciamento de músicas (adicionar, editar, remover, listar e buscar)

💾 Persistência local automática em arquivo CSV (as músicas permanecem após fechar o programa)

🔐 Política de acesso — apenas usuários logados podem adicionar, editar ou remover músicas

🧱 Estrutura modular com pacotes (model, repository, persistence, app)

## 🏗️ Estrutura do Projeto

```text
src/
├── app/
│   └── Main.java             # CLI e controle de autenticação
├── model/
│   ├── Midia.java            # Superclasse abstrata que representa qualquer tipo de mídia (título, gênero e duração)
│   ├── Musica.java           # Entidade música (UUID + atributos privados)
│   └── Usuario.java          # Entidade usuário
├── repository/
│   ├── BibliotecaMusical.java  # CRUD + integração com persistência
│   └── UsuarioRepository.java  # Cadastro e autenticação de usuários
└── persistence/
    └── FileStorage.java        # Persistência de músicas em arquivo CSV
```

## ⚙️ Tecnologias Utilizadas

| Tecnologia                      | Função                                        |
| ------------------------------- | --------------------------------------------- |
| ☕ **Java 17+**                  | Linguagem principal                           |
| 🧩 **POO**                      | Encapsulamento, construtores, getters/setters |
| 🧠 **ArrayList**                | Armazenamento dinâmico em memória             |
| 💾 **File I/O (java.nio.file)** | Persistência automática das músicas           |
| 🔑 **UUID**                     | Identificador único para cada música          |

## 🔐 Fluxo de Uso

Ao iniciar a aplicação, o usuário deve primeiro se autenticar:

1.  **Registrar** um novo usuário.
2.  **Fazer login** com um usuário existente.

Após o login bem-sucedido, o usuário acessa o menu principal da biblioteca de músicas:

```bash
==== MINI BIBLIOTECA DE MÚSICAS ====
1 - Adicionar música
2 - Editar música
3 - Remover música
4 - Listar todas
5 - Buscar (título / artista / gênero)
0 - Sair
```


## 💾 Persistência de dados

* **Arquivo:**
    * Todas as músicas são armazenadas no arquivo `musicas.csv`

* **Formato:**
    * Cada música corresponde a uma linha no arquivo, com os campos delimitados por `;` (ponto e vírgula). 
    * O sistema lida automaticamente com o "escape" de caracteres especiais (como `\n` e `;`) que possam existir nos dados.

* **Leitura e gravação:**
    * Os dados são carregados do CSV para a memória na inicialização do sistema e são salvos de volta no arquivo após cada modificação (adicionar, editar ou remover), garantindo que as alterações sejam persistidas.

### Todas as músicas são salvas automaticamente em:

~/.mini-bib-musicas/musicas.csv

## 🧩 Herança e Polimorfismo

A estrutura do projeto utiliza os conceitos de Herança e Polimorfismo para organizar os diferentes tipos de mídias.

* **Superclasse Abstrata: `Midia`**
    * Funciona como a base para todas as mídias.
    * Contém atributos genéricos/comuns, como `titulo`, `genero` e `duracaoSegundos`.

* **Subclasse: `Musica`**
    * Herda diretamente de `Midia` (usando `extends`).
    * Adiciona seus próprios atributos específicos, como `artista` e `album`.
    * Sobrescreve o método `descricao()` (com `@Override`) para exibir detalhes específicos de uma música, demonstrando polimorfismo.

### 🔁 Opção 6: Demonstração de Polimorfismo

Esta opção executa um exemplo simples de polimorfismo dinâmico (em tempo de execução).

O sistema realiza os seguintes passos:

1.  Cria uma lista da superclasse (`ArrayList<Midia>`).
2.  Adiciona objetos da subclasse (`Musica`) diretamente nessa lista.
3.  Itera sobre a lista e chama o método de exibição de cada item.

Graças ao polimorfismo, o Java invoca automaticamente o método sobrescrito (override) na classe `Musica`, exibindo os detalhes específicos da música, em vez do método genérico da superclasse `Midia`.

**Exemplo de saída no terminal:**

```bash
-- Demonstração de Polimorfismo --
Música: Thunderstruck - AC/DC (Rock) [292s]
Música: Bohemian Rhapsody - Queen (Rock) [354s]
