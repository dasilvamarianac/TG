package fatec.tg.SignsOn;

public interface IGestureDetectModel {
    public void event(long eventTime, byte[] data);
    public void setAction(IGestureDetectAction action);
    public void action();
}
