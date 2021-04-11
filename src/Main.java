public class Main {
    public static void main(String[] args) {

        String arquivo;
        //arquivo = "duplos4.txt";
        //arquivo = "duplos4idela.txt";
        //arquivo = "duplos9ideal.txt";
        //arquivo = "duplos10.txt";
        //arquivo = "duplos10ideal.txt";
        //arquivo = "duplos20.txt";
        //arquivo = "duplos20ideal.txt";
        //arquivo = "duplos50.txt";
        //arquivo = "duplos50ideal.txt";
        arquivo = "duplos100ideal.txt";

        App app = new App(arquivo);
        app.run();
    }
}
