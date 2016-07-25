package fatec.tg.SignsOn;

public class NopModel implements IGestureDetectModel{
    @Override
    public void event(long time, byte[] data) {
    }

    @Override
    public void setAction(IGestureDetectAction action) {
    }


    @Override
    public void action() {

    }
}
