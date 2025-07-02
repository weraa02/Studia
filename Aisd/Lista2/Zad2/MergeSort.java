//Weronika Chmiela
//aisd lista 2 zad2
//MergeSort


import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.SecureRandom;
import java.io.FileWriter;
import java.io.IOException;

public class MergeSort {

    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;

    
    public static void main(String[] args) {
        //Scanner pisani = new Scanner(System.in); //zeby z klawiatury

        int n; //dlugosc tablicy
        //int[] klucze; //tablica

	int k=1;
	while(k<=100){
		try {
                	FileWriter file = new FileWriter("dowykresówMerge.csv", true); //dopisywanie
               	file.write("\nn ; porownania(c); przestawienia(s); c/n; s/n;\n");
                	file.close();
            	}
            	catch (IOException e) {
                	e.printStackTrace();
                	return;
            	}
            	
            	//wielkosc ciagu
            	//for(n=10; n <= 200; n += 10){
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
                    		sortowanie(n, klucze);
                    		
                    		sreporown += porownania;
                    		sreprzest += przestawienia;
                	}
                	
                	sreporown = sreporown/k;
		        sreprzest = sreprzest/k;

		        ilorazS = sreprzest / n ;
		        ilorazC = sreporown / n;
			
		        try {
		            FileWriter file = new FileWriter("dowykresówMerge.csv", true); //dopisywanie
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
     
     static void sortowanie(int n, int[] klucze){
        //dla jednego el sortowanie jest oczywiste	
	if(n==1){
	wyswietl(klucze);
	}
	
	/*if(n<40){
	    czymaly = true;					
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
        }*/
							
	int[] zakonczone;
        zakonczone = mergeSort(klucze);
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
}
