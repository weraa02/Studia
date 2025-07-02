//Weronika Chmiela
//aisd lista 2 zad1
//quick Sort

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class QuickSort {

    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    
    public static void main(String[] args) {
        Scanner pisani = new Scanner(System.in); //zeby z klawiatury

        int n; //dlugosc tablicy
        int[] klucze; //tablica


        try{
            System.out.println("Podaj długość tablicy i jej elementy:");
            n = pisani.nextInt();

            klucze = new int[n];		
            for(int i=0; i<n; i++){
                klucze[i] = pisani.nextInt();	
            }
            
        }
        catch (InputMismatchException ex){
            System.out.println("zly typ danych");
            return;
        }
        catch (Exception ex){
	    System.out.println( "błąd "+ex.getClass() );
            return;
        }
        
        //dla jednego el sortowanie jest oczywiste	
	if(n==1){
	wyswietl(klucze);
	}
	
	if(n<40){	
	    czymaly = true;									
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
        }
							
	int[] zakonczone;
        zakonczone = quickSort(klucze,0, klucze.length-1);
        sprawdzenie(zakonczone);

    }
    
    //wyswietla wszystkie elementy tablicy
    static void wyswietl(int[] klucze){		
        for (int k : klucze) {			
    		System.out.print(k+" ");
        }
        System.out.println();
    }
    
    static void stan(){				
        System.out.println("Liczba porównań między kluczami" + porownania );
        System.out.println("Liczbę przestawień kluczy: "+ przestawienia);
    }
    
    static void sprawdzenie(int[] klucze){		
	if(czymaly){
            System.out.println("Tablica po posortowaniu:");
            wyswietl(klucze);
        }
        stan();

	//sprawdza posortowanie
        int poprzedni= klucze[0];			
        boolean uporzadkowany = true;
        for (int v : klucze) {			
            if(v<poprzedni){
                uporzadkowany = false;
                break;
            }
            poprzedni = v;
        }
        System.out.println( uporzadkowany? " Tablica jest uporządkowana" : "Tablica jest NIEuporządkowana");
    }
    
    
    static int[] quickSort(int[] kluczy, int dol, int gora){
        if (dol < gora){
            int r = partition(kluczy, dol, gora); //adres pivota
            
            if(czymaly){        
                wyswietl(kluczy);
            }
            
            quickSort(kluczy, dol, r-1); //te przed r
            quickSort(kluczy, r+1, gora); //te po r
        }
        return kluczy;
    }


    private static int partition(int[] kluczy, int dol, int gora){

        int pivot = kluczy[gora]; //bierze ostatni, ale szukajac dobrego miejsca, przeniesie wszystkie mniejsze na lewo
        int i = dol - 1; //do szukania miejsca pivota

        for(int j=dol; j<gora; j++){ 
            porownania++;
            
            if(kluczy[j]<= pivot ){
                i++;
                swap(kluczy, i, j); // "kluczy[i]" bedzie na pewno razem z malymi
                porownania++;
            }
        }
        //sprawdzone wszystkie punkty, wszsytkie male jak najbardziej w lewo
        swap(kluczy, i+1, gora); //pivot byl dalej na gorze, a musi byc zaraz za mniejszymi
        return (i+1); //adres pivota
    }

    private static void swap(int[] A, int i, int j){
        int x = A[i];
        A[i] = A[j];
        A[j] = x;
        
        przestawienia++;
    }
    
    // 1 5 10 13 6 3 2 8 11 4
}    
