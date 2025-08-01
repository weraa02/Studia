import java.security.SecureRandom;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Select {
    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    static SecureRandom losowe = new SecureRandom();

    //zmienna globalna
    static int kk; //index czego chcemy znalezc

    public static void main(String[] args) {
        Scanner pisani = new Scanner(System.in); //zeby z klawiatury

        //wejscie
        int n; //dlugosc tablicy
        int k; //szukana statystyka pozycyjna
        int[] klucze; //tablica

        //Pobieranie z wejscia
        try{
            System.out.println("Podaj długość tablicy:");
            n = pisani.nextInt();

            System.out.println("Podaj szukana statystyke pozycyjna:");
            k = pisani.nextInt();
            kk = k;
            if(k<1 || k>n) throw new IllegalArgumentException();

            System.out.println("Podaj: elementy tablicy:");

            klucze = new int[n];
            for(int i=0; i<n; i++){
                klucze[i] = pisani.nextInt();
            }

        }        
        catch (InputMismatchException ex){
            System.out.println("zly typ danych");
            return;
        }
        catch(IllegalArgumentException ex){
            System.out.println("Dana musi być w przedziale 1 - n");
            return;
        }
        catch (Exception ex){
            System.out.println( "błąd "+ex.getClass() );
            return;
        }
        algorytm(klucze,n,k);
    }

    static void algorytm(int[] klucze, int n, int k){

        //dla jednego el sortowanie jest oczywiste
        if(n==1){
            wyswietl(klucze);
        }

        if(n<50){
            czymaly = true;
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
            System.out.println("");
        }

        int statystyka = select(klucze, 0, klucze.length-1, k);
        sprawdzenie(klucze, statystyka);

    }

    //wyswietla wszystkie elementy tablicy
    static void wyswietl(int[] klucze) {
        wyswietl(klucze,0,klucze.length);
        System.out.println();

    }
    //wyswietla czesc tablicy
    static void wyswietl(int[] klucze, int zaczyna, int dlugosc){
        for (int k = zaczyna; k< dlugosc; k++) {
            System.out.print(klucze[k]+" ");
        }
    }

    static void stan(){
        System.out.println("Liczba porównań między kluczami: " + porownania );
        System.out.println("Liczbę przestawień kluczy: "+ przestawienia);
    }

    static void sprawdzenie(int[] klucze, int k){
        //static void sprawdzenie(int[] klucze, int k){
        System.out.println("  Znaleziono: A[k]= "+k); //najpierw powinien sie wyswietlic rozwiazanie(k)
        stan(); //zawsze

        if(czymaly){
            System.out.println("Tablica po znalezeoniu k:");
            wyswietl(klucze);

            System.out.println("Posortowane klucze: ");
            Arrays.sort(klucze);
            wyswietl(klucze,0,kk-1);//przed kk
            System.out.print(" k:"+ klucze[kk-1]+" ");
            wyswietl(klucze,kk, klucze.length);
        }
    }

    static int[] losowe (int n) {
        SecureRandom losowe = new SecureRandom();
        int i=0;
        int[] klucz = new int [n];
        while(i<n){
            klucz[i] = losowe.nextInt(2*n-1);
            System.out.print(klucz[i]+" ");
            i++;
        }
        //System.out.println();
        return klucz;
    }

    private static int select(int[] kluczy, int dol, int gora, int i){
        if(i > 0 && i <= gora - dol + 1){
            int n = gora - dol + 1; //tyle el rozwazamy

            int a; //do podzialu
            int[] mediany;
            if(n%5 == 0){
                mediany = new int[n/5];
            }
            else{
                mediany = new int[(n/5)+1]; //dla niepelnej 5
            }
            //bez ostatniej bo moze byc pusta ; a bedzie liczyc ilosc grup
            for(a = 0; a < n/5; a++){
                Arrays.sort(kluczy, a*5 + dol, a*5 + 5 + dol);

                mediany[a] = kluczy[a*5+dol+2];
            }
            if(a * 5 < n){
                Arrays.sort(kluczy, a*5 + dol, a*5 + dol + (n%5));
                mediany[a] = kluczy[a*5 + dol +((n%5)/2)];
                a++;
            }

            int nadMediana; //mediana median

            if(a == 1)
                nadMediana = mediany[0];
            else
                nadMediana = select(mediany, 0, a - 1, (a/2)+1);

            int k = partition(kluczy, dol, gora, nadMediana);

            if(czymaly){
                wyswietl(kluczy);
            }

            if( i == k - dol + 1 ){
                return nadMediana; //==kluczy[k]
            }
            else if(k - dol > i - 1){
                return select(kluczy, dol, k - 1, i);
            }
            else{
                return select(kluczy, k +1 , gora, i - k + dol -1);
            }
        }
        return -1; //jako blad
    }
    private static int partition(int[] kluczy, int dol, int gora, int x){
        //szukamy jego lokalizacji
        int a;
        for(a = 0; a<gora; a++){
            if(x==kluczy[a]){
                porownania++;
                swap(kluczy, a, gora);
                break;
            }
        }
        //Lomuto Partition
        int pivot = kluczy[gora]; //bierze ostatni, ale szukajac dobrego miejsca, przeniesie wszystkie mniejsze na lewo
        int i = dol - 1; //do szukania miejsca pivota

        for(int j=dol; j<gora; j++){
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

}

