import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Binarne {
    static int przestawienia = 0;
    static int  porownania = 0;
    static SecureRandom losowe = new SecureRandom();

    static String nazwapliku = "bianrne.csv";

    public static void main(String[] args) {
        //wpisywanie
        Scanner pisani = new Scanner(System.in); //zeby z klawiatury

        //wejscie
        int n; //dlugosc tablicy
        int k; //szukana statystyka pozycyjna
        int[] klucze; //tablica

        /*
        //Pobieranie z wejscia
        try{
            System.out.println("Podaj dlugosc tablicy:");
            n = pisani.nextInt();

            System.out.println("Podaj szukana wartosc:");
            k = pisani.nextInt();

            System.out.println("Podaj pososrotwana tablicy:");

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
        */

        ///*


        k = 1; //3 rożne k
        while(k <=1000){
            try {
                FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                file.write(k+" :D;");
                file.write("\nn ; porownania(c); przestawienia(s); c/n; s/n; czas;\n");
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            int v= 0;
            //wielkosc ciagu
            n=1000;
            while(n<=10000){


                double sreprzest = 0;
                double sreporown = 0;

                double ilorazC = 0;
                double ilorazS = 0;

                long sredCzas = 0;

                klucze = losowe(n); //mamy tabele do posortowania
                //ilosc powtorzen
                int m = 50;
                for(int i = 0; i< m; i++){
                    przestawienia = 0;
                    porownania = 0;

                    if(k ==1 ){
                        v=n/5; //w miare blisko
                    }
                    if(k==10){
                        v=n/2;
                    }
                    if(n==100){
                        v=4*n/5;
                    }
                    if(n==1000){
                        v= losowe.nextInt(n);
                    }

                    long startTime = System.nanoTime();

                    algorytmGenerowanie(n,k);

                    long endTime = System.nanoTime();
                    long taken = endTime - startTime;
                    sredCzas += taken;

                    sreporown += porownania;
                    sreprzest += przestawienia;
                }

                sreporown = sreporown/ m;
                sreprzest = sreprzest/ m;
                sredCzas = sredCzas/m;

                ilorazS = sreprzest / n ;
                ilorazC = sreporown / n;
                try {
                    FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                    file.append(Integer.toString(n)).append(";");
                    file.append(Double.toString(sreporown)).append(";");
                    file.append(Double.toString(sreprzest)).append(";");
                    file.append(Double.toString(ilorazC)).append(";");
                    file.append(Double.toString(ilorazS)).append(";");
                    file.append(Long.toString(sredCzas)).append(";\n");

                    file.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }


                n +=1000;
            }
            k *= 10;
        }

    }

    static void algorytm(int[] klucze, int n, int k){

        //dla jednego el sortowanie jest oczywiste
        if(n==1){
            wyswietl(klucze);
        }

        int[] tablica = losowe(n);

        int poprzedni= klucze[0];
        for (int v : klucze) {
            if(v<poprzedni){
                klucze = quickSort(tablica,0, tablica.length-1);
                break;
            }
            poprzedni = v;
        }
        int odp = binaryS(tablica, k, 0, tablica.length-1);
        sprawdzenie(odp);

    }

    static void algorytmGenerowanie(int ntablicy, int k) {
        int[] tablica = losowe(ntablicy);
        quickSort(tablica, 0, tablica.length - 1);
        int odp = binaryS(tablica, k, 0, tablica.length-1);
        sprawdzenie(odp);
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

    static void sprawdzenie(int k){
        /*if(k ==1 ){
            System.out.println("1 - znaleziono ");
        }
        else System.out.println("0 - nie znaleziono ");*/
        //stan(); //zawsze
    }

    static int binaryS(int[] posortowane, int wartosc, int pocz, int koniec){

        if(koniec >= pocz){
            if(pocz == koniec){
                return 1;
            }

            int srodek = pocz + (koniec - pocz) / 2;

            if(posortowane[srodek] == wartosc) {
                porownania++;
                return 1;
            }
            else if(posortowane[srodek] > wartosc) {
                porownania++;
                return binaryS(posortowane,wartosc, 0, srodek-1);
            }
            else {
                porownania++;
                return binaryS(posortowane, wartosc, srodek + 1, koniec);
            }
        }
        return 0;
    }

//z lsity 2
    static int[] quickSort(int[] kluczy, int dol, int gora){
        if (dol < gora){
            int r = partition(kluczy, dol, gora); //adres pivota

            quickSort(kluczy, dol, r-1); //te przed r
            quickSort(kluczy, r+1, gora); //te po r
        }
        return kluczy;
    }


    private static int partition(int[] kluczy, int dol, int gora){

        int pivot = kluczy[gora]; //bierze ostatni, ale szukajac dobrego miejsca, przeniesie wszystkie mniejsze na lewo
        int i = dol - 1; //do szukania miejsca pivota

        for(int j=dol; j<gora; j++){
            //porownania++;

            if(kluczy[j]<= pivot ){
                i++;
                swap(kluczy, i, j); // "kluczy[i]" bedzie na pewno razem z malymi
                //porownania++;
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

        //przestawienia++;
    }

    static int[] losowe (int n) {
        //SecureRandom losowe = new SecureRandom();
        int i=0;
        int[] klucz = new int [n];
        while(i<n){
            klucz[i] = losowe.nextInt(2*n-1);
            //System.out.print(klucz[i]+" ");
            i++;
        }
        //System.out.println();
        return klucz;
    }
}

