import java.util.concurrent.Semaphore;

public class SemaforoBinario {

    //permite apenas uma thread por vez
	static Semaphore semaphore = new Semaphore(1);

	static class MutexThread extends Thread {

        //define nome da thread
		String name = "";

		MutexThread(String name) {
			this.name = name;
		}

        //começa a execução da thread
		public void teste() {

			try {

                //verifica se o semáforo está disponível
				System.out.println(name + " : semáforos disponíveis: " + semaphore.availablePermits());

                //bloqueia o uso de outros semaforos até que a thread termine a execução
				semaphore.acquire();
				System.out.println(name + " : começou a execução");

                //executa a thread
				try {

					for (int i = 1; i <= 5; i++) {

						System.out.println(name + " : executando operação " + i + ", semáforos disponíveis : " + semaphore.availablePermits());

						// espera um segundo para simular uma operação 'demorada'
						Thread.sleep(1000);

					}

				} finally {

                    //libera o semáforo
					semaphore.release();
					System.out.println(name + " : semáforos disponíveis: " + semaphore.availablePermits());

				}

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

		}

	}

	public static void main(String[] args) {

		System.out.println("Total de semáforos disponíveis : " + semaphore.availablePermits());

        //instancia as threads e inicia suas execuções
        
		MutexThread t1 = new MutexThread("A");
		t1.teste();

		MutexThread t2 = new MutexThread("B");
		t2.teste();

		MutexThread t3 = new MutexThread("C");
		t3.teste();

		MutexThread t4 = new MutexThread("D");
		t4.teste();

		MutexThread t5 = new MutexThread("E");
		t5.teste();

		MutexThread t6 = new MutexThread("F");
		t6.teste();

	}
}