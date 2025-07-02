//Weronika Chmiela
//aisd lista 2 zad1

import java.security.SecureRandom;

public class losowe {
    public static void main(String[] args) {
 	int n;
    	try{
            n = Integer.parseInt(args[0]);
            System.out.println(n);
        }
        catch (Exception ex){
            System.out.println( "błąd "+ex.getClass() );
            return;
        }
    
        SecureRandom losowe = new SecureRandom();
        int i=0;
        while(i<n){
            int klucz;
            klucz = losowe.nextInt(2*n-1);
            System.out.print(klucz+" ");
            i++;
        }
        System.out.println();
    }
}
