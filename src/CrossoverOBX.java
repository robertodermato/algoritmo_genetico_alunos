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

        long crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
        System.out.println("Como a taxa de crossover é " + crossoverRate + " e o tamanho de um cromosso é " + (populacao[0].length-1) + ", o crossover ocorrerá em " + crossoverRateSum + " genes");

        Integer [] posicoesDoDrossover = new Integer[populacao[0].length];

        Integer [] parent1 = null;
        Integer [] parent2 = null;

        Integer [] son1 = null;
        Integer [] son2 = null;

        //escolhendo as posições do genoma que sofrerão crossover. Vai até -1 pra não pegar a função de fitness
        for (int i=0; i<populacao[0].length-1; i++){
            posicoesDoDrossover[i]=rng.nextInt(2);
            if (posicoesDoDrossover[i] !=null && posicoesDoDrossover[i] == 1) crossoverRateSum = crossoverRateSum - 1;
            if (crossoverRateSum ==0) break;
            if (i==populacao[0].length-2 && crossoverRateSum!=0) {
                i=0;
                crossoverRateSum = Math.round((populacao[0].length -1) * crossoverRate);
            }
        }

        for (int i=0; i<populacao[0].length; i++){
            if (posicoesDoDrossover[i]==null) posicoesDoDrossover[i]=0;
        }

        System.out.print("+++++++++++++++=====================================posições do Crossover: ");
        for (int i=0; i<populacao[0].length-1; i++){
            System.out.print(posicoesDoDrossover[i]+",");
        }

        System.out.println("");
        System.out.print("Ou seja o crossover ocorrerá nos genes: ");
        for (int i=0; i<populacao[0].length-1; i++){
            if(posicoesDoDrossover[i]==1) System.out.print(i + ", ");
        }
/*
        for (int i = nBest; i < populacao.length; i += 2) {
            parent1 = populacao[i];
            parent2 = populacao[i + 1];

            if ((random.nextDouble() > crossoverRate) || (parent1 == parent2)) {
                continue;
            }

            //son1 = new SGenome();
            //son2 = new SGenome();

            OBX(parent1, parent2, son1);
            OBX(parent2, parent1, son2);
        }

 */
    }

    /*
    private void OBX(Integer [] parent1, Integer [] parent2, Integer [] son) {
        Random random = new Random();
        int i;
        int size;
        int pos;
        int value;

        Map<Integer, Integer> geneMap = new HashMap<Integer, Integer>();

        size = random.nextInt(parent1.length - 2);
        for (i = 0; i < size; ++i) {
            pos = random.nextInt(parent1.length);
            while (geneMap.containsKey(parent1.getGenome().get(pos))) {
                pos = random.nextInt(parent1.getGenome().size());
            }

            geneMap.put(parent1.getGenome().get(pos), pos);
        }

        for (i = 0; i < parent2.getGenome().size(); ++i) {
            if (geneMap.containsKey(parent2.getGenome().get(i)) == true) {
                value = parent1.getGenome().get(geneMap.get(parent2.getGenome().get(i)));
            } else {
                value = parent2.getGenome().get(i);
            }

            son.getGenome().add(value);
        }
    }

     */

}