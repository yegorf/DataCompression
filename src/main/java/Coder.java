import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Coder {

    private int height;
    private int width;

    private char[][] bits;

    public StringBuffer read(String url) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedImage image = ImageIO.read(new File(url));
        WritableRaster raster = image.getRaster();
        width = raster.getWidth();
        height = raster.getHeight();
        bits = new char[height][width + 1];//Последний стоб - длля повторителей

        for(int y = 0; y < height; y++){
            int i = 0;
            for(int x = 0; x < width; x++){
                if(image.getRGB(x, y) == -16777216){
                    sb.append("0");
                    bits[y][x] = '0';
                }
                else{
                    sb.append("1");
                    bits[y][x] = '1';
                }
                i++;
                if(i == width) {
                    sb.append("\n");
                    i = 0;
                }
            }
        }

        readCoding();
        return sb;
    }

    //Дищман
    public void readCoding() {
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                System.out.print(bits[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }



    public void stringToByte(String string) {
        byte[] b = string.getBytes();
        System.out.println(b);

        String[] arr = string.split("");
        int l = 0;
        for(String s : arr) {

            byte[] bt = s.getBytes();
            String ss = "";
            for(int i=0; i< bt.length; i++) {
                ss+=bt[i];
                l++;
            }
        }

        System.out.println(l);
    }
}
