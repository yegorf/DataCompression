import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;


public class HuffmanCoding {
    private final String CODES_URL = "codes.txt";
    private final int MAX_CODE_SIZE = 13;

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
        System.out.println(haffmanBuffer);
    }

    private void haffmanMethod() throws IOException {
        StringBuilder s = new StringBuilder();
        boolean white = true;
        int len;
        InfoPrinter.printRepeats(counts);

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

        writeFile(s);
    }

    private int ost;
    public void writeFile(StringBuilder bitsBuffer) throws IOException {
        ost = 0;
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("archive.txt")));
        while (bitsBuffer.length() > 0) {
            while (bitsBuffer.length() < 8) {
                bitsBuffer.append('0');
                ost ++;
            }
            outputStream.writeByte((byte) Integer.parseInt(bitsBuffer.substring(0, 8), 2));
            bitsBuffer.delete(0, 8);
        }
        outputStream.close();
    }

    private String byteToBits(byte b) {
        return String.format("%" + 8 + "s", Integer.toBinaryString(b & 0xFF))
                .replace(' ', '0');
    }

    private void read(StringBuilder bitTemp, DataInputStream in) throws IOException {
        bitTemp.append(byteToBits(in.readByte()));
    }

    public StringBuilder readFile() throws IOException {
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream("archive.txt")));
        StringBuilder s = new StringBuilder();
        boolean neof = true;
        while(neof) {
            try {
                read(s, inputStream);
            } catch (EOFException e) {
                neof = false;
            }
        }
        String ss = s.substring(0, s.length() - ost); //ТУТ КОСТЫЫЛь
        s = new StringBuilder(ss);
        System.out.println("AAA " + s);
        return s;
    }


    public void decode(String url) throws IOException {

        StringBuilder s = readFile();
        int len = s.length();
        calculator.toDec(s, haffmanBytes);
        calculator.toBin(s, haffmanBytes, len);

        //В коды
        StringBuilder builder = new StringBuilder();
        int summ = 0;
        boolean white = true;
        while (s.length() != 0) {
            int countRepeats = 0;
            kek:
            for (int j = MAX_CODE_SIZE; j > 0; j--) {
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
        StringBuilder stringBuilder;
        //boolean white;
        for (String str : haffmanBuffer) {
            white = true;
            stringBuilder = new StringBuilder();
            String[] s1 = str.split(" ");
            for (int j = 0; j < s1.length; j++) {
                for (int i = 0; i < Integer.parseInt(s1[j]); i++) {
                    stringBuilder.append(white ? "1" : "0");
                }
                white = !white;
            }
            buffer.add(stringBuilder.toString());
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