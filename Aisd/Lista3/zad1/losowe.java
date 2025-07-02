//Weronika Chmiela
//aisd lista 3 zad1, na podstawie programu z listy 2        //ready

import java.security.SecureRandom;

public class losowe {
    public static void main(String[] args) {
        int n,k;
        try{
            n = Integer.parseInt(args[0]);
            System.out.println(n);
            //
            k = Integer.parseInt(args[1]);
            System.out.println(k);
        }
        catch (Exception ex){
            System.out.println( "błąd "+ex.getClass() );
            return;
        }

        SecureRandom losowe = new SecureRandom();
        int i=0;
        int[] klucz = new int [n];
        while(i<n){
            klucz[i] = losowe.nextInt(2*n-1);
            System.out.print(klucz[i]+" ");
            i++;
        }
        System.out.println();

        //return klucz; //dla wersji do pliku - wtedy nei drukuj tez klucza
    }
}

