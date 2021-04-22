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

    private int quantidadeDeCromossomos;

    private double taxaDeGenesQueSofreraoCrossover;
    private double taxaDeCromossomosQueSofreraoCrossover;

    private double porcentagemDeCromossomosQueVaiSofrerMutacao;
    private double porcentagemDeGenesQueVaoSofrerMutacao;

    private static int geracoesParaRodar;
    private static int pararAposXGeracoesRepetindoResultados;

    private static int nivelDeVerbosidade;

    private static int opcaoDeSequencia;

    // variáveis do simulated annealing
    private double temperatura;
    private double taxaDeResfriamento;
    private int iteracoes;


    // Construtor. Defina as variáveis aqui
    public App(String arquivo, int opcaoDeSequenciaRecebido) {
        readSource(arquivo);

        // para 100: 100 cromossomos, 0.001 de mutação genes e 0,1 mutação cromossomos, 0.2 de crossover, 15mil gerações
        quantidadeDeCromossomos = tamanhoDaTurma;

        //Essa taxa não está sendo usada
        taxaDeCromossomosQueSofreraoCrossover = 0.8;
        // Essa é a que vale e convém ficar entre 0.1 e 0.4 já que são trocas, não adiante ser 0.5, pois isso faria troca de 100% dos genes.
        taxaDeGenesQueSofreraoCrossover = 0.15;

        porcentagemDeCromossomosQueVaiSofrerMutacao = 0.05;
        porcentagemDeGenesQueVaoSofrerMutacao = 0.001;

        geracoesParaRodar = tamanhoDaTurma*1000;
        pararAposXGeracoesRepetindoResultados = tamanhoDaTurma*30;

        opcaoDeSequencia = opcaoDeSequenciaRecebido;

        // variáveis do simulated annealing
        temperatura = 10000;
        taxaDeResfriamento = 0.005;
        iteracoes = tamanhoDaTurma * 1000;

    }

    public void run() {
        int opcao = 1;

        while(opcao!= 0){       // Repetição do menu

            System.out.println(" ");
            System.out.println(" ");
            System.out.println("=================================================");
            System.out.println("Bem vindo ao App de confraternização de turmas");
            System.out.println("=================================================");
            System.out.println(" ");
            System.out.println("Escolha uma das opções: ");

            System.out.println("1 - Ver apenas o resultado final");
            System.out.println("2 - Ver a evolução das gerações");
            System.out.println("3 - Ver a execução passo a passo");
            System.out.println("4 - Ver a execução ultra detalhada");
            System.out.println("5 - Rodar planetas");
            System.out.println("6 - Resolver com Simulated Annealing");
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
                    nivelDeVerbosidade = 0;
                    geraErodaOAlgoritmo();
                    break;

                case 2:
                    nivelDeVerbosidade = 1;
                    geraErodaOAlgoritmo();
                    break;

                case 3:
                    nivelDeVerbosidade = 2;
                    geraErodaOAlgoritmo();
                    break;

                case 4:
                    nivelDeVerbosidade = 3;
                    geraErodaOAlgoritmo();
                    break;

                case 5 :
                    nivelDeVerbosidade = 0;
                    geraErodaPlanetas();
                    break;

                case 6:
                    SimulatedAnnealing algSimulatedAnnealing = new SimulatedAnnealing(preferenciasTurmaA, preferenciasTurmaB, tamanhoDaTurma,
                            temperatura, taxaDeResfriamento, iteracoes);
                    algSimulatedAnnealing.runAlgoritmo();

                case 0:     // Sai do menu, encerra programa
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void geraErodaOAlgoritmo(){
        algoritmoEStudantes = new GeneticAlgorithmStudents(tamanhoDaTurma, preferenciasTurmaA, preferenciasTurmaB,
                quantidadeDeCromossomos, taxaDeCromossomosQueSofreraoCrossover, taxaDeGenesQueSofreraoCrossover,
                porcentagemDeCromossomosQueVaiSofrerMutacao, porcentagemDeGenesQueVaoSofrerMutacao, geracoesParaRodar,
                pararAposXGeracoesRepetindoResultados, nivelDeVerbosidade, opcaoDeSequencia);

        algoritmoEStudantes.runGenerations();
    }

    private void geraErodaPlanetas(){
        algoritmoEStudantes = new GeneticAlgorithmStudents(tamanhoDaTurma, preferenciasTurmaA, preferenciasTurmaB,
                quantidadeDeCromossomos, taxaDeCromossomosQueSofreraoCrossover, taxaDeGenesQueSofreraoCrossover,
                porcentagemDeCromossomosQueVaiSofrerMutacao, porcentagemDeGenesQueVaoSofrerMutacao, geracoesParaRodar,
                pararAposXGeracoesRepetindoResultados, nivelDeVerbosidade, opcaoDeSequencia);

        algoritmoEStudantes.runPlanets();
    }

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
            //System.out.println("Preferencia A");
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
            //printMatriz(preferenciasTurmaA);

            // Popula array de preferências da turma B
            //System.out.println("Preferencia B");
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
            //printMatriz(preferenciasTurmaB);


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