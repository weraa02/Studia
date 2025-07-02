//Weronika Chmiela
//aisd lista 2 zad1
//insertion Sort

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InsertionSort {

    static int przestawienia = 0;
    static int  porownania = 0;
    
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
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
        }
							
	int[] zakonczone;
        zakonczone = insertionSort(n, klucze);
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
	if(klucze.length<40){
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
    
    static int[] insertionSort(int n, int[] klucze){	

        for(int j=1; j<n; j++){
            int klucz = klucze[j];			
            int i = j-1;    //musi bo warunek for
            while (i>=0 && klucze[i] > klucz){
                klucze[i+1]  = klucze[i];
                i--;
                
                przestawienia++;			
                porownania++;
            }
            klucze[i+1] = klucz;
            
            porownania++;				

            if(n<40){
                wyswietl(klucze);
            }
        }
        return(klucze);				
    }   
    // 7 3 4 8 2 5 	-przyklad z zeszytu 
}    
