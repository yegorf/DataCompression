public class Main {
    public static void main(String[] args) throws Exception {
        Coder coder = new Coder();
        StringBuffer string = coder.read("image.bmp");

        System.out.println(string);
    }
}
