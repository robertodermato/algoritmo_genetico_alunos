public class Main {
    public static void main(String[] args) {

        // Escolha o nome do arquivo que deverá ser lido

        String arquivo;
        //arquivo = "duplos4.txt"; // arquivo da professora
        //arquivo = "duplos4idela.txt";
        //arquivo = "duplos9ideal.txt";
        //arquivo = "duplos10.txt"; // arquivo da professora
        //arquivo = "duplos10ideal.txt";
        //arquivo = "duplos20.txt"; // arquivo da professora
        //arquivo = "duplos20ideal.txt";
        arquivo = "duplos50.txt"; // arquivo da professora
        //arquivo = "duplos50ideal.txt";
        //arquivo = "duplos100ideal.txt";
        //arquivo = "duplos250ideal.txt";

        // Essas sequências estão implementadas a titulo de curiosidade, pois parecem ser mais ineficazes
        // que a sequência de números inteiros, que é a opção 0, portanto recomenda-se não mudar esse parâmetro

        int opcaoDeSequencia;
        opcaoDeSequencia=0; // inteiros
        //opcaoDeSequencia=1; // primos
        //opcaoDeSequencia=2; // primos divididos por 2
        //opcaoDeSequencia=3; // fibonacci
        //opcaoDeSequencia=4; // potências de 2
        //opcaoDeSequencia=5; // primos invertida
        //opcaoDeSequencia=6; // primos divididos por 2 invertida
        //opcaoDeSequencia=7; // fibonacci invertida
        //opcaoDeSequencia=8; // potências de 2 invertida

        App app = new App(arquivo,opcaoDeSequencia);
        app.run();
    }
}
