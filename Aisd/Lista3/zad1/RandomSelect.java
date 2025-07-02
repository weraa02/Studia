//Weronika Chmiela
//aisd lista 3 zad1

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
//8 7 6 10 13 5 8 3 2 11
public class RandomSelect {
    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    static SecureRandom losowe = new SecureRandom();
    //zmienna globalna
    static int kk; //szukana statystyka pozycyjna


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

        int statystyka = randSelect(klucze, 0, klucze.length-1, k);
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

    static int randSelect(int[] kluczy, int dol, int gora, int k){
        if(dol == gora) //bo oczywiste
            return kluczy[dol];
        int r = randPartition(kluczy, dol, gora); //do pivota
        int i = r - dol +1; //lokalny index pivota

        //stan posredni
        if (czymaly) {
            wyswietl(kluczy);
        }

        if(i == k){
            return kluczy[r];
        }
        else if(i < k){
            return randSelect(kluczy, r+1, gora, k-i);
        }
        else{
            return randSelect(kluczy, dol, r-1, k);
        }
    }

    private static int randPartition(int[] kluczy, int dol, int gora){
        int a = losowe.nextInt(gora-dol+1)+dol;
        swap(kluczy, a,gora);//podstawi wylosowany element i mozna normalnie Lomuto
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

