public class Jantar { //classe principal 
      public static void main (String[] args) {
         Mesa mesa = new Mesa (); //instância de Mesa
         for (int filosofo = 0; filosofo < 5; filosofo++) { //iteração que cria 5 threads de 5 filósofos diferentes
            new Filosofo("Filosofo_" + filosofo, mesa, filosofo).start();
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------");

    }
}

class Filosofo extends Thread { //classe que representa os filósofos como threads
   final static int TEMPO_MAXIMO = 100; //tempo máximo que um filósofo pode ficar comendo ou pensando
   Mesa mesa;
   int filosofo; //número do filósofo

    public Filosofo (String nome, Mesa mesadejantar, int filo) { //construtor da classe
      super(nome);
      mesa = mesadejantar;
      filosofo = filo;
      System.out.println("O Filosofo " + filosofo + " sentou-se a mesa");
    }
    /*método que determina quando um filósofo irá comer ou 
    pensar devido ao seu tempo decorrido em cada estado*/
    public void run () { 
        int tempo = 0;
        while (true) {
            tempo = (int) (Math.random() * TEMPO_MAXIMO);
            pensar(tempo); //após passar um determinado tempo pensando o filósofo deve pegar os hashis e comer
            getHashi();
            tempo = (int) (Math.random() * TEMPO_MAXIMO);
            comer(tempo); //após passar um determinado tempo comendo o filósofo deve largar os hashis e pensar
            returnHashi();
        }
    }

    public void pensar (int tempo) { //método que representa o filósofo pensando
        try {
            sleep(tempo);
        }
        catch (InterruptedException e) { //caso passe do tempo máximo
            System.out.println("O Filosofo pensou muito");
        }
    }

    public void comer (int tempo) { //método que representa o filósofo comendo
        try {
            sleep(tempo);
        }
        catch (InterruptedException e) { //caso passe do tempo máximo
            System.out.println("O Filosofo comeu muito");
        }
    }

    public void getHashi() { //método auxiliar para os filósofos pegarem os Hashis
        mesa.pegarHashis(filosofo);
    }

    public void returnHashi() { //método auxiliar para os filósofos largarem os Hashis
      mesa.returningHashis(filosofo);
    }
}

class Mesa { //classe que representa o que está acontecendo na mesa
   final static int PENSANDO = 1;
   final static int COMENDO = 2;
   final static int FOME = 3;
   final static int NR_FILOSOFOS = 5;
   final static int PRIMEIRO_FILOSOFO = 0;
   final static int ULTIMO_FILOSOFO = NR_FILOSOFOS - 1;
   boolean[] hashis = new boolean[NR_FILOSOFOS]; //array para verificar se um hashi está disponível ou ocupado
   int[] filosofos = new int[NR_FILOSOFOS]; //array representando os 5 filósofos na mesa
   int[] tentativas = new int[NR_FILOSOFOS]; //array represetando o número de tentativas de comer de cada filósofo 

   public Mesa() { //construtor da classe que seta os três arrays citados acima
        for (int i = 0; i < 5; i++) {
            hashis[i] = true; //todos os hashis começam como "disponível (true)"
            filosofos[i] = PENSANDO;
            tentativas[i] = 0;
        }
    }
    /*método  que usa o synchronized para o filósofo (thread) pegar os Hashis 
    um por vez. Recebe o número do filosofo que irá tentar executar esta ação*/
    public synchronized void pegarHashis (int filosofo) { 
        
        filosofos[filosofo] = FOME; //a princípio define o filósofo com o status fome 
        
        /*enquanto o filósofo a sua esquerda ou a sua direita estiverem comendo,
        o filósofo em questão irá tentar pegar hashis, mas sem sucesso*/
        while (filosofos[aEsquerda(filosofo)] == COMENDO || filosofos[aDireita(filosofo)] == COMENDO){
            try{
                tentativas[filosofo]++;
                
                /*após uma tentativa, pausa a execução da thread e espera o retorno do método "notifyall" 
                para que sua thread volte ao estado de prontidão e entre de novo na fila para pegar o hashi*/
                wait(); 
            }   catch (InterruptedException e){}
        }
        
        /*após o filósofo sair do loop sem sucesso, significa que os hashis a sua
        direita e a sua esquerda estão disponíveis, portanto, ele irá pegá-los */

        hashis[hashiEsquerdo(filosofo)] = false; //definindo hashi a sua esquerda como ocupado
        System.out.println("O Filosofo " + filosofo + " pegou o hashi " + hashiEsquerdo(filosofo));

        hashis[hashiDireito(filosofo)] = false; //definindo hashi a sua direita como ocupado
        System.out.println("O Filosofo " + filosofo + " pegou o hashi " + hashiDireito(filosofo));

        System.out.println("----------------------------------------------------------------------------------------------------------------");

        filosofos[filosofo] = COMENDO; //após pegar os dois hashis o filósofo irá comer
        tentativas[filosofo] = 0; //após comer, as tentativas do filósofo são zeradas

        imprimeHashis();
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        imprimeTentativas();
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        imprimeEstadosFilosofos(); 
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println("----------------------------------------------NOVO CICLO--------------------------------------------------------");
    }

