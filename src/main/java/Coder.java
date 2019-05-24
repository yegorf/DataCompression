import com.sun.javafx.iio.ImageStorage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Coder {

    private int height;
    private int width;

    private char[][] bits;

    //Читаем кодовую таблицу
    private String[][] codes = new String[92][3];
    public void readCodes(String path) throws IOException {
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        String[] line;
        int i=0;
        while ((strLine = br.readLine()) != null){
            line = strLine.split(" ");
            for(int j=0; j<3; j++) {
                codes[i][j] = line[j];
            }
            i++;
        }

        for(int l=0; l<92; l++) {
           for(int j=0; j<3; j++) {
               System.out.print(codes[l][j] + " ");
           }
            System.out.println();
        }
    }

    //Побитово читаем пикчу
    public void read(String url) throws IOException {
        BufferedImage image = ImageIO.read(new File(url));
        WritableRaster raster = image.getRaster();

        width = raster.getWidth();
        height = raster.getHeight();
        bits = new char[height][width + 1];

        for(int y = 0; y < height; y++){
            int i = 0;
            for(int x = 0; x < width; x++){
                if(image.getRGB(x, y) == -16777216){
                    bits[y][x] = '0';
                }
                else{
                    bits[y][x] = '1';
                }
                i++;
                if(i == width) {
                    i = 0;
                }
            }
        }

        System.out.println("Биты файла:");
        for(int l=0; l<height; l++) {
            for(int j=0; j<width; j++) {
                System.out.print(bits[l][j]);
            }
            System.out.println();
        }

        System.out.println("Повторения:");
        ArrayList<ArrayList<Integer>> counts = getCounts();
        for(ArrayList<Integer> list : counts) {
            for(Integer count : list) {
                System.out.print(count + " ");
            }
            System.out.println();
        }


//        readMethod(getCounts());
//        deleteLines();
//        System.out.println();
//        decode();
//
//        BufferedImage fin = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_INT_RGB);
//        for(int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if(bits[y][x] == '0') {
//                    fin.setRGB(x, y, -16777216);
//                } else  {
//                    fin.setRGB(x, y, 16777215);
//                }
//            }
//        }
//        ImageIO.write(fin, "bmp", new File("result.bmp"));
    }
//
//    public void deleteLines() {
//        for(int i=0; i<height - 1; i++) {
//            if(bits[i + 1][width] == 'y') {
//                for(int j=0;j<width-1;j++) {
//                    bits[i][j] = 'x';
//                }
//                bits[i][width] = 'p';
//            }
//        }
//
//        for(int i=0; i<height; i++) {
//            for(int j=0; j<width-1; j++) {
//                System.out.print(bits[i][j]);
//            }
//            System.out.println();
//        }
//    }
//
//    public void decode() {
//        for(int i=0; i<height - 1; i++) {
//            if(bits[i][width] == 'p') {
//                for(int j=0;j<width-1;j++) {
//                    bits[i][j] = bits[i - 1][j];
//                }
//            }
//        }
//
//        for(int i=0; i<height; i++) {
//            for(int j=0; j<width-1; j++) {
//                System.out.print(bits[i][j]);
//            }
//            System.out.println();
//        }
//    }
//
//    public void readMethod(ArrayList<ArrayList<Integer>> counts) {
//        for(int i=0; i<height-1; i++) {
//            ArrayList<Integer> current = counts.get(i);
//            ArrayList<Integer> next = counts.get(i+1);
//
//            int size;
//            if(current.size() < next.size()) {
//                size = current.size();
//            } else {
//                size = next.size();
//            }
//
//            for(int j=0; j<size; j++) {
//                if(Math.abs(current.get(j) - next.get(j)) >= 3) {
//                    System.out.println("Не повторяем");
//                    bits[i][width] = 'n';
//                } else {
//                    System.out.println("Повторяем");
//                    bits[i][width] = 'y';
//                }
//            }
//        }
//    }
//
    public ArrayList<ArrayList<Integer>> getCounts() {
        ArrayList<ArrayList<Integer>> counts = new ArrayList<>();

        for(int i=0; i<height; i++) {
            boolean white = true;
            ArrayList<Integer> countCurrent = new ArrayList<>();
            int count = 0;
            for(int j=0; j<width - 1; j++) {
                if(bits[i][j] == '1' && white) {
                    count++;
                }
                if(bits[i][j] == '0' && white) {
                    white = false;
                    countCurrent.add(count);
                    count = 0;
                }
                if(bits[i][j] == '0' && !white) {
                    count ++;
                }
                if(bits[i][j] == '1' && !white) {
                    white = true;
                    countCurrent.add(count);
                    count = 0;
                }

                if(j == width-1) {
                    countCurrent.add(count);
                }
            }
            counts.add(countCurrent);
        }
        return counts;
    }
}
