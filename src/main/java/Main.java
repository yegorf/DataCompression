public class Main {
    public static void main(String[] args) throws Exception {
        HuffmanCoding coder = new HuffmanCoding();
        coder.encode("test.bmp");
        coder.decode("result.bmp");
    }
}
