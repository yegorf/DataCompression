import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;


public class Coder {

    private int height;
    private int width;

    private ArrayList<String> buffer = new ArrayList<>();
    private ArrayList<String> haffmanBuffer = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> counts = new ArrayList<>();
    private ArrayList<Integer> haffmanBytes = new ArrayList<>();


    //Читаем кодовую таблицу
    private String[][] codes = new String[92][3];

    public void readCodes(String path) throws IOException {
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
    }

    //делает все
    public void openAndRead(String url) throws IOException {
        BufferedImage image = ImageIO.read(new File(url));

        WritableRaster raster = image.getRaster();

        StringBuilder s = new StringBuilder();

        width = raster.getWidth();
        height = raster.getHeight();
        for (int y = 0; y < height; y++) {
            s = new StringBuilder();
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) == -16777216) {
                    s.append('0');
                } else {
                    s.append('1');
                }
            }
            s.append('n');
            buffer.add(s.toString());
        }

        //print();

        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Repeats:");
        counts = getCounts();
        for (ArrayList<Integer> list : counts) {
            for (Integer count : list) {
                System.out.print(count + " ");
                stringBuilder.append(count);
            }
            System.out.println();
        }
        writeToFile("begin.txt", stringBuilder.toString());


        readMethod(counts);
        //print();
        System.out.println();
        System.out.println("COUNTS SIZE = " + counts.size());
        printSize();
        deleteLines();
        //print();

        stringBuilder = new StringBuilder();
        for (ArrayList<Integer> list : counts) {
            for (Integer count : list) {
                //System.out.print(count + " ");
                stringBuilder.append(count);
            }
            System.out.println();
        }
        writeToFile("end.txt", stringBuilder.toString());

        printSize();
        System.out.println("COUNTS SIZE = " + counts.size());
        haffmanMethod();


        decode();
        // print();
        inImage(raster);

    }

    private void writeToFile(String url, String text) throws IOException {
        FileWriter fileWriter = new FileWriter(url);
        fileWriter.write(text);
        fileWriter.close();
    }


    private void printSize() {
        System.out.println("SIZE = " + buffer.size());
    }

    private void haffmanMethod() {
        StringBuilder s = new StringBuilder();
        boolean white = true;
        int len;
        System.out.println("KEKEKE");
        for (ArrayList<Integer> in : counts) {
            //s = new StringBuilder();
            for (Integer inin : in) {

                System.out.print(inin + " ");
            }
        }
        System.out.println();

        int countsNum = 0;
        for (ArrayList<Integer> in : counts) {
            //s = new StringBuilder();
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
                // System.out.println("\naloalo");
                // System.out.println(s.toString());
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
        // print
        System.out.println("HAFFMANBUFFER");
        System.out.println(s.toString());

        //KOSTIL
        len = s.length();
        //to 10
        while (s.length() != 0) {
            if (s.length() < 8) {
                haffmanBytes.add(Integer.parseInt(s.substring(0), 2));
                s.delete(0, s.length());
            } else {
                haffmanBytes.add(Integer.parseInt(s.substring(0, 8), 2));
                s.delete(0, 8);
            }
        }




        //deprecate
//        for (Integer x : haffmanBytes) {
//            System.out.println(x);
//        }

        //toBinary
        // PUT -1 to size and not append last
        for (int j = 0; j < haffmanBytes.size(); j++) {
            String strX = Integer.toBinaryString(haffmanBytes.get(j));
            if (strX.length() < 8) {
                for (int i = 0; i < 8 - strX.length(); i++) {
                    s.append("0");
                }
                s.append(strX);
            } else {
                s.append(Integer.toBinaryString(haffmanBytes.get(j)));
            }
        }
        //s.append(haffmanBytes.get(haffmanBytes.size() - 1));
        s.delete(len, s.length());
        System.out.println(s.toString());


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
                    //System.out.print(0 + " ");
                    s.delete(0, s.length() >= j ? j : s.length());
                }
//                if(tmp.equals("111111111111")){
//                    System.out.print(" REPEAT ");
//                    builder.append("p");
//                }
                if (tmp.equals(codes[91][1])) {
                    white = false;
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
                            //builder.append(codes[i][0] + " ");
                            summ += Integer.parseInt(codes[i][0]);
                            System.out.print(codes[i][0] + " ");
                            s.delete(0, s.length() >= j ? j : s.length());
                            break kek;
                        }
                    }

                }
            }

        }

        System.out.println();
        System.out.println("#!@I&^%R");
        for (String s1 : haffmanBuffer) {
            System.out.println(s1);
        }

    }

    private void print() {
        System.out.println("Bits:");
        for (String str : buffer) {
            for (char c : str.toCharArray()) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println(buffer.size());
    }

    //пихаем в картинку
    private void inImage(WritableRaster raster) throws IOException {
        BufferedImage fin = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (buffer.get(y).charAt(x) == '0') {
                    fin.setRGB(x, y, -16777216);
                } else {
                    fin.setRGB(x, y, 16777215);
                }
            }
        }
        ImageIO.write(fin, "bmp", new File("result.bmp"));
    }

    public void deleteLines() {

        for (int i = 0; i < buffer.size() - 1; i++) {
            int ind = i;
            while (i + 1 < buffer.size() && buffer.get(i + 1).contains("y")) {
                buffer.set(ind, buffer.get(ind) + "p");
                buffer.remove(i + 1);
                counts.remove(i + 1);

            }

        }
    }

    public void decode() {

        //KOSTIL
        // haffmanBuffer.remove(1);

        for (int i = 0; i < height - 1; i++) {
            int j = i + 1;
            while (haffmanBuffer.get(i).contains("p")) {
                if (haffmanBuffer.get(i).indexOf('p') != -1) {
                    haffmanBuffer.add(j++, haffmanBuffer.get(i).substring(0, haffmanBuffer.get(i).indexOf('p') - 1));
                }
                haffmanBuffer.set(i, haffmanBuffer.get(i).replaceFirst("p", ""));


            }
            //i = j - 1;
        }

        //KOSTIL
        haffmanBuffer.set(haffmanBuffer.size() - 1, haffmanBuffer.get(0));
        //haffmanBuffer.add(haffmanBuffer.get(0));

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

//        for (String s : buffer) {
//            if (s.contains("y")) {
//                s.replace('y', ' ');
//            } else {
//                s.replace('y', ' ');
//            }
//        }


    }

    public void readMethod(ArrayList<ArrayList<Integer>> counts) {
        for (int i = 0; i < height - 1; i++) {
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
                    buffer.set(i + 1, buffer.get(i + 1).replace('n', 'y'));
                } else {
                    if (Math.abs(cur - nex) >= 3) {
                        System.out.println("Не повторяем");
                        buffer.set(i + 1, buffer.get(i + 1).replace('y', 'n'));
                        break;
                    } else {
                        System.out.println("Повторяем");
                        buffer.set(i + 1, buffer.get(i + 1).replace('n', 'y'));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getCounts() {
        ArrayList<ArrayList<Integer>> counts = new ArrayList<>();


        for (String s : buffer) {

            boolean white = true;
            ArrayList<Integer> countCurrent = new ArrayList<>();
            int count = 0;
            for (int j = 0; j < width; j++) {
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