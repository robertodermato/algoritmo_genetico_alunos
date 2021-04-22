
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
public class SimulatedAnnealing {
    private static Integer [][] preferenciasTurmaA;
    private static Integer [][] preferenciasTurmaB;

    private static int tamanhoDaTurma;

    private static Integer [] pareamentoAtual;
    private static Integer [] pareamentoVizinho;

    private double temperatura;
    private double taxaDeResfriamento;

    private int iteracoes;

    public SimulatedAnnealing(Integer[][] preferenciasTurmaARecebido, Integer[][] preferenciasTurmaBrecebido, int tamanhoDaTurmaRecebido,
                              double temperaturaRecebido, double taxaDeResfriamentoRecebido, int iteracoesRecebido){

        preferenciasTurmaA = preferenciasTurmaARecebido;
        preferenciasTurmaB = preferenciasTurmaBrecebido;

        tamanhoDaTurma = tamanhoDaTurmaRecebido;

        pareamentoAtual = new Integer [tamanhoDaTurma];
        pareamentoVizinho = new Integer [tamanhoDaTurma];

        Random r = new Random();
        pareamentoAtual = geraPareamentoInicial();

        temperatura = temperaturaRecebido;
        taxaDeResfriamento = taxaDeResfriamentoRecebido;
        iteracoes=iteracoesRecebido;
    }


    public Integer [] geraPareamentoInicial() {
        Integer[] pareamentoInicial = new Integer[tamanhoDaTurma];

        // Cria um vetor com os números em ordem
        for (int i = 0; i < pareamentoInicial.length; i++) {
            pareamentoInicial[i] = i;
        }

        Collections.shuffle(Arrays.asList(pareamentoInicial));

        return pareamentoInicial;
    }

