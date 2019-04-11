package projeto.suporteentrevista;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import projeto.suporteentrevista.Adapter.MyAdapter;
import projeto.suporteentrevista.Model.Model;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import edmt.dev.advancednestedscrollview.AdvancedNestedScrollView;
import edmt.dev.advancednestedscrollview.MaxHeightRecyclerView;

public class PreEntrevistaActivity extends AppCompatActivity {

    private boolean isShowingCardHeaderShadow;
    List<Model> modelList = new ArrayList<>();
    TextView nomeBox;

    private FloatingActionButton btnNovaPergunta;
    private EditText editTextNovaPergunta;
    private FloatingActionButton btnIniciarEntrevista;
    // nomeText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preentrevista);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /***************atualizar as perguntas********************/
        generateModelList();

        /**nova pergunta*/
        btnNovaPergunta = (FloatingActionButton)findViewById(R.id.btnNovaPergunta);
        editTextNovaPergunta = (EditText)findViewById(R.id.editTextNovaPergunta);
        btnIniciarEntrevista = (FloatingActionButton)findViewById(R.id.beginBtn);

        btnNovaPergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pergunta = editTextNovaPergunta.getText().toString();
                if(pergunta.isEmpty())return;
                modelList.add(new Model(pergunta));
                editTextNovaPergunta.setText("");

            }
        });
        /****************/

        /******************iniciar entrevista*************************/

        nomeBox = (TextView) findViewById(R.id.nomeText);

        btnIniciarEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeBox.getText().toString();
                ArrayList<String> perguntas = new ArrayList<>();
                Model m;
                for(int i = 0; i < modelList.size(); i++){
                    m = modelList.get(i);
                    if(m.getIsChecked()) {
                        perguntas.add(m.getText());
                    }
                }
                if(nome.isEmpty()) return;
                String nomeProcessado = unaccent(nome);
                //Toast.makeText(getBaseContext(), nomeProcessado, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PreEntrevistaActivity.this, EntrevistaActivity.class);
                intent.putStringArrayListExtra("Perguntas", perguntas);
                intent.putExtra("Nome", nomeProcessado.toString());
                PreEntrevistaActivity.this.startActivity(intent);
            }
        });

        /**** CAROUSSEL DE PERGUNTAS */
        final MaxHeightRecyclerView rv = (MaxHeightRecyclerView)findViewById(R.id.card_recycler_view);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(new MyAdapter(this,modelList));
        rv.addItemDecoration(new DividerItemDecoration(this,lm.getOrientation()));

        final View cardHeaderShadow = findViewById(R.id.card_header_shadow);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                boolean isRecyclerViewScrolledToTop = lm.findFirstVisibleItemPosition() == 0
                        && lm.findViewByPosition(0).getTop() == 0;
                if(!isRecyclerViewScrolledToTop && !isShowingCardHeaderShadow) {
                    isShowingCardHeaderShadow = true;
                    showOrHideView(cardHeaderShadow, isShowingCardHeaderShadow);
                }else{
                    isShowingCardHeaderShadow = false;
                    showOrHideView(cardHeaderShadow, isShowingCardHeaderShadow);
                }

            }
        });

        AdvancedNestedScrollView advancedNestedScrollView = (AdvancedNestedScrollView)findViewById(R.id.nested_scroll_view);
        advancedNestedScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        advancedNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == 0 && oldScrollY > 0){
                    //reset the recyclerview's croll position each time the card return to its starting position
                    rv.scrollToPosition(0);
                    cardHeaderShadow.setAlpha(0f);
                    isShowingCardHeaderShadow = false;
                }
            }
        });

    }

    private void showOrHideView(View view, boolean isShow) {
        view.animate().alpha(isShow ? 1f:0f).setDuration(100).setInterpolator(new DecelerateInterpolator());
    }

    /*
    private void generateModelList() {
        modelList.add(new Model("https://www.almadeviajante.com/wp-content/uploads/riad-marraquexe-farhan-750x420.jpg","cenas 1fixes uma pergunta um pouco mais longa para ver como é que fica"));

        modelList.add(new Model("https://www.almadeviajante.com/wp-content/uploads/riad-marraquexe-farhan-750x420.jpg","cenas 2fixes"));

        modelList.add(new Model("https://www.almadeviajante.com/wp-content/uploads/riad-marraquexe-farhan-750x420.jpg","cenas 3fixes"));

        modelList.add(new Model("https://www.almadeviajante.com/wp-content/uploads/riad-marraquexe-farhan-750x420.jpg","cenas 4fixes"));
    }
    */

    private void generateModelList() {

        /***********************************************************************/
        String nome = "á éááá áááàó õ";

        /***********************************************************************/
        modelList.add(new Model("cenas 1fixes uma pergunta um pouco mais longa para ver como é que fica"));

        modelList.add(new Model("cenas 2fixes",false));

        modelList.add(new Model("cenas 3fixes"));

        modelList.add(new Model("cenas 4fixes"));

        modelList.add(new Model("SWAG"));
    }

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("([^\\p{ASCII}]|\\s)*" , "");
    }

}
