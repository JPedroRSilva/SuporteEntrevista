package projeto.suporteentrevista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class PosEntrevistaActivity extends AppCompatActivity {

    /*****************Variaveis ****************/
    private TextView resumoView;
    private EditText notasView;
    private ImageButton terminarBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resumoView = (TextView) findViewById(R.id.resumoTextView);
        notasView = (EditText) findViewById(R.id.notasEditText);
        terminarBtn = (ImageButton) findViewById(R.id.terminarBtn);

        //preencheResumo()
    }
}
