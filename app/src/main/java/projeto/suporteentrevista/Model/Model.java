package projeto.suporteentrevista.Model;

import android.widget.CheckBox;

public class Model {
    private String text;
    private boolean isChecked;
    private CheckBox checkBox;

    //constructors
    public Model() {
    }

    public Model(String text) {
        this.isChecked = true;
        this.text = text;

    }

    public Model(String text, boolean isChecked) {
        this.isChecked = isChecked;
        this.text = text;

    }

    public String getText() {
        return text;
    }

    public boolean getIsChecked(){
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public void changeCheck(){
        this.isChecked = !this.isChecked;
    }

    public void setText(String text) {
        this.text = text;
    }

}
