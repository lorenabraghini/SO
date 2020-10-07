#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <error.h>

void *print_message_function(void *ptr );

int main() {

    pthread_t thread1, thread2, thread3, thread4, thread5;

    char *message1 = "Hello World";
    char *message2 = "Hello World";
    char *message3 = "Hello World";
    char *message4 = "Hello World";
    char *message5 = "Hello World";

    int  iret1, iret2, iret3, iret4, iret5;

    /* criacao de threads independentes as quais irao executar a funcao */

    iret1 = pthread_create(&thread1, NULL, print_message_function, (void*) message1);
    iret2 = pthread_create(&thread2, NULL, print_message_function, (void*) message2);
    iret3 = pthread_create(&thread3, NULL, print_message_function, (void*) message3);
    iret4 = pthread_create(&thread4, NULL, print_message_function, (void*) message4);
    iret5 = pthread_create(&thread5, NULL, print_message_function, (void*) message5);

    // espera ate as threads estarem completas para continuar.

    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL); 
    pthread_join(thread3, NULL); 
    pthread_join(thread4, NULL); 
    pthread_join(thread5, NULL); 

    // fim da execucao de cada thread

    printf("Fim da thread 1. Retorno: %d\n",iret1);
    printf("Fim da thread 2. Retorno: %d\n",iret2);
    printf("Fim da thread 3. Retorno: %d\n",iret3);
    printf("Fim da thread 4. Retorno: %d\n",iret4);
    printf("Fim da thread 5. Retorno: %d\n",iret5);

    exit(0);

    return 0;
}

void *print_message_function(void *ptr )
{
    // funcao que printa o hello world de cada thread
    char *message;
    message = (char *) ptr;
    printf("%s \n", message);
}