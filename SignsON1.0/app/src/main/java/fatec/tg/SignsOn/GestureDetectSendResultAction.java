package fatec.tg.SignsOn;

/**
 * Created by naoki on 15/04/16.
 */
public class GestureDetectSendResultAction implements IGestureDetectAction {
    MyoActivity activity;

    public GestureDetectSendResultAction(MyoActivity mainActivity){
        activity = mainActivity;
    }

    @Override
    public void action(String Tag ) {
        switch (Tag) {
            case "SAVE":
                activity.setGestureText("Teach Me Another");
                activity.startNopModel();
                break;
            case "SAVED":
                activity.setGestureText("Detect Ready");
                activity.startNopModel();
                break;
            default:
                activity.setGestureText(Tag);
                break;
        }
    }
}
