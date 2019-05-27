import java.util.ArrayList;

public class BinCalculator {
    private final int BYTE_SIZE = 8;

    public void toBin(StringBuilder s, ArrayList<Integer> haffmanBytes, int len) {
        for (int j = 0; j < haffmanBytes.size(); j++) {
            String strX = Integer.toBinaryString(haffmanBytes.get(j));
            if (strX.length() < BYTE_SIZE) {
                for (int i = 0; i < BYTE_SIZE - strX.length(); i++) {
                    s.append("0");
                }
                s.append(strX);
            } else {
                s.append(Integer.toBinaryString(haffmanBytes.get(j)));
            }
        }
        s.delete(len, s.length());
    }

    public void toDec(StringBuilder s,ArrayList<Integer> haffmanBytes) {
        while (s.length() != 0) {
            if (s.length() < BYTE_SIZE) {
                haffmanBytes.add(Integer.parseInt(s.substring(0), 2));
                s.delete(0, s.length());
            } else {
                haffmanBytes.add(Integer.parseInt(s.substring(0, BYTE_SIZE), 2));
                s.delete(0, BYTE_SIZE);
            }
        }
    }
}
