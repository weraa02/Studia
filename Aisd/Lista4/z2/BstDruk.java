//zad2
import java.io.FileWriter;
import java.io.IOException;
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

class Searching {
    public TreeBst rodzic;
    public TreeBst dziecko;
    public int porownania = 0;
    public int odczyt = 0;

    public Searching(int obiekt, TreeBst korzen) {
        porownania = 0;
        odczyt = 0;
        rodzic = null;
        dziecko = szukanie(obiekt, korzen);
    }

    TreeBst szukanie(int obiekt, TreeBst korzen) {
        rodzic = null;
        TreeBst current = korzen;

        while (current != null) {
            if (obiekt == current.wezel) {
                odczyt++;
                return current;
            } else if (obiekt <= current.wezel) {
                porownania++;
                rodzic = current;
                current = current.lewy;
            } else {
                porownania++;
                rodzic = current;
                current = current.prawy;
            }
        }
        return null; // nie znaleziono
    }
}
class BstTesty {
    static TreeBst korzenglowny;
    static SecureRandom losowe = new SecureRandom();
    static boolean printingEveryAction = true;
    static String nazwapliku = "dowykresówBST.csv";
    static int  porownania = 0;
    static int odczyt = 0; //odczyt i podstaweinia
    static int wysokosc = 0;

    /*
    //konstruktor, ale wszysko w main
    Bst(){
        korzenglowny =null;
    }*/

    public static void main(String[] args) {
        korzenglowny = null;

        printingEveryAction = false; //zeby sie nic niepotrzebnego nei wyswietlało

        int[] tablica;
        int n=0;
        int k = 0; //2 rożne k - 0 dla rosnacych i 1 dla losowych
        while(k < 2){ //czy rosnace czy losowe
            try {
                FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                if(k%2 == 0){
                    file.write("\nrosnace;");
                }
                else {
                    file.write("\nlosowe;");
                }
                file.write("\nn ; porownania; odczyt/podstawienia; przedWysokosc; poWysokosc; maxPorow; maxOdczyt; maxWys;\n");
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            //wielkosc ciagu
            n=10000;

            while(n<=100000){
                double sreodczyt = 0;
                double sreporown = 0;
                double sreprzedwyskosc = 0;
                double srepowysokosc = 0;

                double maxodczyt = 0;
                double maxporown = 0;
                double maxwysokosc = 0;

                //ilosc powtorzen
                int m = 20;
                for(int j = 0; j < m; j++){
                    //ciag do drzewa (najpierw rosnace potem losowe)
                    if(k%2 == 0){
                        tablica = rosnace(n);
                    }
                    else {
                        tablica = losowe(n);
                    }
                    odczyt = 0;
                    porownania = 0;

                    for(int i = 0; i<tablica.length; i++){
                        insert(tablica[i]);
                    }
                    wysokosc = height();
                    sreprzedwyskosc += wysokosc; //wysykosc przed deletem
                    System.out.println("n: "+n+" k:"+k+" j:"+j);

                    tablica = losowe(n);
                    for(int i = 0; i<tablica.length; i++){
                        delete(tablica[i]);
                    }
                    wysokosc = height();

                    sreporown += porownania;
                    sreodczyt += odczyt;
                    srepowysokosc += wysokosc;

                    if(maxodczyt<odczyt){
                        maxodczyt = odczyt;
                    }
                    if(maxporown<porownania){
                        maxporown = porownania;
                    }
                    if (maxwysokosc<wysokosc){
                        maxwysokosc = wysokosc;
                    }
                    while (korzenglowny != null){
                        delete(korzenglowny.wezel);
                    }
                }
                
                sreporown = sreporown/ m;
                sreodczyt = sreodczyt / m;
                sreprzedwyskosc = sreprzedwyskosc / m;
                srepowysokosc = srepowysokosc /m;

                try {
                    FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                    file.append(Integer.toString(n)).append(";");
                    file.append(Double.toString(sreporown)).append(";");
                    file.append(Double.toString(sreodczyt)).append(";");
                    file.append(Double.toString(sreprzedwyskosc)).append(";");
                    file.append(Double.toString(srepowysokosc)).append(";");

                    file.append(Double.toString(maxporown)).append(";");
                    file.append(Double.toString(maxodczyt)).append(";");
                    file.append(Double.toString(maxwysokosc)).append(";\n");

                    file.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                n +=10000;
            }
            k++;
        }
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
            korzenglowny = newNode;
            if (printingEveryAction) {
                printTree();
            }
            return;
        }
        
        //szukanie miejsca do wpisania
        TreeBst current = korzenglowny; //od korzenia
        TreeBst parent;
        while (true) { //az return
            parent = current;
            if (current.wezel >= value) {
                current = current.lewy;
                //wolne miejsce - dodajemy
                if (current == null) {
                    odczyt++;
                    parent.lewy = newNode;
                    return;
                }
            } else {
                current = current.prawy;
                if (current == null) {
                    odczyt++;
                    parent.prawy = newNode;
                    return;
                }
            }
        }
    }

    //usuwanie jednego wystąpienia klucza k w drzewie (jeśli istnieje).
    static void delete(int value){
        Searching search = new Searching(value, korzenglowny);
        porownania+=search.porownania;
        odczyt+=search.odczyt;
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
        if (korzenglowny == null){
            return;
        }
        //czy w ogole jest
        if(element == null){
            return;
        }
        //brak obu lisci
        if(element.lewy == null && element.prawy == null){
            if(rodzic == null) {  //brak rodzica == to korzen glowny
                korzenglowny = null;
            }//usuwanie wybranego el wzgledem rodzica
            else if(rodzic.lewy == element){
                porownania++;
                odczyt++;
                rodzic.lewy = null;
            }
            else if(rodzic.prawy == element) {
                porownania++;
                odczyt++;
                rodzic.prawy = null;
            }
        }//jeden lisc - prawy
        else if(element.lewy==null){
            //ustala ktory lisc dla rodzica by podac mu wszystkich potomków wybranego punktu
            if(rodzic == null){//brak rodzica
                odczyt++;
                korzenglowny = element.prawy;
            }
            else if(rodzic.lewy == element){
                odczyt++;
                porownania++;
                rodzic.lewy = element.prawy;
            }
            else if(rodzic.prawy == element){
                odczyt++;
                porownania++;
                rodzic.prawy = element.prawy;
            }
        }//tylko lewy lisc
        else if(element.prawy==null){
        
            if(rodzic == null){
                odczyt++;
                korzenglowny = element.lewy;
            }
            else if(rodzic.lewy == element){
                odczyt++;
                porownania++;
                rodzic.lewy = element.lewy;
            }
            else if(rodzic.prawy==element){
                odczyt++;
                porownania++;
                rodzic.prawy = element.lewy;
            }
        }//oba lisice
        else{
            //deleteThis(nast); //dostosowuje drzewo dalej
            odczyt++;
            element.wezel= nastepnik(element).wezel; //nowa wartosc, ale reszta polaczen tak samo
        }
    }

    //Szuka nastepnika, ktory ma sie pojawic na miejscu usuwanej wartosci w przypadku obu wartosci
    static TreeBst nastepnik(TreeBst korzen){
        //musi byc bo ma oba liscie
        TreeBst rodzic = korzen;
        korzen=korzen.prawy; //wiekszy
        odczyt++;
        while(korzen.lewy != null){
            odczyt++;
            rodzic = korzen;
            korzen = korzen.lewy; //ale jak najmniejszy
        }
        deleteThis(korzen,rodzic); //dostosowuje drzewo dalej
        return korzen;
    }
    
    //funkcja zwracająca bieżącą wysokość drzewa.
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
