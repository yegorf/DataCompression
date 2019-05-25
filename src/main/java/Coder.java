import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;


public class Coder {
    private ArrayList<ArrayList<Integer>> counts = new ArrayList<>();
    private ArrayList<String> buffer = new ArrayList<>();
    private ArrayList<String> haffmanBuffer = new ArrayList<>();
    private ArrayList<Integer> haffmanBytes = new ArrayList<>();
    private String[][] codes = new String[92][3];
    private ImageHandler imageHandler = new ImageHandler();
    private InfoPrinter printer = new InfoPrinter();
    private BinCalculator calculator = new BinCalculator();

    //Делает все
    public void openAndRead(String url) throws IOException {
        WritableRaster raster = imageHandler.readFile(url, buffer);
        codes = HaffmanTable.readCodes("codes.txt");
        counts = getCounts();
        printer.printRepeats(counts);

        readMethod(counts);
        printer.printBitsMatrix(buffer);
        System.out.println();
        deleteLines();

        haffmanMethod();

        decode();
        printer.printBitsMatrix(buffer);
        imageHandler.createImage(raster, buffer);
    }


    private void haffmanMethod() {
        StringBuilder s = new StringBuilder();
        boolean white = true;
        int len;
        printer.printRepeats(counts);

        int countsNum = 0;
        for (ArrayList<Integer> countList : counts) {
            for (Integer count : countList) {
                while (count > 64) {
                    for (int i = 90; i >= 0; i--) {
                        if (Integer.parseInt(codes[i][0]) <= count) {
                            count -= Integer.parseInt(codes[i][0]);
                            System.out.print(codes[i][0] + " ");
                            s.append(codes[i][white ? 1 : 2]);
                            break;
                        }
                    }
                }
                if (count != 0) {
                    for (int i = 0; i < 92; i++) {
                        if (Integer.parseInt(codes[i][0]) == count) {
                            System.out.print(codes[i][0] + " ");

                            s.append(codes[i][white ? 1 : 2]);
                            break;
                        }
                    }
                }
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

        //KOSTIL
        len = s.length();
        calculator.toDec(s, haffmanBytes);
        calculator.toBin(s,haffmanBytes,len);
        System.out.println(s.toString());

        StringBuilder builder = new StringBuilder();
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
                    white = false;
                    int counter = 0;
                    do {
                        counter += 13;
                        countRepeats++;

                    } while (s.substring(counter - 1, counter + 11).equals(codes[91][1]));
                    for (int i = 0; i < countRepeats - 1; i++) {
                        builder.append(" p ");
                    }

                    System.out.print(" EOL ");
                    haffmanBuffer.add(builder.toString());
                    builder = new StringBuilder();
                    s.delete(0, 12 * countRepeats);
                    break;
                } else {
                    for (int i = 0; i < 92; i++) {
                        if (codes[i][white ? 1 : 2].equals(tmp)) {
                            builder.append(codes[i][0] + " ");
                            System.out.print(codes[i][0] + " ");
                            s.delete(0, s.length() >= j ? j : s.length());
                            break kek;
                        }
                    }
                }
            }
            white = !white;
        }

        System.out.println();
        System.out.println("#!@I&^%R");
        for (String s1 : haffmanBuffer) {
            System.out.println(s1);
        }
    }

    //Декодирование
    public void decode() {
        for (int i = 0; i < haffmanBuffer.size() - 1; i++) {
            if (haffmanBuffer.get(i).contains("p")) {
                haffmanBuffer.set(i, haffmanBuffer.get(i).replace('p', ' '));
                haffmanBuffer.add(i + 1, haffmanBuffer.get(i));
                i--;
            }
        }
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
    }

    //READ
    public void readMethod(ArrayList<ArrayList<Integer>> counts) {
        for (int i = 0; i < counts.size() - 1; i++) {
            ArrayList<Integer> current = counts.get(i);
            ArrayList<Integer> next = counts.get(i + 1);
            for (Integer x : current) {
                System.out.print(x + " ");
            }
            System.out.println();
            for (Integer x : next) {
                System.out.print(x + " ");
            }
            System.out.println();
            int size = current.size() < next.size() ? current.size() : next.size();

            int cur = 0, nex = 0;
            for (int j = 0; j < size; j++) {
                cur += current.get(j) + 1;
                nex += next.get(j) + 1;
                if (cur == nex && current.size() == 1 && next.size() == 1) {
                    System.out.println("Повторяем");
                    buffer.set(i, buffer.get(i).replace('n', 'y'));
                } else {
                    if (Math.abs(cur - nex) >= 3) {
                        System.out.println("Не повторяем");
                        buffer.set(i, buffer.get(i).replace('y', 'n'));
                        break;
                    } else {
                        System.out.println("Повторяем");
                        buffer.set(i, buffer.get(i).replace('n', 'y'));
                    }
                }
            }
        }
    }

    //Удаление строк
    public void deleteLines() {
        for (int i = 0; i < buffer.size() - 1; i++) {
            while (buffer.get(i++).contains("y") && i < buffer.size() - 1) {
                buffer.set(i - 1, buffer.get(i - 1) + "p");
                buffer.remove(i);
                counts.remove(i);
            }
        }
    }

    //Получение всех длин последовательностей
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