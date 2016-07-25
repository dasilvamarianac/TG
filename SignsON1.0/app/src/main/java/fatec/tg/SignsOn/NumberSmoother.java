package fatec.tg.SignsOn;

import java.util.ArrayList;

public class NumberSmoother {
    private final static int SMOOTHING_LENGTH = 75;

    private ArrayList<Integer> gestureNumArray = new ArrayList<>();
    private int[] numCounter = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int storageDataCount = 0;

    public void addArray(Integer gestureNum) {
        gestureNumArray.add(gestureNum);
        if (gestureNum != -1) {
            numCounter[gestureNum]++;
        }
        storageDataCount++;
        if (storageDataCount > SMOOTHING_LENGTH) {
            int deleteNumber = gestureNumArray.get(0);
            if (deleteNumber != -1) {
                numCounter[deleteNumber]--;
            }
            gestureNumArray.remove(0);
            storageDataCount--;
        }
    }

    public int getSmoothingNumber(Integer comp) {
        for (int i_element = 0; i_element < comp; i_element++) {
            if (numCounter[i_element] >= comp) {
                return i_element;
            }
        }
        return -1;
    }
}
