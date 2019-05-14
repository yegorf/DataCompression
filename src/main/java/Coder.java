import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Coder {

    private int height;
    private int width;

    private char[][] bits;

    public void read(String url) throws IOException {
        //StringBuffer sb = new StringBuffer();
        BufferedImage image = ImageIO.read(new File(url));
        WritableRaster raster = image.getRaster();

        width = raster.getWidth();
        height = raster.getHeight();
        bits = new char[height][width + 1];

        for(int y = 0; y < height; y++){
            int i = 0;
            for(int x = 0; x < width; x++){
                if(image.getRGB(x, y) == -16777216){
                  //  sb.append("0");
                    bits[y][x] = '0';
                }
                else{
                   /// sb.append("1");
                    bits[y][x] = '1';
                }
                i++;
                if(i == width) {
                   // sb.append("\n");
                    i = 0;
                }
            }
        }

        readMethod(getCounts());
        deleteLines();
        System.out.println();
        decode();
    }

    public void createImage() throws IOException {
        String url = "new-image.bmp";
        BufferedImage image = ImageIO.read(new File(url));
        WritableRaster raster = image.getRaster();

        for(int y = 0; y < raster.getHeight(); y++){
            int i = 0;
            for(int x = 0; x < raster.getWidth(); x++){
                if(bits[x][y] == '0'){
                    image.setRGB(x, y, -16777216);
                }
                else{
                    image.setRGB(x, y, -12277216);
                }
            }
        }
    }

    public void deleteLines() {
        for(int i=0; i<height - 1; i++) {
            if(bits[i + 1][width] == 'y') {
                for(int j=0;j<width-1;j++) {
                    bits[i][j] = 'x';
                }
                bits[i][width] = 'p';
            }
        }

        for(int i=0; i<height; i++) {
            for(int j=0; j<width-1; j++) {
                System.out.print(bits[i][j]);
            }
            System.out.println();
        }
    }

    public void decode() {
        for(int i=0; i<height - 1; i++) {
            if(bits[i][width] == 'p') {
                for(int j=0;j<width-1;j++) {
                    bits[i][j] = bits[i - 1][j];
                }
            }
        }

        for(int i=0; i<height; i++) {
            for(int j=0; j<width-1; j++) {
                System.out.print(bits[i][j]);
            }
            System.out.println();
        }
    }

    public void readMethod(ArrayList<ArrayList<Integer>> counts) {
        for(int i=0; i<height-1; i++) {
            ArrayList<Integer> current = counts.get(i);
            ArrayList<Integer> next = counts.get(i+1);

            int size;
            if(current.size() < next.size()) {
                size = current.size();
            } else {
                size = next.size();
            }

            for(int j=0; j<size; j++) {
                if(Math.abs(current.get(j) - next.get(j)) >= 3) {
                    System.out.println("Не повторяем");
                    bits[i][width] = 'n';
                } else {
                    System.out.println("Повторяем");
                    bits[i][width] = 'y';
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getCounts() {
        ArrayList<ArrayList<Integer>> counts = new ArrayList<>();

        for(int i=0; i<height; i++) {
            boolean white = true;
            ArrayList<Integer> countCurrent = new ArrayList<>();
            int count = 0;
            for(int j=0; j<width - 1; j++) {
                System.out.print(bits[i][j]);

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
            System.out.println();
        }
        System.out.println();
        for(ArrayList<Integer> list : counts) {
            for(Integer i : list) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        return counts;
    }
}