    /*método que usa o synchronized para o filósofo (thread)
    largar os Hashis um por vez após passar pelo tempo limite comendo.*/
    public synchronized void returningHashis (int filosofo){

        /*como este método é chamado sempre que um filosofo passou tempo suficiente pensando 
        não é necessário um loop para verificação ou condicionais para largar os hashis*/
        
        hashis[hashiEsquerdo(filosofo)] = true;//definindo hashi a sua esquerda como disponível
        System.out.println("O Filosofo " + filosofo + " largou o hashi " + hashiEsquerdo(filosofo));

        hashis[hashiDireito(filosofo)] = true; //definindo hashi a sua direita como disponível
        System.out.println("O Filosofo " + filosofo + " largou o hashi " + hashiDireito(filosofo));

        System.out.println("----------------------------------------------------------------------------------------------------------------");

        /*se os filosofos a esquerda ou a direita estiverem com fome,
        eles terão a chance de comer após o atual filoso largar os hashis*/
        if (filosofos[aEsquerda(filosofo)] == FOME || filosofos[aDireita(filosofo)] == FOME){
            notifyAll(); //notifica a thread que está em estado de wait para voltar ao estado de prontidão
        }
        filosofos[filosofo] = PENSANDO; //o filoso que largou os hashis irá agora pensar
        
        imprimeHashis();
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        imprimeTentativas();
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        imprimeEstadosFilosofos(); 
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println("----------------------------------------------NOVO CICLO--------------------------------------------------------");
    }

    public int aDireita (int filosofo){ //método para verificar qual é o filosofo a direita do chamado
        int direito;
        if (filosofo == ULTIMO_FILOSOFO){ //se o filosofo em questão for o último, o seu a direita será o primeiro
            direito = PRIMEIRO_FILOSOFO;
        }
        else{
            direito = filosofo + 1;
        }
        return direito;
    }

    public int aEsquerda (int filosofo){ //método para verificar qual é o filosofo a esquerda do chamado
        int esquerdo;
        if (filosofo == PRIMEIRO_FILOSOFO){ //se o filosofo em questão for o primeiro, o seu a direita será o último
            esquerdo = ULTIMO_FILOSOFO;
        }
        else{
            esquerdo = filosofo - 1;
        }
        return esquerdo;
    }

    public int hashiEsquerdo (int filosofo){ //método para definir qual é o hashi a esquerda do filósofo
        int hashiEsquerdo = filosofo;
        return hashiEsquerdo;
    }

    public int hashiDireito (int filosofo){ //método para definir qual é o hashi a direita do filósofo
        int hashiDireito;
        if (filosofo == ULTIMO_FILOSOFO){ //se o filosofo em questão for o último, o hashi será o primeiro
            hashiDireito = 0;
        }
        else {
            hashiDireito = filosofo + 1;
        }
        return hashiDireito;
    }

    public void imprimeEstadosFilosofos (){ //método que printa o estado de todos as threads a cada ciclo
        String texto = "*";
        for (int i = 0; i < NR_FILOSOFOS; i++){ //iteração que irá passar por todos os filósofos(threads)
            System.out.print("O Filosofo " + i + " Esta ");
            switch (filosofos[i]){ //verifica qual o estado do filósofo 
                case PENSANDO :
                texto = "PENSANDO";
                break;
                case FOME :
                texto = "COM FOME";
                break;
                case COMENDO :
                texto = "COMENDO";
                break;
            }
            System.out.print(texto + " \n");
        }
    }

    public void imprimeHashis (){ //método que printa o estado de todos os hashis a cada ciclo
        String hashi = "*";
        System.out.print("Hashis = [");
        for (int i = 0; i < NR_FILOSOFOS; i++){
            System.out.print("HASHI " + i);
            if (hashis[i]){
                hashi = " LIVRE";
            }
            else{
                hashi = " OCUPADO";
            }
            System.out.print(hashi);
            if(i != 4) System.out.print(" | ");
        }
        System.out.println("]");
    }

    public void imprimeTentativas (){ //método que printa a quantidade de vezes que um filosofo tentou pegar os hashis
        for (int i = 0; i < NR_FILOSOFOS; i++){
            System.out.println("O Filosofo " + i + " Tentou Pegar Hashi " + filosofos[i] + " Vez(es)");
        }
    }
}