    // O valor da heurística é calculada pela posição em que o aluno ocupa na lista de preferência. Quanto mais preferido, menor a heurística.
    // Logo, quanto menor a heurística, melhor a afinidade entre os alunos.
    public int calculaHeuristica(Integer [] pareamento){
        int soma = 0;
        int posicaoDePreferenciaDoAluno;
        int valorDaPreferencia;

        for(int i = 0; i < tamanhoDaTurma; i++){
            //Vê as posições de preferências nas turma A
            posicaoDePreferenciaDoAluno = indexOf(pareamento[i], preferenciasTurmaA[i]);
            //System.out.println("posição da preferencia do aluno " + populacao[x][i] + " da turma B para o aluno A" + i + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = posicaoDePreferenciaDoAluno;
            soma = soma + valorDaPreferencia;

            //Vê as posições de preferências nas turma B
            posicaoDePreferenciaDoAluno = indexOf(i, preferenciasTurmaB[pareamento[i]]);
            //System.out.println("posição da preferencia do aluno " + i + " da turma A para o aluno B" + populacao[x][i] + " é: " + posicaoDePreferenciaDoAluno);
            valorDaPreferencia = posicaoDePreferenciaDoAluno;
            soma = soma + valorDaPreferencia;

        }
        //System.out.println(" -- Heurística: " + soma);
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

    // Gera Pareamento Vizinho
    public void geraPareamentoVizinho(){
        Random rand = new Random();

        for(int i = 0; i< pareamentoAtual.length; i++){
            pareamentoVizinho[i] = pareamentoAtual[i];
        }

        // Escolhe as posições que sofrerão troca
        int posicao1 = rand.nextInt(tamanhoDaTurma);
        int posicao2 = rand.nextInt(tamanhoDaTurma);
        // Evita escolher duas vezes a mesma posição
        while (posicao1 == posicao2) {
            posicao2 = rand.nextInt(tamanhoDaTurma);
        }

        // Faz a troca
        int conteudoPosicao1 = pareamentoVizinho[posicao1];
        int conteudoPosicao2 = pareamentoVizinho[posicao2];
        int conteudoIntermediario = conteudoPosicao1;
        conteudoPosicao1 = conteudoPosicao2;
        conteudoPosicao2 = conteudoIntermediario;

        pareamentoVizinho[posicao1] = conteudoPosicao1;
        pareamentoVizinho[posicao2] = conteudoPosicao2;
    }

    public void copiaSolucao(Integer v[]){
        for(int i = 0; i< pareamentoAtual.length; i++){
            pareamentoAtual[i] = v[i];
        }
    }

    public void runAlgoritmo(){
        Random r = new Random();

        int valorPareamentoAtual;
        int valorPareamentoVizinho;

        double energia;
        double probabilidade;
        double valor;

        System.out.println("Simulated Annealing ");
        System.out.println("Pareamento atual h:" + calculaHeuristica(pareamentoAtual));

        for(int t=1; t<=iteracoes; t++){
            valorPareamentoAtual = calculaHeuristica(pareamentoAtual);
            System.out.print( "Ciclo: " + t + "- Temperatura: " + temperatura + " -") ;
            System.out.println("Solução Atual - h=" + valorPareamentoAtual);
            if(valorPareamentoAtual==0) {
                achouSolucao(t);
                break;
            }

            geraPareamentoVizinho();
            valorPareamentoVizinho = calculaHeuristica(pareamentoVizinho);
            //           System.out.println("Solução Vizinha - h=" + valorPareamentoVizinho + "\n" +print(distribuicaoVizinha,valorPareamentoVizinho));

            energia = valorPareamentoVizinho - valorPareamentoAtual;
            if(energia<0){
                copiaSolucao(pareamentoVizinho);
            }
            else {
                probabilidade = Math.exp(-energia/temperatura);
                valor = r.nextDouble();
                if(valor <probabilidade) {
                    System.out.println("Aceitou uma solução pior...");
                    copiaSolucao(pareamentoVizinho);
                }
            }
            temperatura = temperatura * (1 - taxaDeResfriamento);

            // Se chegou ao final das iteracoes e não encontrou nenhuma das condições de parada
            if(t==iteracoes){
                naoEncontrouCondicoesDeParada();
                break;
            }
        }

        // Criei esse método para tentar encontrar uma melhor solução que o algoritmo genético.
        // No algoritmo genético a melhor heurística achaada foi 529
        /*
        if (calculaHeuristica(pareamentoAtual) > 540) {
            temperatura = 1000;
            reaquecerForja(2);
        }
        else printSolucaoDecodificada(pareamentoAtual);
        */

    }

    // Quando achou a solução ideal
    public void achouSolucao(int iteracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("Achou a solução ótima na iteração " + iteracao);
        printSolucaoDecodificada(pareamentoAtual);
    }

    public void paradaPorRepeticao(int iteracao){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("Parou a execução na iteracao " + iteracao + ", pois ficou repetindo a mesma solução.");
        printSolucaoDecodificada(pareamentoAtual);
    }

    public void naoEncontrouCondicoesDeParada(){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("=================================================");
        System.out.println("==================== Solução ====================");
        System.out.println("=================================================");
        System.out.println("O algoritmo chegou na última iteracao e a melhor solução encontrada foi a seguinte: ");
        printSolucaoDecodificada(pareamentoAtual);
    }

    public void printSolucaoDecodificada (Integer [] pareamento){
        System.out.println("Solução codificada: " + Arrays.toString(pareamento));
        System.out.println("Valor da heurística: " + calculaHeuristica(pareamento));
        System.out.println("Solução decodificada: ");
        for (int i=0; i<pareamento.length; i++){
            System.out.println("Aluno A" + (i+1) + " com Aluno B" + (pareamento[i]+1));
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

    public void reaquecerForja (int quantidadeDeReaquecimentos){
        Integer [] melhorPareamentoAteOMomento = new Integer [pareamentoAtual.length];
        Integer [] melhorPareamentoDesseCiclo = new Integer [pareamentoAtual.length];
        for (int i=0; i<melhorPareamentoAteOMomento.length; i++) {
            melhorPareamentoAteOMomento[i] = pareamentoAtual[i];
        }

        for (int i=0; i<quantidadeDeReaquecimentos; i++){
            runAlgoritmo();
            for (int j=0; j<melhorPareamentoDesseCiclo.length; j++) {
                melhorPareamentoDesseCiclo[j] = pareamentoAtual[j];
            }
            if (calculaHeuristica(melhorPareamentoDesseCiclo) < calculaHeuristica(melhorPareamentoAteOMomento)) melhorPareamentoAteOMomento=melhorPareamentoDesseCiclo;
            System.out.println("Ciclo " + quantidadeDeReaquecimentos + " h:" + calculaHeuristica(melhorPareamentoAteOMomento));
            pareamentoAtual = melhorPareamentoAteOMomento;
            System.out.println("Ciclo " + quantidadeDeReaquecimentos + " h:" + calculaHeuristica(pareamentoAtual));
        }

        int valorPareamentoAtual = calculaHeuristica(pareamentoAtual);
        System.out.println("Solução final - h=" + valorPareamentoAtual);
    }
}



