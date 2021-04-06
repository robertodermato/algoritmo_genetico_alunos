// source: https://github.com/sangreal/monitor-ye/blob/4f90f5c37d207a4ebfd9683af60badf5cd8c5e9f/enn-monitor-ai-test/enn-monitor-ai-common/src/main/java/enn/monitor/ai/ga/crossover/GACrossoverOBX.java

//package enn.monitor.ai.ga.crossover;

import java.util.*;

//import enn.monitor.ai.ga.common.SGenome;

public class CrossoverOBX {

    private double crossoverRate;

    public CrossoverOBX(double taxaDeCrossover) {
        crossoverRate=taxaDeCrossover;
    }

    public void crossover(Integer [][] populacao, int nBest) {
        Random rng = new Random();

        // Calcula em quantos genes ocorrerá o crossover
        long crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
        System.out.println("");
        System.out.println("=============================== Crossover ================================");
        System.out.println("Como a taxa de crossover é " + crossoverRate + " e o tamanho de um cromosso é " + (populacao[0].length-1) + ", o crossover ocorrerá em " + crossoverRateSum + " genes.");

        Integer [] posicoesDoCrossover = new Integer[populacao[0].length];

        Integer [] parent1 = null;
        Integer [] parent2 = null;

        Integer [] son1 = null;
        Integer [] son2 = null;

        //escolhendo as posições do genoma que sofrerão crossover. Vai até -1 pra não pegar a função de fitness
        for (int i=0; i<populacao[0].length-1; i++){
            posicoesDoCrossover[i]=rng.nextInt(2);
            //System.out.println("posicoesDoCrossover[" +i +"]= " +posicoesDoCrossover[i] );

            if (posicoesDoCrossover[i] !=null && posicoesDoCrossover[i] == 1) {
                crossoverRateSum = crossoverRateSum - 1;
            }

            // Se já atingiu o número necessário de genes selecionados para o crossover, termina o loop
            if (crossoverRateSum ==0) {
                //System.out.println("Crossover é zero. Deu break");
                break;
            }

            // Se chegou ao final do loop e ainda não atingiu o número necessário de genes selecionados para o crossover, reseta o vetor e reinicia o loop
            if (i==populacao[0].length-2 && crossoverRateSum!=0) {
                i=-1;
                crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
                for (int j=0; j<posicoesDoCrossover.length;j++){
                    posicoesDoCrossover[j]=0;
                }
            }
        }

        for (int i=0; i<populacao[0].length; i++){
            if (posicoesDoCrossover[i]==null) posicoesDoCrossover[i]=0;
        }

        System.out.print("A máscara de crossover que será usada é (sendo 1 = gene que sofrerá crossover e 0 = não sofrerá): ");
        for (int i=0; i<populacao[0].length-1; i++){
            if (i==populacao[0].length-2){
                System.out.print(posicoesDoCrossover[i]);
                break;
            }
            System.out.print(posicoesDoCrossover[i]+", ");
        }

        System.out.println("");
        System.out.print("Ou seja o crossover ocorrerá nos genes (posições): ");
        for (int i=0; i<populacao[0].length-1; i++){
            if(posicoesDoCrossover[i]==1) System.out.print(i + ", ");
        }

        System.out.println("");

        parent1 = new Integer[4];
        parent2 = new Integer[4];
        son1 = new Integer[4];
        son2 = new Integer[4];

        for (int i=0; i<parent1.length;i++){
            parent1[i]=i+4;
            parent2[i]=3-i+4;
        }

        // Pai 1
        System.out.print("Pai 1: ");
        for (int i=0; i<parent1.length;i++){
            System.out.print(parent1[i] + ", ");
        }

        // Cria um vetor com os elementos que sofrerão crossover
        crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
        Integer [] elementosDoPai1 = new Integer[(int) crossoverRateSum];

        int k=0;
        for (int i=0; i<posicoesDoCrossover.length; i++){
             if (posicoesDoCrossover[i]==1) {
                 elementosDoPai1[k]=parent1[i];
                 k++;
             }
        }

        System.out.println("");
        System.out.print("Os elementos que sofrerão crossover em Pai1 são: ");
        for (int i=0; i<elementosDoPai1.length; i++){
            System.out.print(elementosDoPai1[i] + ", ");
        }

        // Pai 2

        System.out.println("");
        System.out.print("Pai 2: ");
        for (int i=0; i<parent2.length;i++){
            System.out.print(parent2[i] + ", ");
        }

        // Cria um vetor com os elementos que sofrerão crossover
        crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
        Integer [] elementosDoPai2 = new Integer[(int) crossoverRateSum];

        k=0;
        for (int i=0; i<posicoesDoCrossover.length; i++){
            if (posicoesDoCrossover[i]==1) {
                elementosDoPai2[k]=parent2[i];
                k++;
            }
        }

        System.out.println("");
        System.out.print("Os elementos que sofrerão crossover em Pai2 são: ");
        for (int i=0; i<elementosDoPai2.length; i++){
            System.out.print(elementosDoPai2[i] + ", ");
        }

        // Vê quais posições os genes de Pai 1 que sofrerão crossover estão no gene de pai 2
        Integer [] posicoesGenesPai1noPai2 = new Integer [elementosDoPai1.length];
        int p=0;
        for (int i=0; i<elementosDoPai1.length; i++){
            for (int j=0; j<parent2.length; j++){
                if (elementosDoPai1[i]==parent2[j]){
                    posicoesGenesPai1noPai2[p]=j;
                    p++;
                }
            }
        }

        System.out.println("");
        System.out.print("Os genes do pai 1, ocupam as seguines posições no pai 2: ");
        for (int i=0; i<posicoesGenesPai1noPai2.length; i++){
            System.out.print(posicoesGenesPai1noPai2[i] + ", ");
        }

        // coloca em ordem as posições que irão sofrer crossover
        Collections.sort(Arrays.asList(posicoesGenesPai1noPai2));

        System.out.println("");
        System.out.print("De forma organizada, os genes do pai 1, ocupam as seguines posições no pai 2: ");
        for (int i=0; i<posicoesGenesPai1noPai2.length; i++){
            System.out.print(posicoesGenesPai1noPai2[i] + ", ");
        }

        // Vê quais posições os genes de Pai 2 que sofrerão crossover estão no gene de Pai 1
        Integer [] posicoesGenesPai2noPai1 = new Integer [elementosDoPai2.length];
        p=0;
        for (int i=0; i<elementosDoPai2.length; i++){
            for (int j=0; j<parent1.length; j++){
                if (elementosDoPai2[i]==parent1[j]){
                    posicoesGenesPai2noPai1[p]=j;
                    p++;
                }
            }
        }

        System.out.println("");
        System.out.print("Os genes do pai 2, ocupam as seguines posições no pai 1: ");
        for (int i=0; i<posicoesGenesPai2noPai1.length; i++){
            System.out.print(posicoesGenesPai2noPai1[i] + ", ");
        }

        // coloca em ordem as posições que irão sofrer crossover
        Collections.sort(Arrays.asList(posicoesGenesPai2noPai1));

        System.out.println("");
        System.out.print("De forma organizada, os genes do pai 2, ocupam as seguines posições no pai 1: ");
        for (int i=0; i<posicoesGenesPai2noPai1.length; i++){
            System.out.print(posicoesGenesPai2noPai1[i] + ", ");
        }

        p=0;
        for (int i=0; i<parent1.length;i++){
            if(posicoesDoCrossover[i]==0) son1[i] = parent1[i];
            else {
                son1[i]=parent2[posicoesGenesPai1noPai2[p]];
                p++;
            }
        }

        p=0;
        for (int i=0; i<parent2.length;i++){
            if(posicoesDoCrossover[i]==0) son2[i] = parent2[i];
            else {
                son2[i]=parent1[posicoesGenesPai2noPai1[p]];
                p++;
            }
        }


        System.out.println("");
        System.out.print("Filho 1: ");
        for (int i=0; i<son1.length;i++){
            System.out.print(son1[i] + ", ");
        }

        System.out.println("");
        System.out.print("Filho 2: ");
        for (int i=0; i<son2.length;i++){
            System.out.print(son2[i] + ", ");
        }




    }



}