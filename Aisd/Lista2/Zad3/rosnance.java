//Weronika Chmiela
//aisd lista 2 zad1

import java.security.SecureRandom;

public class rosnance {
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

        int klucz = losowe.nextInt(n-1);
        System.out.print(klucz+" ");
        for(int i = 1; i<n;i++){
            klucz++;
            System.out.print(klucz+" ");
        }
        System.out.println();
    }
}
