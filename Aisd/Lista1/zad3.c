//lista dwukierunkowa cykliczna + merge
#include <stdio.h>
#include <stdlib.h> 
#include <time.h>

typedef struct Note{
	int head;
	struct Note* past;
	struct Note* next;
} note;

typedef struct List{	//struktura, żeby można stworzyc kilka list
	note* point;
} list;


void listCreate(list* lista){
	lista->point = NULL;
}

//dodaje element na koniec listy
void add(list* lista, int newpart){
	note* item = malloc(sizeof(note));
	
	item->head = newpart;
	
	//pusta lista
	if(lista->point == NULL){
		lista->point = item;
		item->next = item;
		item->past = item;
	}
	else{ //ma zawartosc
		item->next = lista->point;
		item->past = lista->point->past; 
		//lista->point->prev punkt ktory byl prev do pointa, chcemy byc jego next
		lista->point->past->next = item;
		lista->point->past = item;
	}
	//printf("Dodano na koniec listy: %d \n", end->head);
}


//wyswietla wszystkie elementy listy
void showList(list* a){
	note* tmp = a->point;
	if(tmp == NULL){
		printf("Nie ma nic");
	}
	else{
		printf("%d ", tmp->head);
		while(tmp->next != a->point) {
			tmp = tmp->next;
			printf("%d ", tmp->head);
		}
	}
	printf("\n");
}

//laczy dwie listy
void merge(list* l1, list* l2){
	note* point1 = l1->point; //dla czytelnosci
	note* point2 = l2->point;
	note* temp = point1->past;

	point1->past->next = point2; //ten przed point1, laczymy z point2
	point1->past = point2->past;
	point2->past->next = point1;
	point2->past = temp;
	
	printf("Połączono listy \n");
}

void averageTime(list* lista , int position){
	clock_t start, end; //przechowuje liczbe cykli zegara;
	double execution_time = 0;

	for(int j=0; j<100;j++){
		start = clock(); //czas rozpoczecia
		
		note* tmp = lista->point;
		//szuka elementu
		for(int i=0; i< position;i++){
			if(tmp->next == lista->point){
				printf("Przeszedł wszystkie");
				return;
			}
			tmp = tmp->next;
		}
		
		end = clock(); //czas zakonczenia
		execution_time += ((double)(end - start))/CLOCKS_PER_SEC;
	}
	printf("Dla elementu %d: %f \n", position, execution_time/100);
}


int main(){
	//do losowosci
	time_t ti;
	srand((unsigned) time (&ti));
	
	list* lista1 = malloc(sizeof(list));
	listCreate(lista1);
	//wypelnienie listy losowymi liczbami (dla czytelnosci mniejszymi od 10000)
	for(int i=0; i<10000;i++){

		add(lista1, rand()%10000);
	}
	//showList(lista1);
	

	printf("\n\n\nSprawdzanie średniego czasu\n");
	
	averageTime(lista1, 100);
	averageTime(lista1, 9999);
	averageTime(lista1, rand()%10000); 
	
	printf("\n\n\nSpawdzanie merge \n");
	
	list* lista2 = malloc(sizeof(list));
	listCreate(lista2);
	
	for(int i=0; i<20;i++){	
		add(lista2, rand()%10000);
	}
	
	list* lista3 = malloc(sizeof(list));
	listCreate(lista3);
	
	for(int i=0; i<20;i++){	
		add(lista3, rand()%10000);
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
