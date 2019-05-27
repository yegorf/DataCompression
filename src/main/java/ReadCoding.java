import java.util.ArrayList;

public class ReadCoding {
    public void readMethod(ArrayList<ArrayList<Integer>> counts, int height, ArrayList<String> buffer) {
        for (int i = 0; i < height - 1; i++) {
            ArrayList<Integer> current = counts.get(i);
            ArrayList<Integer> next = counts.get(i + 1);
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

        deleteLines(counts, buffer);
    }
    public void readMethodWithDeletes(ArrayList<ArrayList<Integer>> counts, ArrayList<String> buffer) {
        for (int i = 0; i < buffer.size() - 1; i++) {
            ArrayList<Integer> current = counts.get(i);
            ArrayList<Integer> next = counts.get(i + 1);

            int size = current.size() < next.size() ? current.size() : next.size();
            boolean repeat = false;

            int cur = 0, nex = 0;
            for (int j = 0; j < size; j++) {
                cur += current.get(j) + 1;
                nex += next.get(j) + 1;
                if (cur == nex && current.size() == 1 && next.size() == 1) {
                    //   System.out.println("Повторяем");
                    repeat = true;
                } else {
                    if (Math.abs(cur - nex) >= 3) {
                        //     System.out.println("Не повторяем");
                        repeat = false;
                        break;
                    } else {
                        //   System.out.println("Повторяем");
                        repeat = true;
                    }
                }

            }
            if (repeat) {
                buffer.set(i, buffer.get(i) + "p");
                buffer.remove(i + 1);
                counts.remove(i + 1);
                i--;
            }
        }
    }


    public void deleteLines(ArrayList<ArrayList<Integer>> counts, ArrayList<String> buffer) {
        for (int i = 0; i < buffer.size() - 1; i++) {
            int ind = i;
            while (i + 1 < buffer.size() && buffer.get(i + 1).contains("y")) {
                buffer.set(ind, buffer.get(ind) + "p");
                buffer.remove(i + 1);
                counts.remove(i + 1);
            }
        }
    }
}
