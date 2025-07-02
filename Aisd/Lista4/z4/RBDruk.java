import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;

class TreeRbBst {
    int wezel;
    boolean kolorCzyCzarny;
    TreeRbBst parent;
    TreeRbBst lewy;
    TreeRbBst prawy;
    //bst wiec lewy <=wezel<prawy
    TreeRbBst(int korzen, boolean czyCzarny){
        this.wezel = korzen;
        kolorCzyCzarny = czyCzarny;
        lewy = null;
        prawy = null;
    }
    TreeRbBst(int korzen){
        this.wezel = korzen;
        kolorCzyCzarny = true;
        lewy = null;
        prawy = null;
    }
}

class RbDruk{

    static TreeRbBst korzenglowny = null;
    static SecureRandom losowe = new SecureRandom();

    static boolean printingEveryAction = true;

    static String nazwapliku = "dowykresówRB.csv";
    static int  porownania = 0;
    static int odczyt = 0; //odczyt i podstaweinia
    static int wysokosc = 0;

    public static void main(String[] args) {
        korzenglowny = null;
        printingEveryAction = false;

        int[] tablica;
        int n=0;
        int k = 0; //2 rożne k - 0 dla rosnacych i 1 dla losowych
        while(k < 2){
            try {
                //String afa = String.valueOf(n)+" n-k "+String.valueOf(k)+";\n";
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
            catch ( IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("n: "+n+" k: "+k+" ");
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
                for(int j = 0; j< m; j++){
                    //ciag do drzewa
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
                    sreprzedwyskosc += wysokosc;
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

    //wstawianie nowego wystąpienia klucza k do drzewa
    static void insert(int wartosc) {
        if (printingEveryAction) {
            System.out.println("insert " + wartosc);
        }
        //lokalizacja
        TreeRbBst wezel = korzenglowny;
        TreeRbBst parent = null;

        //idziemy w glab by znalezc wolne miejsce
        while (wezel != null) {
            parent = wezel;
            if (wartosc < wezel.wezel){
                odczyt++;
                porownania++;
                if(wezel.lewy == null){
                    break;
                }
                wezel = wezel.lewy;
            }
            else if (wartosc > wezel.wezel) {
                odczyt++;
                porownania++;
                if(wezel.prawy == null){
                    break;
                }
                wezel = wezel.prawy;
            }
            else {
                //System.out.println("Wartosc juz jest w drzewie");
                return;
            }
        }
        //tworzenie wezla
        TreeRbBst nowe = new TreeRbBst(wartosc, false);
        if (parent == null) {
            odczyt++;
            korzenglowny = nowe;
        }//czy lewy czy prawy
        else if (wartosc < parent.wezel) {
            odczyt++;
            parent.lewy = nowe;
        }
        else {
            odczyt++;
            parent.prawy = nowe;
        }
        nowe.parent = parent;
        insertRepairColor(nowe);
        if (printingEveryAction) {
            //printTree();
        }
    }

    //poprawia kolory, zeby zachowane wlasnosci
    static void insertRepairColor(TreeRbBst wezel) {
        TreeRbBst parent = wezel.parent;

        //od dolu do gory. Do konca lub do czarnego parenta
        while (parent != null && !parent.kolorCzyCzarny) {
            TreeRbBst przodek = parent.parent; //dziadek dodanego
            if(przodek == null){ //parent jako korzen drzewa
                break;
            }
            TreeRbBst krewny = getUncle(parent);

            //Opcja1 - krewny czerwony
            if (krewny != null && !krewny.kolorCzyCzarny) {
                //recolor parent, przodek, krewny
                parent.kolorCzyCzarny = true;
                przodek.kolorCzyCzarny = false;
                krewny.kolorCzyCzarny = true;

                odczyt++;
                wezel = przodek; // do gory i sprawdzic wlasciwosci
                parent = wezel.parent; // ciagle jego rodzic
            }
            else {  //krewny jest czarny
                if (parent == przodek.lewy) { //jestesmy jego lewym dzieckiem
                    porownania++;
                    // Opcja 2.1: Wezel z zakretem
                    if (wezel == parent.prawy) {
                        porownania++;
                        rotateLewy(parent);
                        // do góry
                        wezel = parent;
                        parent = wezel.parent;
                        odczyt++;
                    }
                    // Opcja 3.1: Wezel prosto
                    rotatePrawy(przodek);

                    // Recolor parent, przodek
                    parent.kolorCzyCzarny = true;
                    przodek.kolorCzyCzarny = false;
                } else { //prawym dzieckiem
                    // Opcja 2.2: wezel z zakretem (ale w druga strone)
                    if (wezel == parent.lewy) {
                        porownania++;
                        rotatePrawy(parent);

                        odczyt++;
                        wezel = parent;
                        parent = wezel.parent;
                    }
                    // Opcja 3.2: Wezel prosto
                    rotateLewy(przodek);

                    parent.kolorCzyCzarny = true;
                    przodek.kolorCzyCzarny = false;
                }
            }
        }
        if(parent == null){ //korzen glowny powinien byc zawsze czarny
            wezel.kolorCzyCzarny = true;
        }
    }   
    
    private static TreeRbBst getUncle(TreeRbBst parent) {
        TreeRbBst przodek = parent.parent;
        if (przodek == null){
            return null;
        }
        //z drugiej strony niz nasz paren
        if (przodek.lewy == parent) {
            porownania++;
            return przodek.prawy;
        }
        else if (przodek.prawy == parent) {
            porownania++;
            return przodek.lewy;
        } else {
            System.out.println("Wezel nie jest dzieckiem przodka");
            return null;
        }
    }

    private static void rotatePrawy(TreeRbBst wezel) {
        TreeRbBst rodzic = wezel.parent;
        TreeRbBst leweDziecko = wezel.lewy;

        if (leweDziecko != null) {
            wezel.lewy = leweDziecko.prawy;
            odczyt++;
            if (leweDziecko.prawy != null) {
                leweDziecko.prawy.parent = wezel; //nie bylo aktu parenta
                odczyt++;
            }
            leweDziecko.prawy = wezel;
            wezel.parent = leweDziecko;
            odczyt++;

            replaceParentsChild(rodzic, wezel, leweDziecko);
        }
    }

    private static void rotateLewy(TreeRbBst wezel) {
        TreeRbBst rodzic = wezel.parent;
        TreeRbBst praweDziecko = wezel.prawy;

        if(praweDziecko != null) {
            wezel.prawy = praweDziecko.lewy;
            odczyt++;
            if (praweDziecko.lewy != null) {
                praweDziecko.lewy.parent = wezel;
                odczyt++;
            }

            praweDziecko.lewy = wezel;
            wezel.parent = praweDziecko;
            odczyt++;

            replaceParentsChild(rodzic, wezel, praweDziecko);
        }
    }

    private static void replaceParentsChild(TreeRbBst rodzic, TreeRbBst stareDziecko, TreeRbBst noweDziecko) {
        if (rodzic == null) { //pusty, wezel glowny
            korzenglowny = noweDziecko;
        }
        else if (rodzic.lewy == stareDziecko) {
            porownania++;
            odczyt++;
            rodzic.lewy = noweDziecko;
        }
        else if (rodzic.prawy == stareDziecko) {
            porownania++;
            odczyt++;
            rodzic.prawy = noweDziecko;
        }
        else {
            //System.out.println("TreeRbBst is not a child of its parent");
            return;
        }
        if (noweDziecko != null) {
            noweDziecko.parent = rodzic;
        }
    }

    //Szuka nastepnika, ktory ma sie pojawic na miejscu usuwanej wartosci w przypadku obu wartosci
    static TreeRbBst nastepnik(TreeRbBst korzen){
        //musi byc bo ma oba liscie
        TreeRbBst rodzic = korzen;
        korzen=korzen.prawy;
        odczyt++;
        while(korzen.lewy != null){
            odczyt++;
            rodzic = korzen;
            korzen = korzen.lewy;
        }
        deleteThis(korzen,rodzic); //dostosowuje drzewo dalej
        return korzen;
    }

        static void delete(int value){
        TreeRbBst search = szukanie(value);
        if(search == null){
            return;
        }
        if (printingEveryAction){
            System.out.println("delete " + value+ " "+search.wezel);
            if(search == null){
                // System.out.println("klucza " + value + " nie ma w drzewie, wiec nie mozemy go usunac ");
                return;
            }
            deleteThis(search,search.parent);
            //deleteRepairColor(search);
            //printTree();
        }
        else {
            if(search == null){
                return;
            }
            deleteThis(search, search.parent);
            //deleteRepairColor(search);
        }
    }
    
    //jak zwykle bst
    static void deleteThis(TreeRbBst element, TreeRbBst rodzic){
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
            if(element.kolorCzyCzarny){
                deleteRepairColor(rodzic);
            }
        }//jeden lisc - prawy
        else if(element.lewy==null){
            //ustala ktory lisc dla rodzica by podac mu wszystkich potomków wybranego punktu
            if(rodzic == null){//brak rodzica
                odczyt++;
                korzenglowny = element.prawy;
                element.prawy.parent = element.parent;
            }
            else if(rodzic.lewy == element){
                odczyt++;
                porownania++;
                rodzic.lewy = element.prawy;
                element.prawy.parent = element.parent;
            }
            else if(rodzic.prawy == element){
                odczyt++;
                porownania++;
                rodzic.prawy = element.prawy;
                element.prawy.parent = element.parent;
            }
            if(element.kolorCzyCzarny) {
                deleteRepairColor(element.prawy); //czerwony to tylko jedene mniej w ciagu
            }

        }//tylko lewy lisc
        else if(element.prawy==null){

            if(rodzic == null){
                odczyt++;
                korzenglowny = element.lewy;
                //element.lewy.parent = null;
                element.lewy.parent = element.parent;
            }
            else if(rodzic.lewy == element){
                odczyt++;
                porownania++;
                rodzic.lewy = element.lewy;
                element.lewy.parent = element.parent;
            }
            else if(rodzic.prawy==element){
                odczyt++;
                porownania++;
                rodzic.prawy = element.lewy;
                element.lewy.parent = element.parent;
            }
            if(element.kolorCzyCzarny) {
                deleteRepairColor(element.lewy); //czerwony to tylko jedene mniej w ciagu
            }
        }//oba lisice
        else{
            //deleteThis(nast); //dostosowuje drzewo dalej
            odczyt++;
            element.wezel= nastepnik(element).wezel; //nowa wartosc, ale reszta polaczen tak samo
        }
    }	  
    
    static void deleteRepairColor(TreeRbBst wezel) {
        //nie ma co poprawiac w pustym
        if(korzenglowny == null){
            return;
        }
        //az dotrzemy do glownego korzenia albo nie jestesmy na czarnym wezle
        while (wezel != korzenglowny && wezel.parent != null && wezel.kolorCzyCzarny) {
            TreeRbBst parent = wezel.parent;
            //lewy potomek rodzica
            if (parent.lewy != null && wezel == parent.lewy) {
                TreeRbBst sibling = parent.prawy;

                // Case 1: Czerwony sibling
                if (sibling != null && !sibling.kolorCzyCzarny) {
                    sibling.kolorCzyCzarny = true;
                    parent.kolorCzyCzarny = false;
                    rotateLewy(parent);
                    sibling = parent.prawy;
                }
                // Case 2: Czarny sibling i jego dzieci
                if ((sibling != null && sibling.lewy != null && sibling.prawy != null)
                        && (sibling.lewy.kolorCzyCzarny && sibling.prawy.kolorCzyCzarny)) {
                    sibling.kolorCzyCzarny = false;
                    wezel = parent;
                } else {
                    // Case 3: Black sibling i jego potomkowie: czerwony i czarny
                    if (sibling != null && sibling.prawy != null &&  sibling.lewy != null && sibling.prawy.kolorCzyCzarny) {
                        sibling.lewy.kolorCzyCzarny = true;
                        sibling.kolorCzyCzarny = false;
                        rotatePrawy(sibling);
                        sibling = parent.prawy;
                    }

                    // Case 4: Czarny sibling jego prawy potomek = czerwony
                    if (sibling != null && sibling.prawy != null) {
                        sibling.kolorCzyCzarny = parent.kolorCzyCzarny;
                        parent.kolorCzyCzarny = true;
                        sibling.prawy.kolorCzyCzarny = true;
                    }
                    rotateLewy(parent);
                    wezel = korzenglowny; // Exit the loop
                }
                //prawy potomek rodzica, lustrzane przypadki
            } else {
                TreeRbBst sibling = parent.lewy;

                // Case 1: Czerwony sibling
                if (sibling != null && !sibling.kolorCzyCzarny) {
                    sibling.kolorCzyCzarny = true;
                    parent.kolorCzyCzarny = false;
                    rotatePrawy(parent);
                    sibling = parent.lewy;
                }
                // Case 2: Czarny sibling i jego dzieci
                if (sibling != null && sibling.prawy != null && sibling.prawy.kolorCzyCzarny
                        && sibling.lewy != null && sibling.lewy.kolorCzyCzarny) {
                    sibling.kolorCzyCzarny = false;
                    wezel = parent;
                } else {
                    // Case 3: Black sibling i jego potomkowie: czerwony i czarny
                    if (sibling != null && sibling.lewy != null && sibling.lewy.kolorCzyCzarny && sibling.prawy != null) {

                        sibling.prawy.kolorCzyCzarny = true;
                        sibling.kolorCzyCzarny = false;
                        rotateLewy(sibling);
                        sibling = parent.lewy;
                    }

                    // Case 4: Czarny sibling jego prawy potomek = czerwony
                    if (sibling != null) {
                        sibling.kolorCzyCzarny = parent.kolorCzyCzarny;
                        parent.kolorCzyCzarny = true;
                        if (sibling.lewy != null) {
                            sibling.lewy.kolorCzyCzarny = true;
                        }
                    }
                    rotatePrawy(parent);
                    wezel = korzenglowny;
                }
            }
        }
        if (wezel != null) {
            wezel.kolorCzyCzarny = true;
        }
    }
    
        //przeniesiono z oddzielnej klasy bo nie potrzebujemy juz szukac parenta
    static TreeRbBst szukanie(int obiekt) {
        TreeRbBst current = korzenglowny;
        while (current != null) {
            if (obiekt == current.wezel) {
                porownania++;
                odczyt++;
                return current;
            } else if (obiekt <= current.wezel) {
                porownania++;
                current = current.lewy;
            } else {
                porownania++;
                current = current.prawy;
            }
        }
        return null; // nie znaleziono
    }
    
    //funkcja zwracająca bieżącą wysokość drzewa.
    public static int height() {
        int height = 0;
        if (korzenglowny == null) {
            System.out.println("Tree is empty.");
            return height;
        }
        
        Queue<TreeRbBst> queue = new LinkedList<>();
        queue.offer(korzenglowny);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeRbBst current = queue.poll();
                //najpierw czy w lewo -> dodaj
                if (current != null && current.lewy != null) {
                    queue.offer(current.lewy);
                }
                //prawo dodaj
                if (current != null && current.prawy != null) {
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
        printdlaWezel(korzenglowny,"","",true);
    }


    private static void printdlaWezel(TreeRbBst wezel, String padding, String pointer, boolean czyOstatni) {
        if (wezel != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(padding); //odstep
            sb.append(pointer);
            sb.append(wezel.wezel);//wartosc
            if (wezel.kolorCzyCzarny){
                sb.append(" (").append("b").append(")"); //powinno byc z kolorem
            }
            else {
                sb.append(" (").append("r").append(")");
            }
            System.out.println(sb.toString());

            String paddingForBoth = padding + (czyOstatni ? "   " : "│  ");
            String pointerRight = "└--";
            String pointerLeft = (wezel.prawy != null) ? "├──" : "└──";

            printdlaWezel(wezel.lewy, paddingForBoth, pointerLeft, false);
            printdlaWezel(wezel.prawy, paddingForBoth, pointerRight, true);
        }
    }

    //generatory
    static int[] losowe (int n) {
        boolean[] czyBylo = new boolean[2*n -1];
        for (int j=0; j<czyBylo.length; j++) {
            czyBylo[j] = false;
        }
        int i=0;
        int[] klucz = new int [n];
        int a;
        while(i<n){
            do {
                a = losowe.nextInt(2 * n - 1);
            }
            while (czyBylo[i]);
            klucz[i] = a;
            czyBylo[i] = true;
            i++;
        }
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
