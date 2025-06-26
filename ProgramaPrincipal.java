public class ProgramaPrincipal {

    private Scanner ler;
    private static final int CREATE = 1;
    private static final int READ = 2;
    private static final int UPDATE = 3;
    private static final int DELETE = 4;
    private static final int FIND = 5;
    private static final int LEAVE = 9;
    private GerenciarUsuario gerUsuario;

    public ProgramaPrincipal() {
        ler = new Scanner(System.in);
        gerUsuario = new GerenciarUsuario();
    }

    public static void main(String[] args) {
        new ProgramaPrincipal().executar();
    }

    public void executar(){
        int opcao = 0;
        
        do {
            mostrarMenu();
            opcao = ler.nextInt();
            
            if(opcao == CREATE){
                cadastrar();
            } 
            else if(opcao == READ){
                listar();
            }
            else if(opcao == UPDATE){
                editar();
            }
            else if(opcao == DELETE){
                remover();
            } 
            else if(opcao == FIND){
                procurarPorId();
            }
        } while(opcao != LEAVE);
    }

    public void cadastrar() {
        System.out.println("Qual é o nome do usuario?");
        String nome = ler.next();
        System.out.println("Qual é o email do usuario?");
        String email = ler.next();
        System.out.println("Qual é a data de nascimento do usuario? dd/MM/yyyy");
        String dtNascimento = ler.next();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNasc = LocalDate.parse(dtNascimento, dtf);

        Usuario user = new Usuario();

        user.setNome(nome);
        user.setEmail(email);
        user.setDataNasc(dataNasc);

        gerUsuario.create(user);
        System.out.println("Usuario Cadastrado com Sucesso!");
    }

    public void editar() {
        System.out.println("Qual id deseja editar?");
        int procurarId = ler.nextInt();

        Usuario usuario = gerUsuario.findById(procurarId);

        System.out.println("Qual é o novo nome do usuario?");
        String novoNome = ler.next();
        System.out.println("Qual é o novo email do usuario?");
        String novoEmail = ler.next();

        Usuario user = new Usuario();

        usuario.setNome(novoNome);
        usuario.setEmail(novoEmail);

        gerUsuario.update(user);
        System.out.println("Usuario Atualizado com Sucesso!");
    }

    public void listar(){
        List<Usuario> lista = gerUsuario.list();
        for(Usuario usuario : lista){
            System.out.println("\nId: "+ usuario.getId());
            System.out.println("Nome: "+ usuario.getNome());
            System.out.println();
        }
    }

    public void remover() {
        System.out.println("Qual id deseja excluir?");
        int id = ler.nextInt();

        Usuario usuario = gerUsuario.findById(id);

        if (usuario == null) {
            System.out.println("Digite um id válido!");
        } else {
            System.out.println("\nId: " + usuario.getId());
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Email: " + usuario.getEmail());

            System.out.println("Confirma Exclusão? sim - 1 | nao - 2");
            int opcao = ler.nextInt();

            if (opcao == 1) {
                gerUsuario.remove(id);
                System.out.println("Usuario Removido Com sucesso\n");
            } else if (opcao == 2) {
                System.out.println("Cancelar\n");
            } else {
                System.out.println("Digite um valor válido!");
            }
        }
    }

    public void procurarPorId() {
        //não sei se ele vai cobrar
    }

    public void mostrarMenu() {

        System.out.println("CADASTRAR - 1");
        System.out.println("LISTAR - 2");
        System.out.println("EDITAR - 3");
        System.out.println("REMOVER - 4");
        System.out.println("PROCURAR POR ID - 5");
        System.out.println("SAIR - 9");
    }
}
