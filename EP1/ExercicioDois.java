class ExercicioDois {

    public static void main(String[] args) {

        //inicia todas as threads com tempos diferentes

        MinhaThread thread = new MinhaThread("Thread #1", 100);

        MinhaThread thread2 = new MinhaThread("Thread #2", 200);

        MinhaThread thread3 = new MinhaThread("Thread #3", 300);

        MinhaThread thread4 = new MinhaThread("Thread #4", 400);

        MinhaThread thread5 = new MinhaThread("Thread #5", 500);

        MinhaThread thread6 = new MinhaThread("Thread #6", 600);

        MinhaThread thread7 = new MinhaThread("Thread #7", 700);

        MinhaThread thread8 = new MinhaThread("Thread #8", 800);

        MinhaThread thread9 = new MinhaThread("Thread #9", 900);

        MinhaThread thread10 = new MinhaThread("Thread #10", 1000);
    }
}

class MinhaThread extends Thread {

    private String nome;
    private int tempo;

    //construtor que inicia a thread
    public MinhaThread(String nome, int tempo){
        this.nome = nome;
        this.tempo = tempo;
        start();
    }

    //metodo que executa a thread
    public void run(){

        try {
            for (int i=0; i<4; i++){
                System.out.println(nome + "Hello World " + i);
                Thread.sleep(tempo);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(nome + " terminou a execução");
    }

}
