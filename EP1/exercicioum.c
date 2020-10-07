#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

int main(){

    pid_t filho;

    int i; //contador do filho
    int status; //informa se o processo filho ja foi finalizado

    //criacao da thread filho
    filho = fork();

    if(filho == 0){

        //executa o processo filho

        printf("Hello World.\n");

        for(i = 0; i < 5; i++){
            printf("%d\n", i);
            sleep(2); //espera 2ms para continuar
        }
        printf("\nFim do processo filho.\n\n");
        _exit(0); //saida imediata do processo filho para retornar o controle para o processo pai
    }

    else {

        //executa o processo pai

        wait(filho, &status, 0); //caso o pai execute primeiro, ele vai esperar o seu filho executar para aí sim ele poder executar
        printf("Hello World.\n");
        printf("\nFim do processo pai.\n");
    }

    return 0;
}