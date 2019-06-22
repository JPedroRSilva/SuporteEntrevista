package projeto.suporteentrevista;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import projeto.suporteentrevista.Pergunta.Pergunta;
import projeto.suporteentrevista.Zipper.ZipUtils;

public class PosEntrevistaActivity extends AppCompatActivity {

    /*****************Variaveis ****************/
    private TextView resumoView;
    private EditText notasView;
    private ImageButton terminarBtn;
    File directoria;
    String name, nameNP;
    ArrayList<Pergunta> perguntaArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posentrevista);

        resumoView = (TextView) findViewById(R.id.resumoTextView);
        resumoView.setMovementMethod(new ScrollingMovementMethod());
        notasView = (EditText) findViewById(R.id.notasEditText);
        terminarBtn = (ImageButton) findViewById(R.id.terminarBtn);

        Intent dados = getIntent();

        directoria = new File(dados.getStringExtra("directory"));
        name = dados.getStringExtra("name");
        nameNP = dados.getStringExtra("nameNP");

        perguntaArrayList = dados.getParcelableArrayListExtra("arrayListP");
        resumoView.setText("");
        for(Pergunta p: perguntaArrayList){
            resumoView.append(p.toString());
        }

        terminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileWriter fw;
                try{

                    fw = new FileWriter(new File(directoria.toString() + File.separator + name + ".xml"));
                    fw.write("?xml version= \"1.0\" encoding = \" \" ?");
                    fw.write("<bi>\n");
                    fw.write("\t <projecto> <\\projecto>\n");
                    fw.write(String.format("\t <depoente>%s<\\depoente>\n", nameNP));
                    fw.write("\t <entrevistador> <\\entrevistador>\n");
                    fw.write("\t <bibliografia> <\\bibliografia>\n");
                    fw.write("<\\bi>\n");
                    for(Pergunta p: perguntaArrayList){
                        if(p.isChecked()){
                            fw.write(String.format("<pergunta>%s<\\pergunta>\n", p.getTexto()));
                            fw.write(String.format("<resposta><p> %s <\\p><\\pergunta>\n", p.getTime()));
                        }
                    }
                    fw.close();

                }catch (IOException e){
                    e.printStackTrace();
                }

                terminarBtn.setEnabled(false);
                String fileZip = directoria.toString() + ".zip";
                ZipUtils zip = new ZipUtils(fileZip, directoria.toString());
                zip.generateFileList(new File(directoria.toString()));
                zip.zipIt(fileZip);
            }
        });
    }

    public void preencheResumo(){;}
}
