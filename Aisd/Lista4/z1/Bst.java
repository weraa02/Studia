//zad1
import java.security.SecureRandom;

class TreeBst {
    int wezel;
    TreeBst lewy;
    TreeBst prawy;
    //bst wiec lewy <=wezel<prawy
    TreeBst(int korzen){
        this.wezel = korzen;
        lewy = null;
        prawy = null;
    }
}

class Searching {
    public TreeBst rodzic;
    public TreeBst dziecko;
    public Searching(int obiekt, TreeBst korzen){
        rodzic = null;
        dziecko = szukanie(obiekt, korzen);
    }

    TreeBst szukanie(int obiekt, TreeBst korzen){
        if(korzen == null){
            rodzic = null; //dla nastepnych szukan
            return null;
        }
        else if(obiekt == korzen.wezel){
            return korzen;
        }
        else if(obiekt <= korzen.wezel){
            rodzic = korzen;
            return szukanie(obiekt, korzen.lewy);
        }
        else{
            rodzic = korzen;
            return szukanie(obiekt, korzen.prawy);
        }
    }

}

public class Bst {

    static TreeBst korzenglowny;
    static SecureRandom losowe = new SecureRandom();
    static boolean printingEveryAction = true;


    /*Bst(){ //konstruktor, ale wszysko w main jest ogarnianie
        korzenglowny =null;
    }*/

    public static void main(String[] args) {
        korzenglowny = null;

        int n = 50;
        int[] tablica;

        //ROSNACY
        System.out.println("1.\nGenerowanie rosnacego ciagu:");
        tablica = rosnace(n);
        for(int i = 0; i<tablica.length; i++){
            insert(tablica[i]);
        }
        printTree();
        System.out.println("Usuniecie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            delete(tablica[i]);
        }
        printTree();

        //czyszczenie drzewa
        printingEveryAction = false;
        while (korzenglowny != null){
            delete(korzenglowny.wezel);
        }
        printTree();
        printingEveryAction = true;

        //LOSOWY
        System.out.println("2.\nGenerowanie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            insert(tablica[i]);
        }
        printTree();
        System.out.println("Usuniecie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            delete(tablica[i]);
        }
        printTree();
    }

    //wstawianie nowego wystąpienia klucza k do drzewa
    static void insert(int value) {
        if (printingEveryAction) {
            System.out.println("insert " + value);
            korzenglowny = insertOrd(value, korzenglowny);
            printTree();
            return;
        }
        korzenglowny = insertOrd(value, korzenglowny);
    }

    //szuka miejsca dla wartosci zgodnie z lewy<=wezel<prawy
    static TreeBst insertOrd(int obiekt, TreeBst korzen){
        if(korzen == null){
            return new TreeBst(obiekt);
        }
        //else nie potrzebne bo return;
        if(korzen.wezel >= obiekt){
            korzen.lewy = insertOrd(obiekt, korzen.lewy);
        }
        else{ //if(korzen.wezel < obiekt)
            korzen.prawy = insertOrd(obiekt, korzen.prawy);
        }
        return korzen;
    }
    
    //usuwanie jednego wystąpienia klucza k w drzewie (jeśli istnieje).
    static void delete(int value){
        Searching search = new Searching(value, korzenglowny); //klasa by przechowywac jednoczesnie obie dane
        if (printingEveryAction){
            System.out.println("delete " + value);
            deleteThis(search.dziecko, search.rodzic);
            printTree();
        }
        else {
            deleteThis(search.dziecko, search.rodzic);
        }
    }

