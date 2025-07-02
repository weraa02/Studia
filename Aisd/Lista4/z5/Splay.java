//jak robimy cos to rpzesuwamy korzen nie nas
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;

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


public class Splay {
    static TreeBst korzenglowny;
    static SecureRandom losowe = new SecureRandom();
    static boolean printingEveryAction = true;
    //static String nazwapliku = "dowykresówSplay.csv";
    static int  porownania = 0;
    static int odczyt = 0; //odczyt i podstaweinia
    static int wysokosc = 0;

    public static void main(String[] args) {
        korzenglowny = null;
        int n = 50;
        int[] tablica;

        System.out.println("1.\nGenerowanie rosnacego ciagu:");
        tablica = rosnace(n);
        for(int i = 0; i<tablica.length; i++){
            insert(tablica[i]);
        }
        System.out.println("Usuniecie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            delete(tablica[i]);
        }
        printTree();

        //czyszczenie
        printingEveryAction = false;
        while (korzenglowny != null){
            delete(korzenglowny.wezel);
        }
        printTree();
        printingEveryAction = true;

        System.out.println("2.\nGenerowanie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            insert(tablica[i]);
        }
        //System.out.println("Usuniecie losowego ciagu:");
        tablica = losowe(n);
        for(int i = 0; i<tablica.length; i++){
            delete(tablica[i]);
        }
    }

    //przesuwanie klucza 'na gore'
    static TreeBst doSplay(TreeBst current, int value) {
        //jak podstawa to chcielismy
        if (current == null || current.wezel == value) {

            return current;
        }

        if (current.wezel > value){
            if( current.lewy == null ){
                return current;
            }
            porownania++;
            // Zig-Zig: lewy syn i lewy wnuk
            if(current.lewy.wezel > value) {
                porownania++;
                current.lewy.lewy = doSplay(current.lewy.lewy, value);//wnuk
                odczyt++;
                current = rotatePrawy(current);
            }
            // Zig-Zag: lewy syn i prawy wnuk
            else if (current.lewy.wezel < value) {
                porownania++;
                current.lewy.prawy = doSplay(current.lewy.prawy, value);
                odczyt++;
                if (current.lewy.prawy != null)
                    current.lewy = rotateLewy(current.lewy);
            }
            if(current.lewy == null){
                return current;
            }
            return rotatePrawy(current);
        }
        else {
            if (current.prawy == null) {
                return current;
            }
            porownania++;
            if (current.prawy.wezel > value) {
                porownania++;
                current.prawy.lewy = doSplay(current.prawy.lewy, value);
                odczyt++;
                if (current.prawy.lewy != null) {
                    current.prawy = rotatePrawy(current.prawy);
                }
            }
            else if (current.prawy.wezel < value) {
                porownania++;
                current.prawy.prawy = doSplay(current.prawy.prawy, value);
                odczyt++;
                current = rotateLewy(current);
            }
            if(current.prawy == null){
                return current;
            }
            return rotateLewy(current);
        }
    }

    //wezel x staje sie prawym lisciem czegos
    public static TreeBst rotatePrawy(TreeBst x) {
        TreeBst y = x.lewy;
        odczyt++;
        x.lewy = y.prawy; //==x.lewy.prawy
        odczyt++;
        y.prawy = x;
        odczyt++;
        return y;
    }

    public static TreeBst rotateLewy(TreeBst x) {
        TreeBst y = x.prawy;
        odczyt++;
        x.prawy = y.lewy;
        odczyt++;
        y.lewy = x;
        odczyt++;
        return y;
    }

    //wstawianie nowego wystąpienia klucza k do drzewa,
    static void insert(int value) {
        if (printingEveryAction) {
            System.out.println("insert " + value);
        }
        //wezel z dana wartoscia
        TreeBst newNode = new TreeBst(value);
        //pierwszy element
        if (korzenglowny == null) {
            odczyt++;
            korzenglowny = newNode;
            if (printingEveryAction) {
                printTree();
            }
            return;
        }

        korzenglowny = doSplay(korzenglowny, value);
        if (value < korzenglowny.wezel) {
            porownania++;
            newNode.lewy = korzenglowny.lewy;
            odczyt++;
            newNode.prawy = korzenglowny;
            odczyt++;
            korzenglowny.lewy = null;
            korzenglowny = newNode;
            odczyt++;
        } else if (value > korzenglowny.wezel) {
            porownania++;
            newNode.prawy = korzenglowny.prawy;
            odczyt++;
            newNode.lewy = korzenglowny;
            odczyt++;
            korzenglowny.prawy = null;
            korzenglowny = newNode;
            odczyt++;
        }
        //else { // Value already exists in the tree}

        if (printingEveryAction) {
            printTree();
        }

    }

    static void delete(int value){
        if (korzenglowny == null){
            return;
        }
        korzenglowny = doSplay(korzenglowny, value);
        odczyt++;
        if(value != korzenglowny.wezel){
            return;
        }
        //nie ma lewego
        if(korzenglowny.lewy == null){
            korzenglowny = korzenglowny.prawy;
        }
        else {
            TreeBst element = korzenglowny;
            korzenglowny = doSplay(korzenglowny.lewy, value);
            odczyt++;
            korzenglowny.prawy =  element.prawy;
        }
        odczyt++;

        if (printingEveryAction){
            System.out.println("delete " + value);
            printTree();
        }
    }

    public static int height() {
        int height = 0;
        //puste
        if (korzenglowny == null) {
            return height;
        }

        Queue<TreeBst> queue = new LinkedList<>();
        queue.offer(korzenglowny);

        while (!queue.isEmpty()) { //dopoki nie omowione wszystkie
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeBst current = queue.poll();//pierwszy el
                //jesli lewy potomek to dodaj
                if (current.lewy != null) {
                    queue.offer(current.lewy);
                }
                //prawy dodaj
                if (current.prawy != null) {
                    queue.offer(current.prawy);
                }
            }
            height++;
        }
        if(printingEveryAction) {
            System.out.println("height " + height);
        }
        return height;
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
