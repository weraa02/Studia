//Weronika Chmiela
//aisd lista 2 zad2
//insertion Sort - po przerobce - zastapienie pobieranie danych z stand wejscia -> wklejenie losowe.java jako metode, dodano drukowanie do pliku i zakomentowano zbedne fragmenty kodu (głownie te drukujace do terminalu)


import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.SecureRandom;
import java.io.FileWriter;
import java.io.IOException;

public class InsertionSort {

    static int przestawienia = 0;
    static int  porownania = 0;
    
    public static void main(String[] args) {
        //Scanner pisani = new Scanner(System.in); //zeby z klawiatury


        int n; //dlugosc tablicy			
        //int[] klucze; //tablica		

	int k=1;
	while(k<=100){
		
	
            	try {
                	FileWriter file = new FileWriter("dowykresówInsertion.csv", true); //dopisywanie
               	file.write("\nn ; porownania(c); przestawienia(s); c/n; s/n;\n");
                	file.close();
            	}
            	catch (IOException e) {
                	e.printStackTrace();
                	return;
            	}
		
		//wielkosc ciagu
            	for(n=10; n <= 200; n += 10){
            		double sreprzest = 0;
		        double sreporown = 0;

		        double ilorazC = 0;
		        double ilorazS = 0;
		        
		        //ilosc powtorzen
                	for(int i=0; i<k; i++){
                		przestawienia = 0;
                    		porownania = 0;
                    		
                    		int[] klucze = losowe(n); //mamy tabele do posortowania
                    		sortowanie(n, klucze);
                    		
                    		sreporown += porownania;
                    		sreprzest += przestawienia;
                	}
                	
                	sreporown = sreporown/k;
		        sreprzest = sreprzest/k;

		        ilorazS = sreprzest / n ;
		        ilorazC = sreporown / n;
		        
		        try {
		            FileWriter file = new FileWriter("dowykresówInsertion.csv", true); //dopisywanie
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
            	}
            	k *= 10;
	}

    }
    
    static void sortowanie(int n, int[] klucze){
    	//dla jednego el sortowanie jest oczywiste	
	if(n==1){
	wyswietl(klucze);
	}
	
	/*if(n<40){					
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
        }*/
							
	int[] zakonczone;
        zakonczone = insertionSort(n, klucze);
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

            /*if(n<40){
                wyswietl(klucze);
            }*/
        }
        return(klucze);				
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
    // 7 3 4 8 2 5 	-przyklad z zeszytu 
}    
