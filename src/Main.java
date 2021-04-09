public class Main {
    public static void main(String[] args) {

        String arquivo;
        //arquivo = "duplos4.txt";
        arquivo = "duplos9ideal.txt";
        //arquivo = "duplos10.txt";
        //arquivo = "duplos20.txt";
        //arquivo = "duplos50.txt";
        //arquivo = "duplos4idela.txt";

        App app = new App(arquivo);
        app.run();
    }
}
