package example.naoki.SignOn;


public class GestureDetectSendResultAction implements IGestureDetectAction {
    MyoActivity activity;

    public GestureDetectSendResultAction(MyoActivity translateActivity){
        activity = translateActivity;
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
