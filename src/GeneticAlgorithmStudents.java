import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GeneticAlgorithmStudents {

    private static int quantidadeDeCromossomos;
    private static int tamanhoDaTurma;

    private static int[][] preferenciasTurmaA;
    private static int[][] preferenciasTurmaB;

    private static Integer[][] populacao;
    private static Integer[][] intermediaria;
    //
    public GeneticAlgorithmStudents(){

        quantidadeDeCromossomos = 20;
        tamanhoDaTurma = 6;
        preferenciasTurmaA = new int[tamanhoDaTurma][3];
        preferenciasTurmaA [0][0] = 0; preferenciasTurmaA [0][1] = 2; preferenciasTurmaA [0][2] = 1;
        preferenciasTurmaA [1][0] = 1; preferenciasTurmaA [1][1] = 0; preferenciasTurmaA [1][2] = 2;
        preferenciasTurmaA [2][0] = 2; preferenciasTurmaA [2][1] = 1; preferenciasTurmaA [2][2] = 0;

        preferenciasTurmaB = new int[tamanhoDaTurma][3];
        preferenciasTurmaB [0][0] = 0; preferenciasTurmaB [0][1] = 2; preferenciasTurmaB [0][2] = 1;
        preferenciasTurmaB [1][0] = 1; preferenciasTurmaB [1][1] = 0; preferenciasTurmaB [1][2] = 2;
        preferenciasTurmaB [2][0] = 2; preferenciasTurmaB [2][1] = 1; preferenciasTurmaB [2][2] = 0;



    }

    public static void runGenerations() {
        Random rand = new Random();
        populacao = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        intermediaria = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        int melhor;

        //cria a população inicial
        init();

        for (int g=0; g<300; g++){
            System.out.println("Geração: " + g);
            calculaAptidao();
            printMatriz();
            melhor = getBest();
            System.out.println( "Metodo Elitismo = " + melhor);
            if(achouSolucao(melhor)) break;
            crossover();
            populacao = intermediaria;
            if(rand.nextInt(5)==0) {
                System.out.println("Mutação");
                mutacao();
            }
        }
    }


    public static void  init() {
        Integer[] cromossomo = new Integer[tamanhoDaTurma];

        // Cria um cromossomo com os números em ordem
        for (int i = 0; i < cromossomo.length; i++) {
            cromossomo[i] = i;
        }

        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            Collections.shuffle(Arrays.asList(cromossomo));
            populacao[i] = cromossomo;
            populacao[i] = Arrays.copyOf(populacao[i], tamanhoDaTurma + 1);
            populacao[i][tamanhoDaTurma]=42;
            System.out.println("Cromossomo " + i + "= " + Arrays.toString(populacao[i]));
        }


    }


    public static void printMatriz() {
        int j = 0;
        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            System.out.print("C: " + i + " - ");
            for (j=0; j < tamanhoDaTurma; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println("F: " + populacao[i][j]);
        }
    }
    //Dicas pro proximo: não da pra segurar shift

    public static int aptidao(int individuo){
        int somaZero = 0;
        int somaUm = 0;
        for(int j = 0; j < tamanhoDaTurma; j++){
            if(populacao[individuo][j] == 0 ){
                //somaZero += cargas[j];
            } else {
                //somaUm += cargas[j];
            }
        }
        return Math.abs(somaZero - somaUm);

    }

    public static void calculaAptidao(){
        for(int i = 0; i < quantidadeDeCromossomos; i++){
            populacao[i][tamanhoDaTurma] = aptidao(i);
        }
    }
    public static int getBest(){
        int min = populacao[0][tamanhoDaTurma];
        int linha = 0;
        for(int i = 1; i < quantidadeDeCromossomos; i++){
            if(populacao[i][tamanhoDaTurma] < min){
                min = populacao[i][tamanhoDaTurma];
                linha = i;
            }
        }

        for(int i = 0; i < tamanhoDaTurma; i++)
            intermediaria[0][i] = populacao[linha][i];

        return linha;
    }

    public static int torneio(){
        Random rand = new Random();
        int individuo1 ,individuo2;

        individuo1 = rand.nextInt(quantidadeDeCromossomos);
        individuo2 = rand.nextInt(quantidadeDeCromossomos);

        if(populacao[individuo1][tamanhoDaTurma] < populacao[individuo2][tamanhoDaTurma])
            return individuo1;
        else
            return individuo2;
    }

    public static void crossover(){

        for (int j = 1; j< quantidadeDeCromossomos; j=j+2){
            int ind1 = torneio();
            int ind2 = torneio();
            for (int k=0; k<tamanhoDaTurma/2; k++){
                intermediaria [j][k]= populacao [ind1][k];
                intermediaria [j+1][k]= populacao [ind2][k];
            }
            for (int k=tamanhoDaTurma/2; k<tamanhoDaTurma; k++){
                intermediaria [j][k]= populacao [ind2][k];
                intermediaria [j+1][k]= populacao [ind1][k];
            }
        }

    }

    public static void mutacao(){
        Random rand = new Random();
        int quant = rand.nextInt(3)+1;
        for(int i = 0; i<quant; i++){
            int individuo = rand.nextInt(quantidadeDeCromossomos);
            int posicao = rand.nextInt(tamanhoDaTurma);

            System.out.println("Cromossomo " + individuo + " sofreu mutação na carga de indice " + posicao);
            if(populacao[individuo][posicao]==0) populacao[individuo][posicao]=1;
            else populacao[individuo][posicao]=0;
        }

    }

    public static boolean achouSolucao(int melhor){
        if(populacao[melhor][tamanhoDaTurma]==0){
            int soma = 0;
            System.out.println("\nAchou a solução ótima. Ela corresponde ao cromossomo :"+ melhor);
            System.out.println("Solução Decodificada: ");
            System.out.println("Pessoa 0: ");
            for(int i=0; i<tamanhoDaTurma; i++)
                if(populacao[melhor][i]==0) {
                    //System.out.print(tamanhoDaTurma[i]+ " ");
                    //soma = soma + cargas[i];
                }
            System.out.println(" - Total: " + soma);

            soma = 0;
            System.out.println("Pessoa 1: ");
            for(int i=0; i<tamanhoDaTurma; i++)
                if(populacao[melhor][i]==1) {
                    //System.out.print(cargas[i]+ " ");
                    //soma = soma + cargas[i];
                }
            System.out.println(" - Total: " + soma);
            return true;
        }
        return false;
    }
}


