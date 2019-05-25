import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageHandler {
    private int height;
    private int width;

    public int getWidth() {
        return width;
    }

    //Считывание картинки в матрицу бит
    public WritableRaster readFile(String url, ArrayList<String> buffer) throws IOException {
        BufferedImage image = ImageIO.read(new File(url));
        WritableRaster raster = image.getRaster();

        StringBuilder s;
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
        return raster;
    }

    //Создаем картинку
    public void createImage(WritableRaster raster, ArrayList<String> buffer) throws IOException {
        BufferedImage fin = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_INT_RGB);
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
}
