//Weronika Chmiela
//aisd lista 2 zad1
//MergeSort

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MergeSort {

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
        zakonczone = mergeSort(klucze);
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
    
    
    static int[] mergeSort(int[] sprawdzane){
        if(sprawdzane.length == 1){
            return sprawdzane; //oczywiste
        }
        else {
            int[] lewe = new int[(sprawdzane.length)/2];
            int[] prawe = new int[(sprawdzane.length)- lewe.length]; //ze reszta

            System.arraycopy(sprawdzane,0, lewe,0,lewe.length);
            System.arraycopy(sprawdzane,(sprawdzane.length)/2, prawe,0,prawe.length);

            int[] mniejsze = mergeSort(lewe);
            int[] wieksze = mergeSort(prawe);

            return merge(mniejsze,wieksze);
        }
    }

    private static int[] merge(int[] B, int[] C){
        int b=0,c =0, i=0;
        int[] scalony = new int[B.length + C.length];
        while(b <B.length && c<C.length){
            if(B[b] < C[c]){
                scalony[i] = B[b];
                b++;
            }
            else {
                scalony[i] = C[c];
                c++;
            }
            i++; //uzupelniamy nastepne
            
            porownania++;
            porownania++;
            przestawienia++;
        }
        while (b <B.length){
            scalony[i] = B[b];
            b++;
            i++;
            
            porownania++;
            przestawienia++;
        }
        while (c< C.length){
            scalony[i] = C[c];
            c++;
            i++;
            
            porownania++;
            przestawienia++;
        }
        if(czymaly){
            wyswietl(scalony);
        }
        
        return scalony;
    }
    // 8 3 1 4 2 6 5 7
    //3 7 12 14 22 1 2 8 9 10
}
