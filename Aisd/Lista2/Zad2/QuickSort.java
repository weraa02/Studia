//Weronika Chmiela
//aisd lista 2 zad2
//quick Sort


import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.SecureRandom;
import java.io.FileWriter;
import java.io.IOException;

public class QuickSort {

    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    
    public static void main(String[] args) {
        //Scanner pisani = new Scanner(System.in); 

        int n; //dlugosc tablicy
        //int[] klucze; //tablica

	int k=1;
	while(k<=100){
		try {
                	FileWriter file = new FileWriter("dowykresówQuick.csv", true); //dopisywanie
               	file.write("\nn ; porownania(c); przestawienia(s); c/n; s/n;\n");
                	file.close();
            	}
            	catch (IOException e) {
                	e.printStackTrace();
                	return;
            	}
            	
            	//wielkosc ciagu
            	n=10;
            	while(n<=200 || n<=20000){
            		double sreprzest = 0;
		        double sreporown = 0;

		        double ilorazC = 0;
		        double ilorazS = 0;
		        
		        //ilosc powtorzen
                	for(int i=0; i<k; i++){
                		przestawienia = 0;
                    		porownania = 0;
                    		
                    		int[] klucze = losowe(n); //mamy tabele do posortowania
                    		sortowanie(klucze);
                    		
                    		sreporown += porownania;
                    		sreprzest += przestawienia;
                	}
                	
                	sreporown = sreporown/k;
		        sreprzest = sreprzest/k;

		        ilorazS = sreprzest / n ;
		        ilorazC = sreporown / n;
			try {
		            FileWriter file = new FileWriter("dowykresówQuick.csv", true); //dopisywanie
		            file.append(Integer.toString(n)).append(";");
		            file.append(Double.toString(sreporown)).append(";");
		            file.append(Double.toString(sreprzest)).append(";");
		            file.append(Double.toString(ilorazC)).append(";");
		            file.append(Double.toString(ilorazS)).append(";\n");

		            file.close();
		        }
		        catch (IOException e) {
                    		e.printStackTrace();
                    		return;
                	}
                	
                	if(n<200){
            			n +=10;
            		}
            		else if (200 == n){
            			n=1000;
            		}
            		else{
            			n += 1000;
            		}
            	}
            	k *= 10;
	}

     }
     
     static void sortowanie(int[] klucze){ 
        
        //dla jednego el sortowanie jest oczywiste	
	/*if(n==1){
	wyswietl(klucze);
	}
	
	if(n<40){	
	    czymaly = true;									
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
        }*/
							
	int[] zakonczone;
        zakonczone = quickSort(klucze,0, klucze.length-1);
        //sprawdzenie(zakonczone);

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
    
    static int[] losowe (int n) {
 	/*int n;
    	try{
            n = Integer.parseInt(args[0]);
            System.out.println(n);
        }
        catch (Exception ex){
            System.out.println( "błąd "+ex.getClass() );
            return;
        }*/
    
        SecureRandom losowe = new SecureRandom();
        int i=0;
        int[] klucz = new int [n];
        while(i<n){
            //klucz = new int [n]; //nowe
            klucz[i] = losowe.nextInt(2*n-1);
            //System.out.print(klucz+" ");
            i++;
        }
        //System.out.println();
        return klucz;
    }
    // 1 5 10 13 6 3 2 8 11 4
}    
