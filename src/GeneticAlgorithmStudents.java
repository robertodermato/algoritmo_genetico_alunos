import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GeneticAlgorithmStudents {

    private static int quantidadeDeCromossomos;
    private static int tamanhoDaTurma;
    private static int fatorDeOdio;

    private static double taxaDeGenesQueSofreraoCrossover;
    private static double taxaDeInidviduosDaPopulacaoQueSofreraoCrossover;

    private static double porcentagemDeCromossomosQueVaiSofrerMutacao;
    private static double porcentagemDeGenesQueVaoSofrerMutacao;

    private static Integer[][] preferenciasTurmaA;
    private static Integer[][] preferenciasTurmaB;

    private static Integer[][] populacao;
    private static Integer[][] intermediaria;

    private static int geracoesParaRodar;
    private static int pararAposXGeracoesRepetindoResultados;

    private static CrossoverOBX crossoverOBX;

    private static int nivelDeVerbosidade;
    //
    public GeneticAlgorithmStudents(){

        quantidadeDeCromossomos = 11;
        tamanhoDaTurma = 4;
        fatorDeOdio = 1;

        // Inicializando variáveis do crossover
        taxaDeGenesQueSofreraoCrossover = 0.5;
        taxaDeInidviduosDaPopulacaoQueSofreraoCrossover = 1;

        // Inicializando variáveis da mutação
        porcentagemDeCromossomosQueVaiSofrerMutacao = 0.2;
        porcentagemDeGenesQueVaoSofrerMutacao = 0.2;

        geracoesParaRodar = 300;
        nivelDeVerbosidade = 0;
        pararAposXGeracoesRepetindoResultados = 20;

        preferenciasTurmaA = new Integer[tamanhoDaTurma][tamanhoDaTurma];
        preferenciasTurmaB = new Integer[tamanhoDaTurma][tamanhoDaTurma];
        //populaTurmaPerfeita();
        populaTurmaaleatoria();

        // Aloca o espaço das populações
        populacao = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        intermediaria = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];

        // Cria a população inicial
        init();

        // Inicializa o Crossover. Numa futura versão pode-se usar outros tipos de crossover aqui
        crossoverOBX = new CrossoverOBX(taxaDeGenesQueSofreraoCrossover, populacao);
    }

    public static void runGenerations() {
        Random rand = new Random();
        //populacao = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        //intermediaria = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        int melhor;
        int melhorGeracaoAnterior = -1;
        int contadorDeRepeticoesDeResultados = 0;

        // Cria a população inicial
        //init();

        //crossoverOBX = new CrossoverOBX(taxaDeGenesQueSofreraoCrossover, populacao);

        // Roda as gerações
        for (int g=0; g<geracoesParaRodar; g++){
            System.out.println("\n\n===================== Geração: " + g + " =====================");
            calculaAptidao();
            //printMatriz();

            // Calcula qual o melhor cromossomo
            melhor = getBest();
            System.out.print("Pelo método de Elitismo, o melhor cromossomo encontrado é o " + melhor + " - ");
            printCromossomoComAptidao(melhor);

            // Testa se o melhor resultado está se repetindo
            if (melhor==melhorGeracaoAnterior){
                contadorDeRepeticoesDeResultados = contadorDeRepeticoesDeResultados +1;
                //System.out.println("contador de repetiçoes = " + contadorDeRepeticoesDeResultados);
                if (contadorDeRepeticoesDeResultados==pararAposXGeracoesRepetindoResultados){
                    paradaPorRepeticao(melhor);
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
                achouSolucao(melhor);
                break;
            }

            intermediaria = crossoverOBX.crossover();
            populacao = intermediaria;

            mutacao();

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
        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            //System.out.print("C: " + i + " - ");
            for (j=0; j < tamanhoDaTurma; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println("F: " + populacao[i][j]);
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
            valorDaPreferencia = posicaoDePreferenciaDoAluno * fatorDeOdio;
            soma = soma + valorDaPreferencia;

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[populacao[x][i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = posicaoDePreferenciaDoAluno * fatorDeOdio;
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

        for(int i = 0; i < tamanhoDaTurma; i++)
            intermediaria[0][i] = populacao[linha][i];

        return linha;
    }

    public static void mutacao(){
        Random rand = new Random();
        int quantidadeDeCromossomosqueVaiSofrerMutacao = (int) (populacao.length * porcentagemDeCromossomosQueVaiSofrerMutacao);

        for(int i = 0; i<quantidadeDeCromossomosqueVaiSofrerMutacao; i++){
            int quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao = (int) (populacao[0].length * porcentagemDeGenesQueVaoSofrerMutacao);

            System.out.println("\nQuantidade de cromossomos que vai sofrer mutação é: " + quantidadeDeCromossomosqueVaiSofrerMutacao);
            System.out.println("Quantidade de genes de um cromossomo que vai sofrer mutação é: " + quantidadeDeGenesNesseCromossomoQueVaiSofrerMutacao);

            int cromossomoEscolhidoParaSofrerMutacao = rand.nextInt(quantidadeDeCromossomos);
            // Evita que o elite sofra mutação
            while (cromossomoEscolhidoParaSofrerMutacao==0){
                cromossomoEscolhidoParaSofrerMutacao = rand.nextInt(quantidadeDeCromossomos);
            }
            System.out.println("Cromossomo escocolhido para mutação: " + cromossomoEscolhidoParaSofrerMutacao);
            // Ainda dá pra evitar que o mesmo cromossomo seja escolhido duas vezes. Update futuro.

            // Escolhe as posições que sofrerão troca
            int posicao1 = rand.nextInt(tamanhoDaTurma);
            int posicao2 = rand.nextInt(tamanhoDaTurma);
            // Evita escolher duas vezes a mesma posição
            while (posicao1==posicao2){
                posicao2 = rand.nextInt(tamanhoDaTurma);
            }

            System.out.println("Posições escolhidas para mutação: " + posicao1 + " e " + posicao2);
            System.out.print("Cromossomo " + cromossomoEscolhidoParaSofrerMutacao + " antes da mutação: ");
            printCromossomoSemAptidao(cromossomoEscolhidoParaSofrerMutacao);

            // Faz a mutação
            int conteudoPosicao1 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1];
            int conteudoPosicao2 = populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2];
            int conteudoIntermediario = conteudoPosicao1;
            conteudoPosicao1 = conteudoPosicao2;
            conteudoPosicao2 = conteudoIntermediario;
            populacao[cromossomoEscolhidoParaSofrerMutacao][posicao1] = conteudoPosicao1;
            populacao[cromossomoEscolhidoParaSofrerMutacao][posicao2] = conteudoPosicao2;
            System.out.print("\nCromossomo " + cromossomoEscolhidoParaSofrerMutacao + " após a mutação: ");
            printCromossomoSemAptidao(cromossomoEscolhidoParaSofrerMutacao);


        }

    }

    public static void achouSolucao(int melhor){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("Achou a solução ótima. Ela corresponde ao cromossomo: "+ melhor);
        System.out.println("Solução Decodificada: ");
        for (int i=0; i<populacao[melhor].length; i++){
            System.out.print(populacao[melhor][i] + ", ");
        }
    }

    public static void paradaPorRepeticao(int melhor){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("Parou a execução, pois ficou repetindo a mesma solução. Essa solução corresponde ao cromossomo: "+ melhor);
        System.out.println("Solução Decodificada: ");
        for (int i=0; i<populacao[melhor].length; i++){
            System.out.print(populacao[melhor][i] + ", ");
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


