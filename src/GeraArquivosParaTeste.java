public class GeraArquivosParaTeste {

    public static void main(String[] args) {

        int tamanhoDaTurma = 250;
        System.out.println(tamanhoDaTurma);

        //Turma A
        for (int contador=0; contador < tamanhoDaTurma; contador++){
            System.out.print("A" + (contador+1) + " ");
            int indiceDoAlunoB = contador;
                for (int contadorInterno=0; contadorInterno < tamanhoDaTurma; contadorInterno++){
                    System.out.print("B"+ (indiceDoAlunoB+1) + " ");
                    indiceDoAlunoB = indiceDoAlunoB +1;
                    if (indiceDoAlunoB>tamanhoDaTurma-1) indiceDoAlunoB=0;
                }
            System.out.println();
        }

        // Turma B
        for (int contador=0; contador < tamanhoDaTurma; contador++) {
            System.out.print("B" + (contador + 1) + " ");
            int indiceDoAlunoA = contador;
            for (int contadorInterno = 0; contadorInterno < tamanhoDaTurma; contadorInterno++) {
                System.out.print("A" + (indiceDoAlunoA + 1) + " ");
                indiceDoAlunoA = indiceDoAlunoA + 1;
                if (indiceDoAlunoA > tamanhoDaTurma - 1) indiceDoAlunoA = 0;
            }
            System.out.println();
        }
    }
}
