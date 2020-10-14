#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <error.h>

void *print_message_function(void *ptr );
int count = 0;

typedef struct {

    pthread_t thread;
    int meu_id;
    int outro;
} Posix, *P;


P criarThread(int meu_id, int outro){

    P res = (P) malloc(sizeof(Posix));
    pthread_create(&(res->thread), NULL, print_message_function, (void*) "Executando");
    pthread_join(res->thread, NULL);
    res->meu_id = meu_id;
    res->outro = outro;
    return res;
}

int main() {

    P p0 = criarThread(0, 1);
    P p1 = criarThread(1, 0);
    int i = 0;
    
    while(1){

        P p;
        if(count == 0) p = p0;
        else p = p1;

        while(count != p->meu_id);

        secao_critica(p->meu_id);

        count = p->outro;

        secao_nao_critica(p->meu_id);

        i++;
        printf("%d processos executados\n\n\n\n", i);
    }

    exit(0);

    return 0;
}


void secao_critica(int id) {

    printf("Count: %d\n", count);
    count++;

    printf("Thread %d na seção crítica\n", id);
}

void secao_nao_critica(int id) {

    printf("A thread %d saiu da seção crítica\n", id);
}


void *print_message_function(void *ptr ){}