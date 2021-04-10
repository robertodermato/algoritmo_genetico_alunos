// fonte de inspiração: https://github.com/sangreal/monitor-ye/blob/4f90f5c37d207a4ebfd9683af60badf5cd8c5e9f/enn-monitor-ai-test/enn-monitor-ai-common/src/main/java/enn/monitor/ai/ga/crossover/GACrossoverOBX.java
import java.util.*;

public class CrossoverOBX {

    private double taxaDeCromossomosQueSofreraoCrossover;
    private double taxaDeGenesQueSofreraoCrossover;
    private Integer [][] populacaoParaCrossover;
    private Integer [][] populacaoCriadaComCrossover;
    int quantidadeDeCromossomos;
    int quantidadeDeGenes;

    public CrossoverOBX(double taxaDeCromossomosQuesofreraoCrossoverRecebida, double taxaDeGenesQueSofreraoCrossoverRecebida, Integer [][] populacaoRecebida) {
        //taxaDeCromossomosQueSofreraoCrossover = taxaDeCromossomosQuesofreraoCrossoverRecebida;
        taxaDeGenesQueSofreraoCrossover = taxaDeGenesQueSofreraoCrossoverRecebida;
        populacaoParaCrossover=populacaoRecebida;
        quantidadeDeCromossomos=populacaoParaCrossover.length;
        quantidadeDeGenes=populacaoParaCrossover[0].length;
        populacaoCriadaComCrossover = new Integer [populacaoRecebida.length][populacaoRecebida[0].length];
    }

    public Integer [][] crossover(){
        Random rand = new Random();

        // Mantém o cromossomo Elite
        populacaoCriadaComCrossover[0] = populacaoParaCrossover[0];

        for (int j = 1; j< quantidadeDeCromossomos; j=j+2){
            int cromossomoPai1 = torneio();
            int cromossomoPai2 = torneio();

            while (cromossomoPai1==cromossomoPai2){
                cromossomoPai2 = torneio();
            }


            System.out.println("============Pegando indivíduos para crossover=======");
            System.out.println("Pai1 do crossover é: " + cromossomoPai1);
            System.out.println("Pai2 do crossover é: " + cromossomoPai2);


            doCrossoverOBX(j, cromossomoPai1, cromossomoPai2);
        }

        printMatrizDaPopulacaoCriadaComCrossover();

        return populacaoCriadaComCrossover;

    }

