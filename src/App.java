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

    private int tamanhoDaTurma;

    private Integer[][] preferenciasTurmaA;
    private Integer[][] preferenciasTurmaB;

    private GeneticAlgorithmStudents algoritmoEStudantes;

    public App(String arquivo) {
        readSource(arquivo);

        algoritmoEStudantes = new GeneticAlgorithmStudents(tamanhoDaTurma, preferenciasTurmaA, preferenciasTurmaB);
        //apagar depois
        algoritmoEStudantes.runGenerations();
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
        algoritmoEStudantes.runGenerations();
    }

    private void mostraApenasResultadoFinal(){

    }

    /*
    public static void populaTurmaaleatoria(){
        preferenciasTurmaA [0][0] = 0; preferenciasTurmaA [0][1] = 3; preferenciasTurmaA [0][2] = 2; preferenciasTurmaA [0][3] = 1;
        preferenciasTurmaA [1][0] = 2; preferenciasTurmaA [1][1] = 1; preferenciasTurmaA [1][2] = 3; preferenciasTurmaA [1][3] = 0;
        preferenciasTurmaA [2][0] = 2; preferenciasTurmaA [2][1] = 3; preferenciasTurmaA [2][2] = 0; preferenciasTurmaA [2][3] = 1;
        preferenciasTurmaA [3][0] = 3; preferenciasTurmaA [3][1] = 1; preferenciasTurmaA [3][2] = 0; preferenciasTurmaA [3][3] = 2;

        preferenciasTurmaB [0][0] = 0; preferenciasTurmaB [0][1] = 1; preferenciasTurmaB [0][2] = 2; preferenciasTurmaB [0][3] = 3;
        preferenciasTurmaB [1][0] = 0; preferenciasTurmaB [1][1] = 2; preferenciasTurmaB [1][2] = 3; preferenciasTurmaB [1][3] = 1;
        preferenciasTurmaB [2][0] = 0; preferenciasTurmaB [2][1] = 3; preferenciasTurmaB [2][2] = 2; preferenciasTurmaB [2][3] = 1;
        preferenciasTurmaB [3][0] = 0; preferenciasTurmaB [3][1] = 3; preferenciasTurmaB [3][2] = 1; preferenciasTurmaB [3][3] = 2;
    }
     */

    /**
     * Reads the SOURCE file and populates de students preferences
     *
     * @param source                    the SOURCE file
     * @exception NoSuchFileException   on file not found error
     * @exception IOException           on any other error
     */
    private void readSource(String source) {
        Path path1 = Paths.get(source);

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;
            int nextA = 0;
            int previousA = -1;
            int nextB = 0;
            int previousB = -1;
            int nextSpace = 0;
            String strPreferenciaAlunoA;
            String strPreferenciaAlunoB;
            Integer preferenciaAlunoA;
            Integer preferenciaAlunoB;

            tamanhoDaTurma = Integer.parseInt(reader.readLine().trim());

            //inicializa atributos
            preferenciasTurmaA = new Integer[tamanhoDaTurma][tamanhoDaTurma];
            preferenciasTurmaB = new Integer[tamanhoDaTurma][tamanhoDaTurma];

            // Popula array de preferências da turma B
            System.out.println("Preferencia A");
            for (int i=0; i<tamanhoDaTurma; i++){
                line = reader.readLine();
                //System.out.println(line);
                previousB=-1;
                for (int j=0; j<tamanhoDaTurma; j++) {
                    nextB = line.indexOf("B", previousB + 1);
                    nextSpace = line.indexOf(" ", nextB);
                    //System.out.println("Índice do primeiro B: " + previousB + " Proximo B: " + nextB + " Proximo espaço: " + nextSpace);
                    strPreferenciaAlunoA = line.substring(nextB+1, nextSpace);
                    preferenciaAlunoA = Integer.parseInt(strPreferenciaAlunoA);
                    //System.out.println( "Preferencia aluno A: " + preferenciaAlunoA + " Prefernecias tamanho:" + preferenciasTurmaA.length);

                    // Vai -1 aqui, pois a função de aptidão precisa disso, já que compara posição com o número fornecido e o primeiro número é o 0
                    preferenciasTurmaA[i][j] = preferenciaAlunoA-1;
                    previousB = nextB;
                }
            }
            printMatriz(preferenciasTurmaA);

            // Popula array de preferências da turma B
            System.out.println("Preferencia B");
            for (int i=0; i<tamanhoDaTurma; i++){
                line = reader.readLine();
                //System.out.println(line);
                previousA=-1;
                for (int j=0; j<tamanhoDaTurma; j++) {
                    nextA = line.indexOf("A", previousA + 1);
                    nextSpace = line.indexOf(" ", nextA);
                    //System.out.println("Índice do primeiro A: " + previousA + " Proximo A: " + nextA + " Proximo espaço: " + nextSpace);
                    strPreferenciaAlunoB = line.substring(nextA+1, nextSpace);
                    preferenciaAlunoB = Integer.parseInt(strPreferenciaAlunoB);
                    //System.out.println( "Preferencia aluno B: " + preferenciaAlunoB + " Prefernecias tamanho:" + preferenciasTurmaB.length);

                    // Vai -1 aqui, pois a função de aptidão precisa disso, já que compara posição com o número fornecido e o primeiro número é o 0
                    preferenciasTurmaB[i][j] = preferenciaAlunoB-1;
                    previousA = nextA;
                }
            }
            printMatriz(preferenciasTurmaB);


        } catch (NoSuchFileException x) {
            System.err.format("SOURCE File not found.\n", x);
        } catch (IOException x) {
            System.err.format("I/O Error %s%n\n", x);
        }
    }

    public void printMatriz(Integer [][] matriz) {
        System.out.println("======= Matriz =======");
        for (int i = 0; i < tamanhoDaTurma; i++) {
            System.out.print("Aluno " + i + ": ");
            for (int j=0; j < tamanhoDaTurma; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

}