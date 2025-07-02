//Weronika Chmiela
//aisd lista 2 zad3
//Dual quick Sort

import java.util.InputMismatchException;
import java.util.Scanner;

public class DualQuickSort {
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
        zakonczone = dualquickSort(klucze,0, klucze.length-1);
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
        System.out.println("Liczba porównań między kluczami " + porownania );
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
    
    private static int[] dualquickSort(int[] kluczy, int dol, int gora) {
        if (dol < gora){
            int[] p = new int[2]; //miejsca pivotów

            p = dualPartition(kluczy, dol, gora);
            if(czymaly){
                wyswietl(kluczy);
            }
            dualquickSort(kluczy, dol, p[0]-1); //te przed
            if(p[0] != p[1]) { //jesli sa takie same to nie ma co sortowac
                dualquickSort(kluczy, p[0] + 1, p[1] - 1); //te po srodku
            }
            dualquickSort(kluczy, p[1]+1, gora); // ostatnie
        }
        return kluczy;
    }

    static int[] dualPartition(int[] kluczy, int dol, int gora){

        //ustalanie pivotów
        if(kluczy[dol]>kluczy[gora]){
            swap(kluczy,dol,gora);
            porownania++;
        }
        int p = kluczy[dol];
        int q = kluczy[gora];

        int s = 0; //ile najmneijszych
        int l = 0; //ile najwiekszych;

        int i = dol + 1; //sprawdzany

        while(i<=gora-l-1) { //nasz maks - te co sa juz sprawdzone - pivot
            porownania++;
            //kolejnosc porownan
            if (l > s) {
                if (kluczy[i] < q) {
                    porownania++;
                    if (kluczy[i] <= p) { //mneijsza od q i od p
                        swap(kluczy, i, dol+s+1); //zamien sprawdzany i p
                        s++;
                        porownania++;
                    }
                    i++; //dla srodkowych i mneijszych, wieksze, ponownie
                } else {    //wieksza od q
                    porownania++;
                    swap(kluczy, i, gora-l-1);
                    l++;
                }
            } else {
                if (kluczy[i] > p) {
                    porownania++;
                    if (kluczy[i] >= q) { //najwieksze
                        swap(kluczy, i, gora-l-1);
                        l++;
                        porownania++;
                    }
                    else {
                        i++;    //dla nie najwiekszych
                    }
                    //  po srodku
                } else { //najmniejsze
                    porownania++;
                    swap(kluczy, i, dol+s+1); //zamien sprawdzany oraz p
                    s++;
                    i++;
                }

            }
        }
        //wyciaganie pivotow na odpoweidnie meijsca
        swap(kluczy, dol+s, dol);
        swap(kluczy, gora-l, gora);

        return new int[]{dol + s, gora - l}; //adresy pivotow
    }
    
     private static void swap(int[] A, int i, int j){
        int x = A[i];
        A[i] = A[j];
        A[j] = x;

        przestawienia++;
    }

    // 1 5 10 13 6 3 2 8 11 4
}
