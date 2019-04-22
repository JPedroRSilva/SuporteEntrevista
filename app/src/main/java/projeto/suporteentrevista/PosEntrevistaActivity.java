package projeto.suporteentrevista;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import projeto.suporteentrevista.Pergunta.Pergunta;
import projeto.suporteentrevista.Zipper.ZipUtils;

public class PosEntrevistaActivity extends AppCompatActivity {

    /*****************Variaveis ****************/
    private TextView resumoView;
    private EditText notasView;
    private ImageButton terminarBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posentrevista);

        resumoView = (TextView) findViewById(R.id.resumoTextView);
        notasView = (EditText) findViewById(R.id.notasEditText);
        terminarBtn = (ImageButton) findViewById(R.id.terminarBtn);

        Intent dados = getIntent();

        File directoria = new File(dados.getStringExtra("directory"));
        String name = dados.getStringExtra("name");
        ArrayList<Pergunta> perguntaArrayList = dados.getParcelableArrayListExtra("arrayListP");
        resumoView.setText("");
        for(Pergunta p: perguntaArrayList){
            resumoView.append(p.toString());
        }

        terminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminarBtn.setEnabled(false);
                ZipUtils zip = new ZipUtils(name + ".zip", directoria.toString());
                zip.generateFileList(new File(directoria.toString()));
                zip.zipIt(name + ".zip");
            }
        });
    }

    public void preencheResumo(){;}
}
