package projeto.suporteentrevista.Pergunta;

public class Pergunta {

    private String texto;
    private boolean checked;
    private String time;


    public Pergunta(String texto, boolean checked, String time){
        this.texto = texto;
        this.checked = checked;
        this.time = time;
    }

    public Pergunta(String texto){
        this.texto = texto;
        this.checked = false;
        this.time = "";
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTexto() {
        return texto;
    }

    public String getTime() {
        return time;
    }

    public boolean isChecked() {
        return checked;
    }

    public String checkT(){
        if(this.checked) return "true";
        return "false";
    }

    public void changeCheck(){
        this.checked = !this.checked;
    }
}
