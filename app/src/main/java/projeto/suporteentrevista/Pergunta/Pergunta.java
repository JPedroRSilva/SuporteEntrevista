package projeto.suporteentrevista.Pergunta;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Pergunta implements Parcelable {

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
        this.time = "Pergunta não efetuada!";
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

    public void changeCheck(){
        this.checked = !this.checked;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Pergunta: " + this.getTexto() + "\n");
        if(checked)
            sb.append("Tempo da pergunta: " + this.time + "\n");
        else sb.append(this.time + "\n");
        return sb.toString();
    }

    // Parcelling part
    public Pergunta(Parcel in){
        this.texto = in.readString();
        if(in.readInt() == 1)
            checked = true;
        else checked = false;
        this.time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(texto);
        if(checked)
            dest.writeInt(1);
        else dest.writeInt(0);
        dest.writeString(time);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Pergunta createFromParcel(Parcel in) {
            return new Pergunta(in);
        }

        public Pergunta[] newArray(int size) {
            return new Pergunta[size];
        }
    };
}
