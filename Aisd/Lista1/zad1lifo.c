//zad 1  LIFO (last in first out)
#include <stdio.h>
#include <stdlib.h> //malloc

typedef struct QueueElement{
	int number;
	struct QueueElement* next; //nastepny element
} queueel;

typedef struct QueueLifo{	//struktura, żeby można stworzyc kilka kolejek
	queueel* first;
	queueel* last;
} stack;

void starting(stack* temp){	
	temp->first = temp->last = NULL; //puste
}

//dodaje nowy element na koniec struktury
void add(stack* all, int value){
	queueel* end = malloc(sizeof(queueel)); //zapisuje na stercie
	if(end == NULL){
		printf("blad malloc");
		return;
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

//usuwa najmlodszy element listy
void del(stack* all){
	queueel* tmp = all->first;
	
	if(tmp == NULL){
		printf("Pusto, nie można nic usunąć \n");
		return;
	}
	//jesli jest to ostatni element
	if(tmp->next == NULL){
		all->first = NULL; 
		all->last = NULL;
	}
	else{
		//szukanie przed ostatniego
		while(tmp->next->next != NULL){
			tmp = tmp->next;
		}
		all->last = tmp;
		tmp = tmp->next;
		all->last->next = NULL;
	}
	
	printf("Usunieto: %d \n", tmp->number); 
	free(tmp);
}

int main()
{
	stack* q = malloc(sizeof(stack));
	starting(q);
	
	for(int i=0; i<100; i++){
		add(q, i);
	}
	
	for(int i=0; i<100; i++){
		del(q);
	}	
	
	return 0;
}


