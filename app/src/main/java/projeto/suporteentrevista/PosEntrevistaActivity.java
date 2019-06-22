package projeto.suporteentrevista;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        preencheResumo();

        terminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ficheiroXML();
                ficheiroNotas();
                String fileZip = directoria.toString() + ".zip";
                ZipUtils zip = new ZipUtils(fileZip, directoria.toString());
                zip.generateFileList(new File(directoria.toString()));
                zip.zipIt(fileZip);
                Intent intent = new Intent(PosEntrevistaActivity.this, InicioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void preencheResumo(){
        resumoView.setText("");
        for(Pergunta p: perguntaArrayList){
            resumoView.append(p.toString());
        }
    }

    public void ficheiroNotas(){
        FileWriter writer;

        try{
            writer = new FileWriter(new File(directoria.toString() + File.separator + "Notas" + ".txt"));
            String snotas = notasView.getText().toString();
            writer.write(String.format("Nome do entrevistado: %s\n", nameNP));
            writer.write("Nome do entrevistador: *A preencher*\n");
            Date date = new Date();
            String strDateFormat = "yyyy-MM-dd";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            writer.write(String.format("Data da entrevista: %s\n",dateFormat.format(date)));
            writer.write("Notas a relembrar dadas pelo entrevistador: \n");
            writer.write(snotas);
            writer.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    private void ficheiroXML() {
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
    }
}
