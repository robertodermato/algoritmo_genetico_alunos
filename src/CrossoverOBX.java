// source: https://github.com/sangreal/monitor-ye/blob/4f90f5c37d207a4ebfd9683af60badf5cd8c5e9f/enn-monitor-ai-test/enn-monitor-ai-common/src/main/java/enn/monitor/ai/ga/crossover/GACrossoverOBX.java

//package enn.monitor.ai.ga.crossover;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import enn.monitor.ai.ga.common.SGenome;

public class CrossoverOBX {

    private double crossoverRate;

    public CrossoverOBX() {
        crossoverRate=0.5;
    }

    public void crossover(Integer [][] populacao, int nBest) {
        Random rng = new Random();

        // Calcula em quantos genes ocorrerá o crossover
        long crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
        System.out.println("");
        System.out.println("=============================== Crossover ================================");
        System.out.println("Como a taxa de crossover é " + crossoverRate + " e o tamanho de um cromosso é " + (populacao[0].length-1) + ", o crossover ocorrerá em " + crossoverRateSum + " genes.");

        Integer [] posicoesDoDrossover = new Integer[populacao[0].length];

        Integer [] parent1 = null;
        Integer [] parent2 = null;

        Integer [] son1 = null;
        Integer [] son2 = null;

        //escolhendo as posições do genoma que sofrerão crossover. Vai até -1 pra não pegar a função de fitness
        for (int i=0; i<populacao[0].length-1; i++){
            posicoesDoDrossover[i]=rng.nextInt(2);
            //System.out.println("posicoesDoDrossover[" +i +"]= " +posicoesDoDrossover[i] );

            if (posicoesDoDrossover[i] !=null && posicoesDoDrossover[i] == 1) {
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
                for (int j=0; j<posicoesDoDrossover.length;j++){
                    posicoesDoDrossover[j]=0;
                }
            }
        }

        for (int i=0; i<populacao[0].length; i++){
            if (posicoesDoDrossover[i]==null) posicoesDoDrossover[i]=0;
        }

        System.out.print("As posições onde ocorrerá o crossover são: ");
        for (int i=0; i<populacao[0].length-1; i++){
            if (i==populacao[0].length-2){
                System.out.print(posicoesDoDrossover[i]);
                break;
            }
            System.out.print(posicoesDoDrossover[i]+", ");
        }

        System.out.println("");
        System.out.print("Ou seja o crossover ocorrerá nos genes: ");
        for (int i=0; i<populacao[0].length-1; i++){
            if(posicoesDoDrossover[i]==1) System.out.print(i + ", ");
        }

        System.out.println("");

    }



}