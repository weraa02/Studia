import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class RandSelPlik {
    static int przestawienia = 0;
    static int  porownania = 0;
    static boolean czymaly = false;
    static SecureRandom losowe = new SecureRandom();
    //zmienna globalna
    static int kk; //szukana statystyka pozycyjna
    static String nazwapliku = "dowykresówRandS.csv";

    public static void main(String[] args) {
        //Scanner pisani = new Scanner(System.in);

        //wejscie
        int n; //dlugosc tablicy
        int k; //szukana statystyka pozycyjna
        int[] klucze; //tablica

        k = 1; //3 rożne k
        while(k <=100){
            kk=k;
            try {
                FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                file.write("\nn ; porownania(c); przestawienia(s); c/n; s/n;\n");
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


                klucze = losowe(n); //mamy tabele do posortowania
                //ilosc powtorzen
                int m = 100;
                for(int i = 0; i< m; i++){
                    przestawienia = 0;
                    porownania = 0;


                    algorytm(klucze, n,k);

                    sreporown += porownania;
                    sreprzest += przestawienia;
                }

                sreporown = sreporown/ m;
                sreprzest = sreprzest/ m;

                ilorazS = sreprzest / n ;
                ilorazC = sreporown / n;
                try {
                    FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
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

        /*if(n<50){
            czymaly = true;
            System.out.println("Tablica wejściowa: ");
            wyswietl(klucze);
            System.out.println("");
        }*/

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
        //System.out.println("  Znaleziono: A[k]= "+k); //najpierw powinien sie wyswietlic rozwiazanie(k)
        //stan(); 

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
            //System.out.print(klucz[i]+" ");
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