    public void doCrossoverOBX(int indiceParaFilho1, int pai1, int pai2) {
        Random rng = new Random();

        // Calcula em quantos genes ocorrerá o crossover
        int crossoverRateSum = (int) Math.ceil((quantidadeDeGenes -1) * taxaDeGenesQueSofreraoCrossover);


        System.out.println("");
        System.out.println("=============================== Crossover ================================");
        System.out.println("Como a taxa de crossover é " + taxaDeGenesQueSofreraoCrossover + " e o tamanho de um cromosso é " + (quantidadeDeGenes-1) + ", o crossover ocorrerá em " + crossoverRateSum + " genes.");

        Integer [] posicoesDoCrossover = new Integer[quantidadeDeGenes];

        Integer [] parent1 = null;
        Integer [] parent2 = null;

        Integer [] son1 = null;
        Integer [] son2 = null;

        //escolhendo as posições do genoma que sofrerão crossover. Vai até -1 pra não pegar a função de fitness
        for (int i=0; i < quantidadeDeGenes-1; i++){
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
            if (i==quantidadeDeGenes-2 && crossoverRateSum!=0) {
                i=-1;
                crossoverRateSum = (int) Math.ceil((quantidadeDeGenes -1) * taxaDeGenesQueSofreraoCrossover);
                for (int j=0; j<posicoesDoCrossover.length;j++){
                    posicoesDoCrossover[j]=0;
                }
            }
        }

        for (int i=0; i < quantidadeDeGenes; i++){
            if (posicoesDoCrossover[i]==null) posicoesDoCrossover[i]=0;
        }


        System.out.print("A máscara de crossover que será usada é (sendo 1 = gene que sofrerá crossover e 0 = não sofrerá): ");
        for (int i=0; i<quantidadeDeGenes-1; i++){
            if (i==quantidadeDeGenes-2){
                System.out.print(posicoesDoCrossover[i]);
                break;
            }
            System.out.print(posicoesDoCrossover[i]+", ");
        }



        System.out.println("");
        System.out.print("Ou seja o crossover ocorrerá nos genes (posições): ");
        for (int i=0; i<quantidadeDeGenes-1; i++){
            if(posicoesDoCrossover[i]==1) System.out.print(i + ", ");
        }


        System.out.println("");

        parent1 = populacaoParaCrossover[pai1];
        parent2 = populacaoParaCrossover[pai2];
        son1 = new Integer[parent1.length];
        son2 = new Integer[parent2.length];

        // Pai 1
        System.out.print("Pai 1: ");
        for (int i=0; i<parent1.length;i++){
            System.out.print(parent1[i] + ", ");
        }


        // Cria um vetor com os elementos que sofrerão crossover
        crossoverRateSum = (int) Math.ceil((quantidadeDeGenes -1) * taxaDeGenesQueSofreraoCrossover);
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
        crossoverRateSum = (int) Math.ceil((quantidadeDeGenes -1) * taxaDeGenesQueSofreraoCrossover);
        Integer [] elementosDoPai2 = new Integer[(int) crossoverRateSum];
        //System.out.println("Pai 2 tem um array com " + elementosDoPai2.length + " elementos");

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


        /*
        System.out.println("elementos do pai 1");
        for (int i=0; i <elementosDoPai1.length; i++){
            System.out.print(elementosDoPai1[i] + ", ");
        }
         */

        /*
        System.out.println("");
        System.out.println("parent2");
        for (int i=0; i<parent2.length; i++){
            System.out.print(parent2[i] + ", ");
        }
         */

        // Vê quais posições os genes de Pai 1 que sofrerão crossover estão no gene de pai 2
        Integer [] posicoesGenesPai1noPai2 = new Integer [elementosDoPai1.length];

        /*
        System.out.println("");
        System.out.println("posicoes gene pai2 lenght " + posicoesGenesPai1noPai2.length);
        System.out.println("");
        */

        int p=0;
        for (int i=0; i<elementosDoPai1.length; i++){
            for (int j=0; j<parent2.length-1; j++){
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
            for (int j=0; j<parent1.length-1; j++){
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
        System.out.print("De forma organizada, os genes do pai 2, ocupam as seguintes posições no pai 1: ");
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


        int indiceParaFilho2 = indiceParaFilho1+1;

        // Coloca os filhos na nova população, se o número sorteado for maior que a taxa de crossver mantém os pais e não faz crossver
        System.out.println("colocando os filhos na nova população");
        System.out.println("colocado os filhos " + indiceParaFilho1 + " e " + indiceParaFilho2);
        double sorteado = rng.nextDouble();
        if (sorteado > taxaDeCromossomosQueSofreraoCrossover) {
            populacaoCriadaComCrossover[indiceParaFilho1] = son1;
            if (indiceParaFilho2 < quantidadeDeCromossomos) populacaoCriadaComCrossover[indiceParaFilho2] = son2;
        }
        else{
            populacaoCriadaComCrossover[indiceParaFilho1] = parent1;
            if (indiceParaFilho2 < quantidadeDeCromossomos) populacaoCriadaComCrossover[indiceParaFilho2] = parent2;
        }

    }

    public int torneio(){
        Random rand = new Random();
        int individuo1 =0;
        int individuo2 =0;

        individuo1 = rand.nextInt(populacaoParaCrossover.length);
        individuo2 = rand.nextInt(populacaoParaCrossover.length);

        while (individuo2==individuo1){
            individuo2 = rand.nextInt(populacaoParaCrossover.length);
        }

        /*
        System.out.println("");
        System.out.println("========= Torneio ========");
        System.out.println("Aptidão do ind1 = " + populacaoParaCrossover[individuo1][populacaoParaCrossover[0].length-1] + " e seu ídice é " + individuo1);
        System.out.println("Aptidão do ind2 = " + populacaoParaCrossover[individuo2][populacaoParaCrossover[0].length-1] + " e seu ídice é " + individuo2);
        */

        if(populacaoParaCrossover[individuo1][populacaoParaCrossover[0].length-1] < populacaoParaCrossover[individuo2][populacaoParaCrossover[0].length-1])
            return individuo1;
        else
            return individuo2;
    }

    public void printMatrizDaPopulacaoCriadaComCrossover() {
        System.out.println(" ");
        int j=0;
        for (int i = 0; i < populacaoCriadaComCrossover.length; i++) {
            System.out.print("Cromossomo " + i + " - ");
            for (j = 0; j < populacaoCriadaComCrossover[0].length; j++) {
                System.out.print(populacaoCriadaComCrossover[i][j] + " ");
            }
            System.out.println("Aptidão: " + populacaoCriadaComCrossover[i][j-1]);
        }
    }


}