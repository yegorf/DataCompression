import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HaffmanTable {
    //Читаем кодовую таблицу
    public static String[][] readCodes(String path) throws IOException {
        String[][] codes = new String[92][3];
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        String[] line;
        int i = 0;
        while ((strLine = br.readLine()) != null) {
            line = strLine.split(" ");
            for (int j = 0; j < 3; j++) {
                codes[i][j] = line[j];
            }
            i++;
        }
        for (int l = 0; l < 92; l++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(codes[l][j] + " ");
            }
            System.out.println();
        }
        return codes;
    }
}
