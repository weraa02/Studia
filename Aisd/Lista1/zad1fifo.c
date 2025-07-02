//zad 1 FIFO (first in first out)
#include <stdio.h>
#include <stdlib.h> //malloc

typedef struct QueueElement{
	int number;
	struct QueueElement* next; //(zeby nie bylo nieskonczonej ilosci pol), ograniczamy się do adresu nastepnego el.
} queueel;

typedef struct QueueFifo{	//struktura, żeby można stworzyc kilka kolejek
	queueel* first;
	queueel* last;
} queue;

void startingQ(queue* temp){
	temp->first = temp->last = NULL; //puste
}

//dodaje nowy element na koniec struktury
void add(queue* all, int value){
	queueel* end = malloc(sizeof(queueel)); //zapisuje na stercie
	if(end == NULL){
		printf("blad malloc");
		return;						//zmien
	}
	
	end->number = value;
	end->next = NULL;
	
	//jesli nie ma jeszcze nic
	if(all->last == NULL){
		all->first = end;
	}
	else{
		all->last->next = end;
	}
	all->last = end;
	
	printf("Dodano: %d \n", value); 
}

//usuwa najstarszy element listy
void del(queue* all){
	queueel* tmp = all->first;
	
	if(tmp == NULL){
		printf("Pusto, nie można nic usunąć \n");
		return;
	}
	if(tmp->next == NULL){	//to byl ostatni element
		all->last = NULL; 
	}
	all->first = tmp->next;
	
	printf("Usunieto: %d \n", tmp->number); 
	free(tmp); //zwalnia to co malloc'iem
}

int main()
{
	queue* q = malloc(sizeof(queue));
	startingQ(q);
	
	for(int i=0; i<100; i++){
		add(q, i);
	}
	
	for(int i=0; i<100; i++){
		del(q);
	}	
	
	return 0;
}
