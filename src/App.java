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
        algoritmo2 = new GeneticAlgorithmStudents();

        //apagar depois
        algoritmo2.runGenerations();

        readFile();
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

    private void readFile(){

    }

    private void populaLista(){
        //System.out.println("\nForam adicionados " + " na turma A e x alunos na turma B.");
    }
}