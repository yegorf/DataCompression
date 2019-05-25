import java.util.ArrayList;

public class BinCalculator {
    public void toBin(StringBuilder s, ArrayList<Integer> haffmanBytes, int len) {
        for (int j = 0; j < haffmanBytes.size(); j++) {
            String strX = Integer.toBinaryString(haffmanBytes.get(j));
            if (strX.length() < 8) {
                for (int i = 0; i < 8 - strX.length(); i++) {
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
            if (s.length() < 8) {
                haffmanBytes.add(Integer.parseInt(s.substring(0), 2));
                s.delete(0, s.length());
            } else {
                haffmanBytes.add(Integer.parseInt(s.substring(0, 8), 2));
                s.delete(0, 8);
            }
        }
    }
}
