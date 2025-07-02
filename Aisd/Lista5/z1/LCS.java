import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class LCS {
    static int porownania = 0;
    static long czas = 0;
    static boolean printingEveryAction = false;
    static String nazwapliku = "dowykresówlcs.csv";

    public static void main(String[] args) {
        System.out.println(findLCS("ABEDM", "ABCDEAG")); //abe
        System.out.println(findLCS("Nabudohon", "abuDn"));

        try {
            FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie

            file.write("\nlcs;");

            file.write("\nn ; porownania; czas;\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int i = 1000;
        while(i<=5000) {
            int limit = 10;
            for (int j = 0; j <= limit; j ++) {
                String pierwszy = generateString(1000);
                String drugi = generateString(1000);
                String odp;

                //program i liczy czas
                long startTime = System.nanoTime();
                odp = findLCS(pierwszy, drugi);

                long endTime = System.nanoTime();
                long diffrence = endTime - startTime;
                czas += diffrence;

                if (printingEveryAction) {
                    System.out.println(pierwszy + "\n" + drugi + "\n");
                    System.out.println(odp);
                }
            }
            try {
                czas = czas/limit;
                porownania = porownania/limit;

                FileWriter file = new FileWriter(nazwapliku, true); //dopisywanie
                file.append(Integer.toString(i)).append(";");
                file.append(Double.toString(porownania)).append(";");
                file.append(Double.toString(czas)).append(";\n");

                file.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            i+=1000;
        }

    }
    public static String findLCS(String ciag1, String ciag2) {

        //podziel na znaki (dodaje x by latiwej macierz)
        String[] znaki1 = ("X" + ciag1).split("");
        String[] znaki2 = ("X" + ciag2).split("");

        int[][] macierz = new int[ciag1.length() + 1][ciag2.length() + 1];
        //wypelnianie macierzy
        for (int i = 0; i <= ciag1.length(); i++) {
            for (int j = 0; j <= ciag2.length(); j++) {
                //tam sa X
                if (i == 0 || j == 0) {
                    macierz[i][j] = 0;
                } else if (Objects.equals(znaki1[i], znaki2[j])) {
                    porownania++;
                    macierz[i][j] = 1 + macierz[i - 1][j - 1];
                } else { //do konca kolumny/rzedu
                    macierz[i][j] = Math.max(macierz[i - 1][j], macierz[i][j - 1]);
                }
            }
        }
        if (printingEveryAction) {
            printMatrix(macierz, ciag1.length() + 1, ciag2.length() + 1);
        }
        return traverseBackwards(ciag1, ciag2, macierz, ciag1.length(), ciag2.length(), new StringBuilder());
    }

    public static String traverseBackwards(String ciag1, String ciag2, int[][] macierz, int i, int j, StringBuilder builder) {

        int current = macierz[i][j]; //od konca
        //omijamy granice gdzie sa same 0 i sprawdzamy czy znamy juz wartosc
        if (i >= 1 && j >= 1 && macierz[i-1][j-1] == current){
            porownania++;
            return traverseBackwards(ciag1, ciag2, macierz, i-1, j-1, builder); //po skosie do tylu
        }
        //z jednej strony do rpzesuwania gdzie dokaldnie skoczylo
        else if (i >= 1 && macierz[i - 1][j] == current) {
            porownania++;
            return traverseBackwards(ciag1, ciag2, macierz, i - 1, j, builder);
        }
        else if (j >= 1 && macierz[i][j-1] == current) {
            porownania++;
            return traverseBackwards(ciag1, ciag2, macierz, i, j - 1, builder);
        }
        //buduje string
        else if (j >= 1 && i >= 1) {
            return traverseBackwards(ciag1, ciag2, macierz, i - 1, j - 1, builder.append(ciag1.split("")[i - 1]));
        }
        return builder.reverse().toString();
    }

    public static void printMatrix(int[][] matrix, int i, int j) {
        for(int x = 0; x < i; x++) {
            for(int y = 0; y < j; y++) {
                System.out.print(matrix[x][y] + " ");
            }
            System.out.println();
        }
    }

    public static String generateString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < length; i++) {
            //losuje ktory
            int randomIndex = random.nextInt(CHARACTERS.length());
            //pobiera znak
            char randomChar = CHARACTERS.charAt(randomIndex);
            //dodaje
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
