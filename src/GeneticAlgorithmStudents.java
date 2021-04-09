import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GeneticAlgorithmStudents {

    private static int quantidadeDeCromossomos;
    private static int tamanhoDaTurma;

    private static double taxaDeGenesQueSofreraoCrossover;
    private static double taxaDeIndviduosDaPopulacaoQueSofreraoCrossover;

    private static double porcentagemDeCromossomosQueVaiSofrerMutacao;
    private static double porcentagemDeGenesQueVaoSofrerMutacao;

    private static Integer[][] preferenciasTurmaA;
    private static Integer[][] preferenciasTurmaB;

    private static Integer[][] populacao;
    private static Integer[][] intermediaria;

    private static int geracoesParaRodar;
    private static int pararAposXGeracoesRepetindoResultados;

    private static int nivelDeVerbosidade;

    private static CrossoverOBX crossoverOBX;


    //
    public GeneticAlgorithmStudents(int tamanhoDaTurmaRecebido, Integer [][] preferenciasTurmaArecebido, Integer [][] preferenciasTurmaBrecebido,
                                    int quantidadeDeCromossomosRecebida, double taxaDeIndviduosDaPopulacaoQueSofreraoCrossoverRecebido,
                                    double taxaDeGenesQueSofreraoCrossoverRecebida, double porcentagemDeCromossomosQueVaiSofrerMutacaoRecebida,
                                    double porcentagemDeGenesQueVaoSofrerMutacaoRecebida, int geracoesParaRodarRecebida,
                                    int pararAposXGeracoesRepetindoResultadosRecebido, int nivelDeVerbosidaderecebido){

        quantidadeDeCromossomos = quantidadeDeCromossomosRecebida;
        tamanhoDaTurma = tamanhoDaTurmaRecebido;

        // Inicializando variáveis do crossover
        taxaDeIndviduosDaPopulacaoQueSofreraoCrossover = taxaDeIndviduosDaPopulacaoQueSofreraoCrossoverRecebido;
        taxaDeGenesQueSofreraoCrossover = taxaDeGenesQueSofreraoCrossoverRecebida;

        // Inicializando variáveis da mutação
        porcentagemDeCromossomosQueVaiSofrerMutacao = porcentagemDeCromossomosQueVaiSofrerMutacaoRecebida;
        porcentagemDeGenesQueVaoSofrerMutacao = porcentagemDeGenesQueVaoSofrerMutacaoRecebida;

        geracoesParaRodar = geracoesParaRodarRecebida;
        nivelDeVerbosidade = nivelDeVerbosidaderecebido;
        pararAposXGeracoesRepetindoResultados = pararAposXGeracoesRepetindoResultadosRecebido;

        preferenciasTurmaA = preferenciasTurmaArecebido;
        preferenciasTurmaB = preferenciasTurmaBrecebido;

        // Aloca o espaço das populações
        populacao = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        intermediaria = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];

        // Cria a população inicial
        init();

        // Inicializa o Crossover. Numa futura versão pode-se usar outros tipos de crossover aqui
        crossoverOBX = new CrossoverOBX(taxaDeIndviduosDaPopulacaoQueSofreraoCrossover, taxaDeGenesQueSofreraoCrossover, populacao);

        //primos();
    }

    public static void runGenerations() {
        Random rand = new Random();
        int melhor;
        int melhorGeracaoAnterior = -1;
        int contadorDeRepeticoesDeResultados = 0;

        // Roda as gerações
        for (int g=0; g<geracoesParaRodar; g++){
            System.out.println("\n\n===================== Geração: " + g + " =====================");
            calculaAptidao();

            // Mostra a população
            //System.out.println("Matriz após aptidão calculada");
            //printMatriz();

            // Calcula qual o melhor cromossomo
            melhor = getBest();
            System.out.print("Pelo método de Elitismo, o melhor cromossomo encontrado é o " + melhor + " - ");
            printCromossomoComAptidao(melhor);
            //printMatriz();

            // Testa se o melhor resultado está se repetindo
            if (melhor==melhorGeracaoAnterior){
                contadorDeRepeticoesDeResultados = contadorDeRepeticoesDeResultados +1;
                //System.out.println("contador de repetiçoes = " + contadorDeRepeticoesDeResultados);
                if (contadorDeRepeticoesDeResultados==pararAposXGeracoesRepetindoResultados){
                    paradaPorRepeticao(melhor, g);
                    break;
                }
            }
            else{
                contadorDeRepeticoesDeResultados=0;
                //System.out.println("Zerou. contador de repetiçoes = " + contadorDeRepeticoesDeResultados);
            }

            melhorGeracaoAnterior=melhor;

            // Testa se achou solução ideal
            if(populacao[melhor][tamanhoDaTurma]==0) {
                achouSolucao(melhor, g);
                break;
            }


            //Integer [] cromossomoBuscado;
            //printMatriz();
            System.out.println("\nantes do crossover");
            System.out.print("melhor: ");
            System.out.print("\ncromossomo 0 da intermediaria: ");
            for (int i=0; i<intermediaria[0].length; i++){
                System.out.print(intermediaria[0][i] + " ");
            }

            Integer [] cromosssomoZeroInt = new Integer[intermediaria[0].length];
            for (int z=0; z< intermediaria[0].length; z++){
                cromosssomoZeroInt[z]=intermediaria[0][z];
            }


            // Faz o crossover
            intermediaria = crossoverOBX.crossover();

            for (int z=0; z<intermediaria[0].length; z++){
                intermediaria[0][z] = cromosssomoZeroInt[z];
            }


            System.out.println("\ndepois do croosover");
            System.out.print("melhor: ");
            System.out.print("\ncromossomo 0 da intermediaria após o croosover: ");
            for (int i=0; i<intermediaria[0].length; i++){
                System.out.print(intermediaria[0][i] + " ");
            }


            for (int p=0; p<populacao.length; p++){
                for (int q=0; q<populacao[0].length; q++){
                    populacao[p][q]=intermediaria[p][q];
                }
            }

            //populacao = intermediaria;
/*
            System.out.print("\ncromossomo 0 da população após intermediaria virar pop: ");
            for (int i=0; i<populacao[0].length; i++){
                System.out.print(populacao[0][i] + " ");
            }

 */
            System.out.print("\nCromossomo 0 da populaçao antes da mutação: ");
            for (int k=0; k<populacao[0].length; k++){
                System.out.print(populacao[0][k] + " ");
            }

            Integer [] cromossomoZero = new Integer[populacao[0].length];
            for (int z=0; z<cromossomoZero.length; z++){
                cromossomoZero[z]=populacao[0][z];
            }

            // Faz a mutação
            mutacaoComBug();

            populacao[0] = cromossomoZero;
            System.out.print("\nCromossomo 0 da populaçao após a mutação: ");
            for (int k=0; k<populacao[0].length; k++){
                System.out.print(populacao[0][k] + " ");
            }
            /*
            System.out.println("\ndepois da mutação");
            System.out.print("melhor: ");
            printCromossomoComAptidao(melhor);

            cromossomoBuscado = populacao[0];
            System.out.print("\ncromossomo 0 da população: ");
            for (int i=0; i<populacao[0].length; i++){
                System.out.print(populacao[0][i] + " ");
            }

            cromossomoBuscado = intermediaria[0];
            System.out.print("\ncromossomo 0 da intermediaria: ");
            for (int i=0; i<intermediaria[0].length; i++){
                System.out.print(intermediaria[0][i] + " ");
            }
             */
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
            populacao[i][tamanhoDaTurma]= aptidao(i);
            //System.out.println("Cromossomo " + i + "= " + Arrays.toString(populacao[i]));
        }
    }


    public static void printCromossomoComAptidao(int cromossomo){
        System.out.print("[");
        for (int i=0; i<populacao[cromossomo].length-1; i++){
            if (i==populacao[cromossomo].length-2){
                System.out.print(populacao[cromossomo][i]+"]");
                break;
            }
            System.out.print(populacao[cromossomo][i] + ", ");
        }
        System.out.print(" -- Aptidão: " + populacao[cromossomo][populacao[cromossomo].length-1]);
    }


    public static void printCromossomoSemAptidao(int cromossomo){
        System.out.print("[");
        for (int i=0; i<populacao[cromossomo].length-1; i++){
            if (i==populacao[cromossomo].length-2){
                System.out.print(populacao[cromossomo][i]+"]");
                break;
            }
            System.out.print(populacao[cromossomo][i] + ", ");
        }
    }


    public static void printMatriz() {
        int j = 0;
        System.out.println(" ");
        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            System.out.print("Cromossomo " + i + " - ");
            for (j=0; j < tamanhoDaTurma; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println("Aptidão: " + populacao[i][j]);
        }
    }

    public static void calculaAptidao(){
        for(int i = 0; i < quantidadeDeCromossomos; i++){
            populacao[i][tamanhoDaTurma] = aptidao(i);
        }
    }

    // A aptidão e calculada pela posição em que o aluno ocupa na lista de preferência. Quanto mais preferido, menor a aptidão.
    // Logo, quanto menor a aptidão, melhor a afinidade entre os alunos.
    public static int aptidao(int x){
        int soma = 0;
        int posicaoDePreferenciaDoAluno;
        int valorDaPreferencia;

        /*
        System.out.print("Cromossomo sendo analisado " + x + " = [");
        for (int i=0; i<populacao[x].length-1; i++){
            if (i==populacao[x].length-2){
                System.out.print(populacao[x][i]+"]");
                break;
            }
            System.out.print(populacao[x][i] + ", ");
        }
         */

        for(int i = 0; i < tamanhoDaTurma; i++){
            //Vê as posições de preferências nas turma A
            posicaoDePreferenciaDoAluno = indexOf(populacao[x][i], preferenciasTurmaA[i]);
            //System.out.println("posição da preferencia do aluno " + populacao[x][i] + " da turma B para o aluno A" + i + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = posicaoDePreferenciaDoAluno;
            soma = soma + valorDaPreferencia;

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[populacao[x][i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = posicaoDePreferenciaDoAluno;
            soma = soma + valorDaPreferencia;

        }
        //System.out.println(" -- Aptidão: " + soma);
        return soma;
    }

    public static int indexOf(int elemento, Integer [] vetor){
        for (int i=0; i<vetor.length; i++)
        {
            if (vetor[i] == elemento ) {
                return i;
            }
        }
        return -1;
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

        // Coloca o melhor na população intermediária
        //System.out.print("\ncolocando na intermediaria 0 o melhor: ");
        for(int i = 0; i < tamanhoDaTurma; i++) {
            intermediaria[0][i] = populacao[linha][i];
            //System.out.print(intermediaria[0][i] + " ");
        }
        //System.out.println("");

        return linha;
    }

    public static void mutacao(){
        Random rand = new Random();
        int quantidadeDeCromossomosqueVaiSofrerMutacao = (int) (Math.ceil(populacao.length * porcentagemDeCromossomosQueVaiSofrerMutacao));
        //int quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao = (int) (Math.ceil(populacao[0].length * porcentagemDeGenesQueVaoSofrerMutacao));
        //System.out.println("Quantidade de cromossomos que vai sofrer mutação é: " + quantidadeDeCromossomosqueVaiSofrerMutacao);

        //nova parte
        int cromossomoEscolhidoParaSofrerMutacao = ((rand.nextInt(quantidadeDeCromossomos-1)))+1;
        int posicao1 = rand.nextInt(tamanhoDaTurma);
        int posicao2 = rand.nextInt(tamanhoDaTurma);
        while (posicao1 == posicao2) {
            posicao2 = rand.nextInt(tamanhoDaTurma);
        }
        int conteudoPosicao1 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1];
        int conteudoPosicao2 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2];
        int conteudoIntermediario = conteudoPosicao1;
        conteudoPosicao1 = conteudoPosicao2;
        conteudoPosicao2 = conteudoIntermediario;
        System.out.println("\nCromossomo escolhido: " + cromossomoEscolhidoParaSofrerMutacao);
        System.out.print("\nCromossomo 0 da populaçao antes do conteúdo 1: ");
        for (int k=0; k<populacao[0].length; k++){
            System.out.print(populacao[0][k] + " ");
        }
        System.out.print("\nCromossomo " + cromossomoEscolhidoParaSofrerMutacao + " da populaçao antes do conteúdo 1: ");
        for (int k=0; k<populacao[cromossomoEscolhidoParaSofrerMutacao].length; k++){
            System.out.print(populacao[cromossomoEscolhidoParaSofrerMutacao][k] + " ");
        }
        populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1] = conteudoPosicao1;
        System.out.print("\nCromossomo 0 da populaçao antes do conteúdo 2: ");
        for (int k=0; k<populacao[0].length; k++){
            System.out.print(populacao[0][k] + " ");
        }
        System.out.print("\nCromossomo " + cromossomoEscolhidoParaSofrerMutacao + " da populaçao antes do conteúdo 2: ");
        for (int k=0; k<populacao[cromossomoEscolhidoParaSofrerMutacao].length; k++){
            System.out.print(populacao[cromossomoEscolhidoParaSofrerMutacao][k] + " ");
        }
        populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2] = conteudoPosicao2;
        System.out.print("\nCromossomo 0 da populaçao após conteúdo 2: ");
        for (int k=0; k<populacao[0].length; k++){
            System.out.print(populacao[0][k] + " ");
        }
        System.out.print("\nCromossomo " + cromossomoEscolhidoParaSofrerMutacao + " da populaçao após conteúdo 2: ");
        for (int k=0; k<populacao[cromossomoEscolhidoParaSofrerMutacao].length; k++){
            System.out.print(populacao[cromossomoEscolhidoParaSofrerMutacao][k] + " ");
        }

        /*
        for(int i = 0; i<quantidadeDeCromossomosqueVaiSofrerMutacao; i++){
            int cromossomoEscolhidoParaSofrerMutacao = ((rand.nextInt(quantidadeDeCromossomos-1)))+1;
            //System.out.println("\nDepois do while, o cromosso escolhido para mutação foi o " + cromossomoEscolhidoParaSofrerMutacao);
            // Ainda dá pra evitar que o mesmo cromossomo seja escolhido duas vezes. Update futuro.
            // Escolhe as posições que sofrerão troca
            int posicao1 = rand.nextInt(tamanhoDaTurma);
            int posicao2 = rand.nextInt(tamanhoDaTurma);
            // Evita escolher duas vezes a mesma posição
            while (posicao1 == posicao2) {
                posicao2 = rand.nextInt(tamanhoDaTurma);
            }
            // Faz a mutação


            int conteudoPosicao1 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1];
            int conteudoPosicao2 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2];
            int conteudoIntermediario = conteudoPosicao1;
            conteudoPosicao1 = conteudoPosicao2;
            conteudoPosicao2 = conteudoIntermediario;
            populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1] = conteudoPosicao1;
            populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2] = conteudoPosicao2;

        }

         */
    }

    public static void mutacaoComBug(){
        Random rand = new Random();
        int quantidadeDeCromossomosqueVaiSofrerMutacao = (int) (Math.ceil(populacao.length * porcentagemDeCromossomosQueVaiSofrerMutacao));
        int quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao = (int) (Math.ceil(populacao[0].length * porcentagemDeGenesQueVaoSofrerMutacao));

        for(int i = 0; i<quantidadeDeCromossomosqueVaiSofrerMutacao; i++){
            //System.out.println("\nQuantidade de cromossomos que vai sofrer mutação é: " + quantidadeDeCromossomosqueVaiSofrerMutacao);
            //System.out.println("Quantidade de genes de um cromossomo que vai sofrer mutação é: " + quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao);

            int cromossomoEscolhidoParaSofrerMutacao = (rand.nextInt(quantidadeDeCromossomos));
            // Evita que o elite sofra mutação
            while (cromossomoEscolhidoParaSofrerMutacao==0){
                cromossomoEscolhidoParaSofrerMutacao = rand.nextInt(quantidadeDeCromossomos);
            }
            //System.out.println("Cromossomo escocolhido para mutação: " + cromossomoEscolhidoParaSofrerMutacao);
            // Ainda dá pra evitar que o mesmo cromossomo seja escolhido duas vezes. Update futuro.


            // Faz mutação em n genes no cromossomo escolhido
            for (int j=0; j<quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao; j++) {
                // Escolhe as posições que sofrerão troca
                int posicao1 = rand.nextInt(tamanhoDaTurma);
                int posicao2 = rand.nextInt(tamanhoDaTurma);
                // Evita escolher duas vezes a mesma posição
                while (posicao1 == posicao2) {
                    posicao2 = rand.nextInt(tamanhoDaTurma);
                }
                //System.out.println("Posições escolhidas para mutação: " + posicao1 + " e " + posicao2);

                // Faz a mutação
                int conteudoPosicao1 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1];
                int conteudoPosicao2 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2];
                int conteudoIntermediario = conteudoPosicao1;
                conteudoPosicao1 = conteudoPosicao2;
                conteudoPosicao2 = conteudoIntermediario;

                populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1] = conteudoPosicao1;
                populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2] = conteudoPosicao2;

            //System.out.print("\nCromossomo " + cromossomoEscolhidoParaSofrerMutacao + " após a mutação: ");
            //printCromossomoComAptidao(cromossomoEscolhidoParaSofrerMutacao);
            }
        }
    }

    public static void achouSolucao(int melhor, int geracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("Achou a solução ótima na geração " + geracao + ". Ela corresponde ao cromossomo: "+ melhor);
        printSolucaoDecodificada(melhor);
    }

    public static void paradaPorRepeticao(int melhor, int geracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("Parou a execução na geração " + geracao + ", pois ficou repetindo a mesma solução. Essa solução corresponde ao cromossomo: "+ melhor);
        printSolucaoDecodificada(melhor);
    }

    public static void printSolucaoDecodificada (int melhor){
        for (int i=0; i<populacao[melhor].length-1; i++){
            System.out.println("Aluno A" + (i+1) + " com Aluno B" + (populacao[melhor][i]+1));
        }
    }

    public static int fibonacci (int termo){
        int [] memo = new int[termo+1];

        memo[0]=1;
        memo[1]=2;
        memo[2]=3;

        for (int i=2; i<=termo;i++){
            memo[i]=memo[i-1]+memo[i-2];
        }
        return memo[termo];
    }

    public static void primosDiv2 () {
        int [] primos = new int [tamanhoDaTurma];
        int maximo = Integer.MAX_VALUE;
        primos[0]=0;

        int indicePrimos = 1;
        int i =0;
        int num =0;

        for (i = 3; i <= maximo; i++) {
            int counter=0;
            for(num =i; num>=1; num--) {
                if(i%num==0) counter = counter + 1;
            }
            if (counter ==2) {
                primos [indicePrimos] = (int) Math.ceil(i/2);
                indicePrimos++;
                if (indicePrimos==tamanhoDaTurma) break;
            }
        }

        System.out.println("Prime numbers from 1 to 100 are :");
        for (int j=0; j<primos.length; j++){
            System.out.println("primo " + j + ": " + primos[j] + ", ");
        }
    }

    public static void populaTurmaPerfeita(){

    preferenciasTurmaA [0][0] = 0; preferenciasTurmaA [0][1] = 1; preferenciasTurmaA [0][2] = 2; preferenciasTurmaA [0][3] = 3;
    preferenciasTurmaA [1][0] = 1; preferenciasTurmaA [1][1] = 2; preferenciasTurmaA [1][2] = 3; preferenciasTurmaA [1][3] = 0;
    preferenciasTurmaA [2][0] = 2; preferenciasTurmaA [2][1] = 3; preferenciasTurmaA [2][2] = 0; preferenciasTurmaA [2][3] = 1;
    preferenciasTurmaA [3][0] = 3; preferenciasTurmaA [3][1] = 0; preferenciasTurmaA [3][2] = 1; preferenciasTurmaA [3][3] = 2;

    preferenciasTurmaB [0][0] = 0; preferenciasTurmaB [0][1] = 1; preferenciasTurmaB [0][2] = 2; preferenciasTurmaB [0][3] = 3;
    preferenciasTurmaB [1][0] = 1; preferenciasTurmaB [1][1] = 2; preferenciasTurmaB [1][2] = 3; preferenciasTurmaB [1][3] = 0;
    preferenciasTurmaB [2][0] = 2; preferenciasTurmaB [2][1] = 3; preferenciasTurmaB [2][2] = 0; preferenciasTurmaB [2][3] = 1;
    preferenciasTurmaB [3][0] = 3; preferenciasTurmaB [3][1] = 0; preferenciasTurmaB [3][2] = 1; preferenciasTurmaB [3][3] = 2;

    }

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
}


