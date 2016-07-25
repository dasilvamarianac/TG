package fatec.tg.SignsOn;

import java.util.ArrayList;

public class GestureDetectMethod {
    private final static int STREAM_DATA_LENGTH = 5;
    private final static Double THRESHOLD = 0.05;

    private final ArrayList<EmgData> compareGesture;
    private final String[] letter = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","y","x","z"};

    private int streamCount = 0;
    private EmgData streamingMaxData;
    private Double detect_distance;
    private int detect_Num;
    private int comp;

    private NumberSmoother numberSmoother = new NumberSmoother();

    public GestureDetectMethod(ArrayList<EmgData> gesture) {

        compareGesture = gesture;
        comp = compareGesture.size();
    }

    private String getEnum(int i_gesture) {
        for(int i = 0 ; i < comp; i++ ){
            if (i_gesture == i){
                return letter[i_gesture];
            }
        }
        return("");
    }

    public String getDetectGesture(byte[] data) {
        EmgData streamData = new EmgData(new EmgCharacteristicData(data));
        streamCount++;
        if (streamCount == 1){
            streamingMaxData = streamData;
        } else {
            for (int i_element = 0; i_element < 8; i_element++) {
                if (streamData.getElement(i_element) > streamingMaxData.getElement(i_element)) {
                    streamingMaxData.setElement(i_element, streamData.getElement(i_element));
                }
            }
            if (streamCount == STREAM_DATA_LENGTH){
                detect_distance = getThreshold();
                detect_Num = -1;
                for (int i_gesture = 0;i_gesture < comp ;i_gesture++) {
                    EmgData compData = compareGesture.get(i_gesture);
                    double distance = distanceCalculation(streamingMaxData, compData);
                    if (detect_distance > distance) {
                        detect_distance = distance;
                        detect_Num = i_gesture;
                    }
                }
                numberSmoother.addArray(detect_Num);
                streamCount = 0;
            }
        }
        return getEnum(numberSmoother.getSmoothingNumber(comp));
    }

    private double getThreshold() {

        return THRESHOLD;

    }

	// 2 vectors distance devied from each vectors norm.
    private double distanceCalculation(EmgData streamData, EmgData compareData){
        double return_val = streamData.getDistanceFrom(compareData)/streamData.getNorm()/compareData.getNorm();
        return return_val;
    }

	// Mathematical [sin] value of 2 vectors' inner angle.
    private double distanceCalculation_sin(EmgData streamData, EmgData compareData){
        double return_val = streamData.getInnerProductionTo(compareData)/streamData.getNorm()/compareData.getNorm();
        return return_val;
    }

	// Mathematical [cos] value of 2 vectors' inner angle from low of cosines.
    private double distanceCalculation_cos(EmgData streamData, EmgData compareData){
        double streamNorm  = streamData.getNorm();
        double compareNorm = compareData.getNorm();
        double distance    = streamData.getDistanceFrom(compareData);
        return (Math.pow(streamNorm,2.0)+Math.pow(compareNorm,2.0)-Math.pow(distance,2.0))/streamNorm/compareNorm/2;
    }




}