    static void deleteThis(TreeBst element, TreeBst rodzic){
        if (korzenglowny == null){ //puste
            return;
        }
        //czy w ogole zmaleziono
        if(element == null){
            return;
        }
        
        //brak obu lisci
        if(element.lewy == null && element.prawy == null){
            if(rodzic == null) {  //brak rodzica == to korzen glowny
                korzenglowny = null;
            }//usuwanie wybranego el wzgledem rodzica
            else if(rodzic.lewy == element){
                rodzic.lewy = null;
            }
            else if(rodzic.prawy == element) {
                rodzic.prawy = null;
            }
        }//jeden lisc - prawy
        else if(element.lewy==null){
            //ustala ktory lisc dla rodzica by podac mu wszystkich potomków wybranego punktu
            if(rodzic == null){//brak rodzica
                korzenglowny = element.prawy;
            }
            else if(rodzic.lewy == element){
                rodzic.lewy = element.prawy;
            }
            else if(rodzic.prawy == element){
                rodzic.prawy = element.prawy;
            }
        }//tylko lewy lisc
        else if(element.prawy==null){

            if(rodzic == null){
                korzenglowny = element.lewy;
            }
            else if(rodzic.lewy == element){
                rodzic.lewy = element.lewy;
            }
            else if(rodzic.prawy==element){
                rodzic.prawy = element.lewy;
            }
        }//oba lisice
        else{
            //deleteThis(nast); //dostosowuje drzewo dalej
            element.wezel= nastepnik(element).wezel; //nowa wartosc, ale reszta polaczen tak samo
        }
    }

    //Szuka nastepnika, ktory ma sie pojawic na miejscu usuwanej wartosci w przypadku obu wartosci
    static TreeBst nastepnik(TreeBst korzen){
        //musi byc bo ma oba liscie
        TreeBst rodzic = korzen;
        korzen=korzen.prawy; //weikszy
        while(korzen.lewy != null){
            rodzic = korzen;
            korzen = korzen.lewy; //minimalnie
        }
        deleteThis(korzen,rodzic); //dostosowuje drzewo dalej
        return korzen;
    }
    
    //funkcja zwracająca bieżącą wysokość drzewa.
    public int height() {
        int wysokosc = glebokoscMax(korzenglowny);
        if(printingEveryAction) {
            System.out.println("height " + wysokosc);
        }
        return wysokosc;
    }

    private int glebokoscMax(TreeBst root) {
        if (root == null)
            return -1; //korzenglowny sie wyzeruje bo return ma +1
        else {
            // sprawdza kazde poddrzewo
            int leweDol = glebokoscMax(root.lewy);
            int praweDol = glebokoscMax(root.prawy);

            // wybierz najwieksze
            if (leweDol < praweDol)
                return (leweDol + 1);
            else
                return (praweDol + 1);
        }
    }

    public static void printTree() {
        TreeBst wezel = korzenglowny;
        if (wezel == null) {
            return; //nic nie wyswietlaj
        }
        StringBuilder sb = new StringBuilder();
        sb.append(wezel.wezel);

        String prawaSciezka = "└--";
        String lewaSciezka = (wezel.prawy != null) ? "├──" : "└──";

        printdlaWezel(sb, "", lewaSciezka, wezel.lewy, wezel.prawy != null);
        printdlaWezel(sb, "", prawaSciezka, wezel.prawy, false);

        System.out.println(sb.toString());
    }

    private static void printdlaWezel(StringBuilder sb, String padding, String doSciezki, TreeBst wezel, boolean czyDalejPrawy) {
        if (wezel != null) {
            sb.append("\n");
            sb.append(padding); //odstep
            sb.append(doSciezki);
            sb.append(wezel.wezel);//wartosc

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (czyDalejPrawy) {
                paddingBuilder.append("│  "); //bo prawy bedzie nizej
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String prawaSciezka = "└--";
            String lewaSciezka = (wezel.prawy != null) ? "├──" : "└──";

            printdlaWezel(sb, paddingForBoth, lewaSciezka, wezel.lewy, wezel.prawy != null);
            printdlaWezel(sb, paddingForBoth, prawaSciezka, wezel.prawy, false);
        }
    }

    //generatory
    static int[] losowe (int n) {

        int i=0;
        int[] klucz = new int [n];
        while(i<n){
            klucz[i] = losowe.nextInt(2*n-1);
            i++;
        }
        //System.out.println();
        return klucz;
    }

    static int[] rosnace(int n) {
        int[] klucz = new int [n];
        int a = losowe.nextInt(n-1);
        for(int i = 0; i<n;i++){
            klucz[i]=a+i;
        }
        return klucz;
    }

}
