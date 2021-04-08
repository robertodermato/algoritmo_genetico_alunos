import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private final Scanner in = new Scanner(System.in);
    //private GeneticAlgorithm algoritmo1;
    private GeneticAlgorithmStudents algoritmo2;

    // atributos private aqui

    public App() {
        //inicializa atributos
        //algoritmo1 = new GeneticAlgorithm();
        //readSource();
        algoritmo2 = new GeneticAlgorithmStudents();

        //apagar depois
        algoritmo2.runGenerations();


        fillDataInApp();
    }

    public void run() {
        int opcao = 1;

        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("Bem vindo ao App de confraternização de turmas");
        System.out.println("=================================================");

        while(opcao!= 0){       // Repetição do menu

            //System.out.println();
            System.out.println("Escolha uma das opções: ");

            System.out.println("1 - Ver a execução passo a passo");
            System.out.println("2 - Ver apenas o resultado final");
            System.out.println("0 - sair\n");

            try{       // Impede que usuário digite letra
                opcao = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida, tente novamente.");
                in.nextLine();
                continue;
            }


            switch (opcao){
                case 1 :
                    mostraExecucaoPassoPasso();
                    break;

                case 2:
                    mostraApenasResultadoFinal();
                    break;

                case 0:     // Sai do menu, encerra programa
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void mostraExecucaoPassoPasso(){
        algoritmo2.runGenerations();
    }

    private void mostraApenasResultadoFinal(){

    }

    private void fillDataInApp(){
        populaLista();
    }

    /**
     * Reads the SOURCE file and turns it into a single String for easier manipulation
     *
     * @param source                    the SOURCE file
     * @exception NoSuchFileException   on file not found error
     * @exception IOException           on any other error
     * @return                          a String with the content of the SOURCE file
     */
    private String readSource(String source) {
        Path path1 = Paths.get(source);
        String sourceStringyfied ="";

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            while ((line = reader.readLine()) != null) {

                if (!line.isEmpty()) {
                    line = line.trim();
                    sourceStringyfied = sourceStringyfied + line;
                }
            }

            return sourceStringyfied;

        } catch (NoSuchFileException x) {
            System.err.format("SOURCE File not found.\n", x);
        } catch (IOException x) {
            System.err.format("I/O Error %s%n\n", x);
        }
        return null;
    }

    private void populaLista(){
        //System.out.println("\nForam adicionados " + " na turma A e x alunos na turma B.");
    }
}