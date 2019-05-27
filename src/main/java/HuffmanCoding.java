import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;


public class HuffmanCoding {
    private String CODES_URL = "codes.txt";
    private ArrayList<String> buffer = new ArrayList<>();
    private ArrayList<String> haffmanBuffer = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> counts = new ArrayList<>();
    private ArrayList<Integer> haffmanBytes = new ArrayList<>();
    private ImageHandler imageHandler = new ImageHandler();
    private String[][] codes = new String[92][3];
    private BinCalculator calculator = new BinCalculator();
    private ReadCoding readCoding = new ReadCoding();
    private WritableRaster raster;

    public void encode(String url) throws IOException {
        //Получаем таблицу кодов Хаффмана
        codes = HaffmanTable.readCodes(CODES_URL);
        //Читаем картинку в биты
        raster = imageHandler.readFile(url, buffer);
        //Получаем длины последовательностей
        counts = getCounts();
        //Удаляем строки ридом
        readCoding.readMethod(counts, imageHandler.getHeight(), buffer);
        //Кодируем Хаффманом
        haffmanMethod();
    }

    private void haffmanMethod() {
        StringBuilder s = new StringBuilder();
        boolean white = true;
        int len;
        InfoPrinter.printRepeats(counts);

        int countsNum = 0;
        for (ArrayList<Integer> in : counts) {
            for (Integer inin : in) {
                while (inin > 64) {
                    for (int i = 90; i >= 0; i--) {
                        if (Integer.parseInt(codes[i][0]) <= inin) {
                            inin -= Integer.parseInt(codes[i][0]);
                            System.out.print(codes[i][0] + " ");
                            s.append(codes[i][white ? 1 : 2]);
                            break;
                        }
                    }
                }
                if (inin != 0) {
                    for (int i = 0; i < 92; i++) {
                        if (Integer.parseInt(codes[i][0]) == inin) {
                            System.out.print(codes[i][0] + " ");
                            s.append(codes[i][white ? 1 : 2]);
                            break;
                        }
                    }
                }
                s.append(Integer.toBinaryString((int) '-'));
                white = !white;
            }

            //КОЛИЧЕСТВО ПОВТОРОВ КОДИРУЕМ
            int pos = buffer.get(countsNum).indexOf("p");
            if (pos != -1) {
                String substring = buffer.get(countsNum).substring(pos);
                for (int j = 0; j < substring.length(); j++) {
                    s.append(codes[91][1]);
                }
            }
            s.append(codes[91][1]);
            white = true;
            countsNum++;
        }
        System.out.println("HAFFMANBUFFER");
        System.out.println(s.toString());

        len = s.length();
        calculator.toDec(s, haffmanBytes);
        calculator.toBin(s, haffmanBytes, len);

        //to codes
        StringBuilder builder = new StringBuilder();
        int summ = 0;
        white = true;
        while (s.length() != 0) {
            int countRepeats = 0;
            kek:
            for (int j = 13; j > 0; j--) {
                String tmp = s.substring(0, s.length() >= j ? j : s.length());
                if (tmp.equals("000000000000")) {
                    System.out.print(" END ");
                    haffmanBuffer.add(builder.toString());
                    s.delete(0, s.length() >= j ? j : s.length());
                }
                if (tmp.equals(codes[91][1])) {
                    int counter = 0;
                    do {
                        counter += 12;
                        countRepeats++;
                        if (s.length() < counter + 12) {
                            break;
                        }
                    } while (s.substring(counter, counter + 12).equals(codes[91][1]));
                    for (int i = 0; i < countRepeats - 1; i++) {
                        builder.append("p");
                    }

                    System.out.print(" EOL ");
                    haffmanBuffer.add(builder.toString());
                    builder = new StringBuilder();
                    s.delete(0, 12 * countRepeats);
                    white = true;
                    break;
                } else {
                    if (tmp.equals("101101")) {
                        System.out.print(".");
                        builder.append(summ + " ");
                        s.delete(s.indexOf("101101"), s.indexOf("101101") + 6);
                        summ = 0;
                        white = !white;
                        break;
                    }
                    for (int i = 0; i < 92; i++) {
                        if (codes[i][white ? 1 : 2].equals(tmp)) {
                            summ += Integer.parseInt(codes[i][0]);
                            System.out.print(codes[i][0] + " ");
                            s.delete(0, s.length() >= j ? j : s.length());
                            break kek;
                        }
                    }
                }
            }
        }
    }

    public void decode(String url) throws IOException {
        for (int i = 0; i < imageHandler.getHeight() - 1; i++) {
            int j = i + 1;
            while (haffmanBuffer.get(i).contains("p")) {
                if (haffmanBuffer.get(i).indexOf('p') != -1) {
                    haffmanBuffer.add(j++, haffmanBuffer.get(i).substring(0, haffmanBuffer.get(i).indexOf('p') - 1));
                }
                haffmanBuffer.set(i, haffmanBuffer.get(i).replaceFirst("p", ""));
            }
        }
        haffmanBuffer.set(haffmanBuffer.size() - 1, haffmanBuffer.get(0));

        buffer.clear();
        StringBuilder builder;
        boolean white;
        for (String s : haffmanBuffer) {
            white = true;
            builder = new StringBuilder();
            String[] s1 = s.split(" ");
            for (int j = 0; j < s1.length; j++) {
                for (int i = 0; i < Integer.parseInt(s1[j]); i++) {
                    builder.append(white ? "1" : "0");
                }
                white = !white;
            }
            buffer.add(builder.toString());
        }
        imageHandler.createImage(url, raster, buffer);
    }

    public ArrayList<ArrayList<Integer>> getCounts() {
        ArrayList<ArrayList<Integer>> counts = new ArrayList<>();

        for (String s : buffer) {
            boolean white = true;
            ArrayList<Integer> countCurrent = new ArrayList<>();
            int count = 0;
            for (int j = 0; j < imageHandler.getWidth(); j++) {
                if (s.charAt(j) == '1') {
                    if (!white) {
                        white = true;
                        countCurrent.add(count);
                        count = 0;
                    }
                    count++;
                }
                if (s.charAt(j) == '0') {
                    if (white) {
                        white = false;
                        countCurrent.add(count);
                        count = 0;
                    }
                    count++;
                }
            }
            countCurrent.add(count);
            counts.add(countCurrent);
        }
        return counts;
    }
}