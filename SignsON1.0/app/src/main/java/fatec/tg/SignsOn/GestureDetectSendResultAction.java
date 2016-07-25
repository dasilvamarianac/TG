package fatec.tg.SignsOn;

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
            case "":
                activity.setGestureText("");
                break;
            default:
                activity.setGestureTextCustom(Tag);
                activity.startNopModel();
                break;
        }
    }
}
