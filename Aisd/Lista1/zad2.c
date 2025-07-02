//lista jednokierunkowa
#include <stdio.h>
#include <stdlib.h> 
#include <time.h>

typedef struct Note{
	int head;
	struct Note* tail;
} note;

typedef struct List{	//struktura, żeby można stworzyc kilka list
	note* first;
} list;


void listCreate(list* lista){
	lista->first = NULL;
}

//dodaje na poczatek istniejacej listy
void addstart(list* a, int newpart){
	note* end = malloc(sizeof(struct Note));
	
	end->head = newpart;
	end->tail = a->first;
	
	a->first = end;
	
	//printf("Dodano na poczatek listy: %d \n", a->first->head);
}

//dodaje element na koniec listy
void addend(list* a, int newpart){
	note* end = malloc(sizeof(note));
	
	end->head = newpart;
	end->tail = NULL;
	
	//pusta lista
	if(a->first == NULL){
		a->first = end;
	}
	else{
		note* tmp = a->first;
		while(tmp->tail != NULL){
			tmp = tmp->tail;
		}
	
		tmp->tail = end;
	}
	//printf("Dodano na koniec listy: %d \n", end->head);
}

//wyswietla wszystkie elementy listy
void showList(list* a){
	if(a->first == NULL){
		printf("Nie ma nic");
	}
	else{
		note* tmp = a->first;
		printf("%d ", tmp->head);
		while(tmp->tail != NULL) {
			tmp = tmp->tail;
			printf("%d ", tmp->head);
		}
	}
	printf("\n");
}

//usuwa ostatni element listy
void del(list* all){	
	note* tmp = all->first;
		
	if(tmp == NULL){
		printf("Pusto, nie można nic usunąć \n");
		return;
	}
	//jesli jest to ostatni element
	if(tmp->tail == NULL){
		all->first = NULL;
		//printf("Usunieto: %d \n", tmp->head);
	}
	else{
		//szukanie przed ostatniego
		while(tmp->tail->tail != NULL){
			tmp = tmp->tail;
		}

		//printf("Usunieto: %d \n", tmp->tail->head);
		tmp->tail = NULL;
	}
}

//łączy dwie listy
void merge(list* l1, list* l2){
	note* tmp = l1->first;
	while(tmp->tail != NULL){
		tmp = tmp->tail;
	}
	
	tmp->tail = l2->first;
	
	printf("Połączono listy \n");
}

//sprawdza sredni czas dostepu do podanego elemntu
void averageTime(list* lista , int position){
	clock_t start, end; //przechowuje liczbe cykli zegara;
	double execution_time = 0;

	for(int j=0; j<100;j++){
		start = clock(); //czas rozpoczecia
		
		note* tmp = lista->first;
		//szuka elementu
		for(int i=0; i< position;i++){
			if(tmp->tail == NULL){
				printf("Koniec listy");
				return;
			}
			tmp = tmp->tail;
		}
		
		end = clock(); //czas zakonczenia
		execution_time += ((double)(end - start))/CLOCKS_PER_SEC;
	}
	printf("Dla elementu %d: %f \n", position, execution_time/100);
}

int main() {
	//do losowosci
	time_t ti;
	srand((unsigned) time (&ti));
	
	//deklaracja listy
	list* lista1 = malloc(sizeof(list));
	listCreate(lista1);
	//wypelnienie listy losowymi liczbami (dla czytelnosci mniejszymi od 10000)
	for(int i=0; i<10000;i++){
		addend(lista1, rand()%10000);
	}
	
	printf("\nSprawdzanie średniego czasu\n");
	
	averageTime(lista1, 100);
	averageTime(lista1, 9999);
	averageTime(lista1, rand()%10000);
	
	printf("\nSpawdzanie merge \n");
	
	list* lista2 = malloc(sizeof(list));
	listCreate(lista2);
	
	for(int i=0; i<20;i++){ //tylko 20 elementow dla czytelnosci	
		addend(lista2, rand()%10000);
	}
	
	list* lista3 = malloc(sizeof(list));
	listCreate(lista3);
	
	for(int i=0; i<20;i++){
		addend(lista3, rand()%10000);
	}
	printf("Elementy listy2: \n");
	showList(lista2);
	printf("Elementy listy3: \n");
	showList(lista3);
	
	merge(lista2, lista3);
	printf("Po połączeniu: \n");
	showList(lista2);
	return 0;
}
