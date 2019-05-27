import java.util.ArrayList;

public class InfoPrinter {
    //Вывод матрицы бит
    public static void printBitsMatrix(ArrayList<String> buffer) {
        System.out.println("Bits:");
        for (String str : buffer) {
            for (char c : str.toCharArray()) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println(buffer.size());
    }

    //Вывод последовательностей повторений
    public static void printRepeats(ArrayList<ArrayList<Integer>> counts) {
        System.out.println("Repeats:");
        //counts = getCounts();
        for (ArrayList<Integer> list : counts) {
            for (Integer count : list) {
                System.out.print(count + " ");
            }
            System.out.println();
        }
    }
}
