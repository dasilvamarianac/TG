package fatec.tg.SignsOn;

public class SignalListView {
    private String texto;
    private String iconeRid;
    private int status;

    public SignalListView()
    {
    }

    public SignalListView(String texto, String iconeRid, int status)
    {
        this.texto = texto;
        this.iconeRid = iconeRid;
        this.status = status;
    }

    public String getIconeRid()
    {
        return iconeRid;
    }

    public void setIconeRid(String iconeRid)
    {
        this.iconeRid = iconeRid;
    }

    public String getTexto()
    {
        return texto;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

}
