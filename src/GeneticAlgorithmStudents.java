import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private static int contadorDeRepeticoesDeResultados;

    private static int nivelDeVerbosidade;

    private static int melhor;
    private static int melhorGeracaoAnterior;
    private static Integer [] melhorNoGeral;

    private static CrossoverOBX crossoverOBX;

    private static int [] sequencia;


    //
    public GeneticAlgorithmStudents(int tamanhoDaTurmaRecebido, Integer [][] preferenciasTurmaArecebido, Integer [][] preferenciasTurmaBrecebido,
                                    int quantidadeDeCromossomosRecebida, double taxaDeIndviduosDaPopulacaoQueSofreraoCrossoverRecebido,
                                    double taxaDeGenesQueSofreraoCrossoverRecebida, double porcentagemDeCromossomosQueVaiSofrerMutacaoRecebida,
                                    double porcentagemDeGenesQueVaoSofrerMutacaoRecebida, int geracoesParaRodarRecebida,
                                    int pararAposXGeracoesRepetindoResultadosRecebido, int nivelDeVerbosidaderecebido, int opcaoDeSequenciaRecebido){

        // Inicializando variáveis da população
        quantidadeDeCromossomos = quantidadeDeCromossomosRecebida;
        tamanhoDaTurma = tamanhoDaTurmaRecebido;

        // Inicializando variáveis do crossover
        taxaDeIndviduosDaPopulacaoQueSofreraoCrossover = taxaDeIndviduosDaPopulacaoQueSofreraoCrossoverRecebido;
        taxaDeGenesQueSofreraoCrossover = taxaDeGenesQueSofreraoCrossoverRecebida;

        // Inicializando variáveis da mutação
        porcentagemDeCromossomosQueVaiSofrerMutacao = porcentagemDeCromossomosQueVaiSofrerMutacaoRecebida;
        porcentagemDeGenesQueVaoSofrerMutacao = porcentagemDeGenesQueVaoSofrerMutacaoRecebida;

        // Inicializando variáveis das gerações e solução
        geracoesParaRodar = geracoesParaRodarRecebida;
        pararAposXGeracoesRepetindoResultados = pararAposXGeracoesRepetindoResultadosRecebido;
        contadorDeRepeticoesDeResultados=0;
        melhorGeracaoAnterior=-1;
        melhorNoGeral=null;

        // Inicializando variável que vai definir o que é ou não mostrado na tela
        nivelDeVerbosidade = nivelDeVerbosidaderecebido;

        // Inicializando variáveis das preferências dos alunos
        preferenciasTurmaA = preferenciasTurmaArecebido;
        preferenciasTurmaB = preferenciasTurmaBrecebido;

        // Alocando o espaço das populações
        populacao = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];
        intermediaria = new Integer[quantidadeDeCromossomos][tamanhoDaTurma+1];

        // Define a sequência que será usada
        int opcaoDeSequencia = opcaoDeSequenciaRecebido;
        if (opcaoDeSequencia==0) sequencia = inteiros();
        if (opcaoDeSequencia==1) sequencia = primos();
        if (opcaoDeSequencia==2) sequencia = primosDiv2();
        if (opcaoDeSequencia==3) sequencia = fibonacci();
        if (opcaoDeSequencia==4) sequencia = potenciasDe2();
        if (opcaoDeSequencia==5) sequencia = escalaSequenciaInvertida(primos());
        if (opcaoDeSequencia==6) sequencia = escalaSequenciaInvertida(primosDiv2());
        if (opcaoDeSequencia==7) sequencia = escalaSequenciaInvertida(fibonacci());
        if (opcaoDeSequencia==8) sequencia = escalaSequenciaInvertida(potenciasDe2());
        if (opcaoDeSequencia==9) sequencia = inteirosComOdioNoFinal();
        if (nivelDeVerbosidade>1) {
            System.out.println("Sequência usada como máscara para cálculo da aptidão");
            printSequencia(sequencia);
        }

        // Criando a população inicial
        criaPopulacaoInicial();

        // Inicializando o Crossover. Numa futura versão pode-se usar outros tipos de crossover aqui
        crossoverOBX = new CrossoverOBX(taxaDeIndviduosDaPopulacaoQueSofreraoCrossover, taxaDeGenesQueSofreraoCrossover, populacao, nivelDeVerbosidade);


    }

    public static Integer [] runGenerations() {
        Random rand = new Random();

        // Roda as gerações
        for (int g=0; g<geracoesParaRodar; g++){
            if (nivelDeVerbosidade>0) {
                System.out.println("\n\n===================== Geração: " + g + " =====================");
            }
            calculaAptidao();

            // Mostra a população
            if (nivelDeVerbosidade>1) {
                System.out.print("População após aptidão calculada");
                printMatrizDaPopulacao();
            }

            // Calcula qual o melhor cromossomo
            melhor = getBest();
            if (nivelDeVerbosidade>0) {
                System.out.print("Pelo método de Elitismo, o melhor cromossomo encontrado é o " + melhor + " - ");
                printCromossomoComAptidao(melhor);
            }

            if (testaSolucao(g)) break;

            if (nivelDeVerbosidade>1) {
                System.out.println("\n\n=============== Crossover =================");
            System.out.print("População antes do crossover");
            printMatrizDaPopulacao();
            }

            // Faz o crossover
            intermediaria = crossoverOBX.crossover();

            if (nivelDeVerbosidade>1) {
            System.out.print("\nPopulação após o crossover");
            printMatrizDaIntermediaria();
            }

            // Como estamos usando variáveis estáticas precisamos faer isso para transportar os valores da intermediária
            // para a população. simplesmente fazer populacao = intermediária não funcionaria
            for (int p=0; p<populacao.length; p++){
                for (int q=0; q<populacao[0].length; q++){
                    populacao[p][q]=intermediaria[p][q];
                }
            }

            if (nivelDeVerbosidade>1) {
                System.out.println("\n=================== Mutação ====================");
                System.out.print("População antes da mutação");
                printMatrizDaPopulacao();
            }

            // Faz a mutação
            mutacao();

            if (nivelDeVerbosidade>1) {
                System.out.print("\nPopulação após a mutação");
                printMatrizDaPopulacao();
            }
        }
        return melhorNoGeral;
    }

    // Cria a população inicial
    public static void criaPopulacaoInicial() {
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
        System.out.print("\nSua aptidão é: " + populacao[cromossomo][populacao[cromossomo].length-1]);
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


    public static void printMatrizDaPopulacao() {
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

    public static void printMatrizDaIntermediaria() {
        int j = 0;
        System.out.println(" ");
        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            System.out.print("Cromossomo " + i + " - ");
            for (j=0; j < tamanhoDaTurma; j++) {
                System.out.print(intermediaria[i][j] + " ");
            }
            System.out.println("Aptidão: " + intermediaria[i][j]);
        }
    }

    public static void calculaAptidao(){
        for(int i = 0; i < quantidadeDeCromossomos; i++){
            populacao[i][tamanhoDaTurma] = aptidao(i);
        }
    }

    // A aptidão é calculada pela posição em que o aluno ocupa na lista de preferência. Quanto mais preferido, menor a aptidão.
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
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[populacao[x][i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

        }
        //System.out.println(" -- Aptidão: " + soma);
        return soma;
    }

    // Método auxiliar para encontrar o índice de um elemento dentro de um vetor
    public static int indexOf(int elemento, Integer [] vetor){
        for (int i=0; i<vetor.length; i++)
        {
            if (vetor[i] == elemento ) {
                return i;
            }
        }
        return -1;
    }

    // Método para calcular o cromossomo elite e colocá-lo na população intermediária
    public static int getBest(){
        int min = populacao[0][tamanhoDaTurma];
        int linha = 0;
        for(int i = 1; i < quantidadeDeCromossomos; i++){
            if(populacao[i][tamanhoDaTurma] < min){
                min = populacao[i][tamanhoDaTurma];
                linha = i;
            }
        }

        // Como estamos usando variáveis estáticas criamos esse cromossomo intermediário para evitar referências cruzadas
        Integer [] cromossomoInt = new Integer [populacao[0].length];

        for (int i=0; i<cromossomoInt.length; i++){
            cromossomoInt[i] = populacao[linha][i];
        }

        // Coloca o melhor na população intermediária
        for(int i = 0; i <= tamanhoDaTurma; i++) {
            intermediaria[0][i]=cromossomoInt[i];
        }

        return linha;
    }

    // Executa a mutação
    public static void mutacao(){
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

    // Testa se alguma das condições de parada foi encontrada e exibe a solução em caso positivo
    public static boolean testaSolucao(int geracao){

        // Testa se o melhor resultado está se repetindo
        if (melhor==melhorGeracaoAnterior){
            contadorDeRepeticoesDeResultados = contadorDeRepeticoesDeResultados +1;
            if (contadorDeRepeticoesDeResultados==pararAposXGeracoesRepetindoResultados){
                paradaPorRepeticao(melhor, geracao);
                return true;
            }
        }
        else{
            contadorDeRepeticoesDeResultados=0;
        }

        melhorGeracaoAnterior=melhor;

        // Testa se achou solução ideal
        if(populacao[melhor][tamanhoDaTurma]==0) {
            achouSolucao(melhor, geracao);
            return true;
        }

        // Se chegou na última geração e não encontrou nenhuma das condições de parada,
        // simplesmente mostra e melhor solução até o momento
        if (geracao==(geracoesParaRodar-1)){
            naoEncontrouCondicoesDeParada();
            return true;
        }

        return false;
    }

    // Quando achou a solução ideal
    public static void achouSolucao(int melhor, int geracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("Achou a solução ótima na geração " + geracao);
        printSolucaoDecodificada(melhor);
    }

    public static void paradaPorRepeticao(int melhor, int geracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("Parou a execução na geração " + geracao + ", pois ficou repetindo a mesma solução.");
        printSolucaoDecodificada(melhor);
    }

    public static void naoEncontrouCondicoesDeParada(){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("O algoritmo chegou na última geração e a melhor solução encontrada foi a seguinte: ");
        printSolucaoDecodificada(0);
    }

    public static void printSolucaoDecodificada (int melhor){
        System.out.print("Solução codificada ");
        printCromossomoComAptidao(melhor);
        System.out.println(" ");
        System.out.println("Solução decodificada: ");
        for (int i=0; i<populacao[melhor].length-1; i++){
            System.out.println("Aluno A" + (i+1) + " com Aluno B" + (populacao[melhor][i]+1));
        }
        melhorNoGeral=populacao[melhor];
        mostraPioresParesDeAlunos(melhor);
    }

    public static void mostraPioresParesDeAlunos(int melhor){
        int soma = 0;
        int posicaoDePreferenciaDoAluno;
        int valorDaPreferencia;
        int piorAptidao = 0;
        int piorPreferencia = 0;
        int posicaoMaisTriste=0;

        //descobre qual pior dupla e qual aluno que tem o que mais odeia, na pior posição de preferência
        for(int i = 0; i < tamanhoDaTurma; i++){
            //Vê as posições de preferências nas turma A
            posicaoDePreferenciaDoAluno = indexOf(populacao[melhor][i], preferenciasTurmaA[i]);
            //System.out.println("posição da preferencia do aluno " + populacao[x][i] + " da turma B para o aluno A" + i + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

            if (valorDaPreferencia > piorPreferencia) piorPreferencia = valorDaPreferencia;

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[populacao[melhor][i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

            if (valorDaPreferencia > piorPreferencia) piorPreferencia = valorDaPreferencia;

            if (soma > piorAptidao) piorAptidao=soma;
            soma =0;
        }

        soma=0;
        int quantidadeDeDuplasComPiorAptidao=0;
        String duplasComPiorAptidao = "A(s) dupla(s) com pior aptidão é(são): ";
        String alunoMaisTriste = "O(s) aluno(s) mais triste(s) é(são) o: ";
        // conta quantas duplas são piores
        for(int i = 0; i < tamanhoDaTurma; i++){
            //Vê as posições de preferências nas turma A
            posicaoDePreferenciaDoAluno = indexOf(populacao[melhor][i], preferenciasTurmaA[i]);
            //System.out.println("posição da preferencia do aluno " + populacao[x][i] + " da turma B para o aluno A" + i + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

            if (valorDaPreferencia == piorPreferencia){
                alunoMaisTriste = alunoMaisTriste + "A" + (i+1) + " ";
                posicaoMaisTriste = posicaoDePreferenciaDoAluno+1;
            }

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[populacao[melhor][i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = sequencia[posicaoDePreferenciaDoAluno];
            soma = soma + valorDaPreferencia;

            if (valorDaPreferencia == piorPreferencia){
                alunoMaisTriste = alunoMaisTriste + "B" + (populacao[melhor][i]+1) + " ";
                posicaoMaisTriste = posicaoDePreferenciaDoAluno+1;
            }

            if (soma == piorAptidao) {
                quantidadeDeDuplasComPiorAptidao ++;
                duplasComPiorAptidao = duplasComPiorAptidao + "A" + (i+1) + " com B" + (populacao[melhor][i]+1);
            }

            soma=0;
        }

        if (piorAptidao>0) {
            System.out.println(duplasComPiorAptidao);
            System.out.println(alunoMaisTriste + ", pois fez(fizeram) dupla com a aluno que está na " + posicaoMaisTriste + " posição da sua lista de preferência.");
        }

    }

    //
    //
    // planetas
    //
    //

    public static void runPlanets(){
        Integer [][] planetas = new Integer [quantidadeDeCromossomos][tamanhoDaTurma+1];
        Integer temp [] = new Integer[tamanhoDaTurma+1];
        for (int i=0; i<planetas.length;i++){
            // Criando a população inicial
            criaPopulacaoInicial();

            // Inicializando o Crossover. Numa futura versão pode-se usar outros tipos de crossover aqui
            crossoverOBX = new CrossoverOBX(taxaDeIndviduosDaPopulacaoQueSofreraoCrossover, taxaDeGenesQueSofreraoCrossover, populacao, nivelDeVerbosidade);
            temp=runGenerations();
            for (int j=0; j<temp.length; j++){
                planetas[i][j]=temp[j];
            }
            System.out.println("Planetas " + i + ": " + Arrays.toString(planetas[i]));
        }

        System.out.println("======================================");
        System.out.println("============== Planetas ==============");
        System.out.println("======================================");

        /*
        for (int i=0; i<planetas.length; i++) {
            System.out.println("Planetas " + i + ": " + Arrays.toString(planetas[i]));
        }
         */

        int j = 0;
        System.out.println(" ");
        for (int i = 0; i < quantidadeDeCromossomos; i++) {
            System.out.print("Planeta " + i + " - ");
            for (j=0; j < tamanhoDaTurma; j++) {
                System.out.print(planetas[i][j] + " ");
            }
            System.out.println("Aptidão: " + planetas[i][j]);
        }

        // Como estamos usando variáveis estáticas precisamos faer isso para transportar os valores da planetas
        // para a população. simplesmente fazer populacao = planetas não funcionaria
        for (int p=0; p<populacao.length; p++){
            for (int q=0; q<populacao[0].length; q++){
                populacao[p][q]=planetas[p][q];
            }
        }
        nivelDeVerbosidade=0;
        taxaDeGenesQueSofreraoCrossover = 0;
        porcentagemDeCromossomosQueVaiSofrerMutacao = 1;
        porcentagemDeGenesQueVaoSofrerMutacao = 0.01;
        runGenerations();
    }


    // Método auxiliar para gerar uma sequência numérica de Fibonacci para o cálculo de aptidão
    public static int [] fibonacci (){
        int [] fibonacci = new int[tamanhoDaTurma];

        fibonacci[0]=0;
        fibonacci[1]=1;
        fibonacci[2]=2;

        for (int i=3; i<tamanhoDaTurma;i++){
            fibonacci[i]=fibonacci[i-1]+fibonacci[i-2];
        }
        return fibonacci;
    }

    // Método auxiliar para gerar uma sequência numérica de números primos para o cálculo de aptidão
    public static int [] primos () {
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
                primos [indicePrimos] = i;
                indicePrimos++;
                if (indicePrimos==tamanhoDaTurma) break;
            }
        }
        return primos;
    }

    // Método auxiliar para gerar uma sequência numérica de números primos divididos por 2 para o cálculo de aptidão
    public static int [] primosDiv2 () {
        int [] primosDivididoPor2 = new int [tamanhoDaTurma];
        int maximo = Integer.MAX_VALUE;
        primosDivididoPor2[0]=0;

        int indicePrimos = 1;
        int i =0;
        int num =0;

        for (i = 3; i <= maximo; i++) {
            int counter=0;
            for(num =i; num>=1; num--) {
                if(i%num==0) counter = counter + 1;
            }
            if (counter ==2) {
                primosDivididoPor2 [indicePrimos] = (int) Math.ceil(i/2);
                indicePrimos++;
                if (indicePrimos==tamanhoDaTurma) break;
            }
        }
        return primosDivididoPor2;
    }

    // Método auxiliar para gerar uma sequência numérica de potências de 2 para o cálculo de aptidão
    private static int [] potenciasDe2 (){
        int [] potenciasDe2 = new int [tamanhoDaTurma];
        potenciasDe2[0]=0;
        for (int i=1; i<tamanhoDaTurma; i++){
            potenciasDe2[i] = (int) Math.pow(2,i);
        }
        return potenciasDe2;
    }

    // Método auxiliar para gerar uma sequência numérica de números 1 para o cálculo de aptidão
    private static int [] inteiros (){
        int [] inteiros = new int [tamanhoDaTurma];
        for (int i=0; i<tamanhoDaTurma; i++){
            inteiros[i] = i;
        }
        return inteiros;
    }

    // Método auxiliar para gerar uma sequência numérica de números 1 para o cálculo de aptidão
    private static int [] inteirosComOdioNoFinal (){
        int [] inteiros = new int [tamanhoDaTurma];
        for (int i=0; i<tamanhoDaTurma; i++){
            inteiros[i] = i;
        }

        int piores60porCento = (int) Math.ceil(tamanhoDaTurma*0.6);
        //System.out.println("60 por cento = " + piores60porCento);

        //Aumenta o valor dos piores 10%
        for (int i=0; i<piores60porCento; i++){
            inteiros [tamanhoDaTurma-1-i] = inteiros[tamanhoDaTurma-1-i] * 100;
        }

        return inteiros;
    }


    private static void printSequencia (int [] sequencia){
        System.out.println("========= Sequência =========");
        System.out.println(Arrays.toString(sequencia));
        //for (int i=0; i < sequencia.length; i++){
        //    System.out.print(sequencia[i] + " ");
        //}
    }

    private static int [] inverteSequencia (int[] sequenciaASerInvertida){
        for(int i=0; i<sequenciaASerInvertida.length/2; i++){
            int temp = sequenciaASerInvertida[i];
            sequenciaASerInvertida[i] = sequenciaASerInvertida[sequenciaASerInvertida.length -i -1];
            sequenciaASerInvertida[sequenciaASerInvertida.length -i -1] = temp;
        }
        return sequenciaASerInvertida;
    }

    private static int [] escalaSequenciaInvertida (int [] sequencia){
        int [] sequenciaInvertida = inverteSequencia(sequencia);
        int [] sequenciaComEscalaDaSequenciaInvertida = new int [tamanhoDaTurma];
        sequenciaComEscalaDaSequenciaInvertida[0] = 0;
        for (int i=1; i<sequenciaComEscalaDaSequenciaInvertida.length; i++){
            int diferencaEntre2NumerosDaSeqInvertida = sequenciaInvertida[i-1] - sequenciaInvertida[i];
            sequenciaComEscalaDaSequenciaInvertida[i]=sequenciaComEscalaDaSequenciaInvertida[i-1] + diferencaEntre2NumerosDaSeqInvertida;
        }
        return sequenciaComEscalaDaSequenciaInvertida;
    }
}


