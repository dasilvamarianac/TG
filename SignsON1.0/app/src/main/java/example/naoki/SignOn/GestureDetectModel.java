package example.naoki.SignOn;

/**
 * Created by naoki on 15/04/16.
 */
public class GestureDetectModel implements IGestureDetectModel{
    private final static Object LOCK = new Object();

    private IGestureDetectAction action;
    private GestureDetectMethod detectMethod;

    public GestureDetectModel(GestureDetectMethod method) {
        detectMethod = method;
    }

    @Override
    public void event(long time, byte[] data) {
        synchronized (LOCK) {
            String gestureState = detectMethod.getDetectGesture(data);
            action(gestureState);
        }
    }

    @Override
    public void setAction(IGestureDetectAction action) {
        this.action = action;
    }

    @Override
    public void action() {
        action.action("DETECT");
    }

    public void action(String message) {
        action.action(message);
    }


}
