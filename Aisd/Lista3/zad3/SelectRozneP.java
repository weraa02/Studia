import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SelectRozneP {
    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    static SecureRandom losowe = new SecureRandom();

    static String nazwapliku = "dowykresówSelect.csv";

    //zmienna globalna
    static int kk; //index czego chcemy znalezc

    static int grupa = 1;


    public static void main(String[] args) {
        //Scanner pisani = new Scanner(System.in);

        //wejscie
        int n=0; //dlugosc tablicy
        int k; //szukana statystyka pozycyjna
        int[] klucze; //tablica

        int[] grupowanie = {2,5,7,9};



        //k = 1; //3 rożne k
        for (int g : grupowanie) {
            grupa = g;


            k= 50;
            kk=k;

            nazwapliku = grupa+"dowykresówSelect.csv";
            try {

                FileWriter file = new FileWriter( nazwapliku, true); //dopisywanie

                file.write(grupa+"\nn ; porownania(c); przestawienia(s); c/n; s/n; czas;\n");
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            //wielkosc ciagu
            n=100;
            while(n<=10000){
                double sreprzest = 0;
                double sreporown = 0;

                double ilorazC = 0;
                double ilorazS = 0;

                long sredCzas = 0;

                klucze = losowe(n); //mamy tabele do posortowania
                //ilosc powtorzen
                int m = 100;

                for(int i = 0; i< m; i++){
                    przestawienia = 0;
                    porownania = 0;

                    //do liczenia czasu
                    long startTime = System.nanoTime();

                    algorytm(klucze, n,k);

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


                n +=100;
            }
            k *= 10;
        }

    }

    static void algorytm(int[] klucze, int n, int k){

        //dla jednego el sortowanie jest oczywiste
        if(n==1){
            wyswietl(klucze);
        }

        /*f(n<50){
            czymaly = true;
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
            System.out.println("");
        }*/

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
        //System.out.println("Liczba porównań między kluczami: " + porownania );
        //System.out.println("Liczbę przestawień kluczy: "+ przestawienia);
    }

    static void sprawdzenie(int[] klucze, int k){
        //static void sprawdzenie(int[] klucze, int k){
        //System.out.println("  Znaleziono: A[k]= "+k); //najpierw powinien sie wyswietlic rozwiazanie(k)
        //stan(); //zawsze

        if(czymaly){
            //System.out.println("Tablica po znalezeoniu k:");
            //wyswietl(klucze);

            //System.out.println("Posortowane klucze: ");
            Arrays.sort(klucze);
            //wyswietl(klucze,0,kk-1);//przed kk
            //System.out.print(" k:"+ klucze[kk-1]+" ");
            //wyswietl(klucze,kk, klucze.length);
        }
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

    private static int select(int[] kluczy, int dol, int gora, int i){
        if(i > 0 && i <= gora - dol + 1){
            int n = gora - dol + 1; //tyle el rozwazamy

            int a; //do podzialu
            int[] mediany;
            if(n%grupa == 0){
                mediany = new int[n/grupa];
            }
            else{
                mediany = new int[(n/grupa)+1]; //dla niepelnej 5
            }
            //bez ostatniej bo moze byc pusta ; a bedzie liczyc ilosc grup
            for(a = 0; a < n/grupa; a++){
                Arrays.sort(kluczy, a*grupa + dol, a*grupa + grupa + dol);

                mediany[a] = kluczy[a*grupa+dol+(grupa/2)];
            }
            if(a * grupa < n){
                Arrays.sort(kluczy, a*grupa + dol, a*grupa + dol + (n%grupa));
                mediany[a] = kluczy[a*grupa + dol +((n%grupa)/2)];
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
