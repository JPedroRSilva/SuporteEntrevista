package projeto.suporteentrevista;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import projeto.suporteentrevista.Camera.CameraActivity;
import projeto.suporteentrevista.Pergunta.Pergunta;

public class EntrevistaActivity extends AppCompatActivity {

    private String name;
    private File directory;
    private ArrayList<String> perguntas;
    private ListView listaView;
    private ImageButton cameraBtn, pauseResBtn,finishBtn;
    private Chronometer chronometer;
    private long pauseOffset;
    private MediaRecorder audio;
    private int numberAudio;
    private String audioFile;
    private boolean isPaused;
    private boolean startResume;
    private boolean end;
    private ArrayList<String> listAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPaused = false;
        numberAudio = 0;

        listAudio = new ArrayList<>();

        Intent intent = getIntent();

        name = intent.getStringExtra("Nome");
        directory = createNewDirectory(name);

        /***************Começar o cronometro ***********************/
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        /***************mostrar perguntas ***********************/

        listaView = (ListView) findViewById(R.id.listaView);

        perguntas = intent.getStringArrayListExtra("Perguntas");

        ArrayList<Pergunta> arrayPerguntas = new ArrayList<Pergunta>();

        for(String p: perguntas){
            arrayPerguntas.add(new Pergunta(p));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked, perguntas);
        listaView.setAdapter(arrayAdapter);
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pergunta m = arrayPerguntas.get(position);
                m.changeCheck();
                if(m.isChecked()) {
                    m.setTime(timeToString(SystemClock.elapsedRealtime() - chronometer.getBase()));
                }
                else m.setTime("Pergunta sem tempo");

            }
        });

        /*----------------Inicializar o audio--------------*/
        audioFile = directory.toString()  + "/Audio/" + "audio" + numberAudio + ".3gp";
        prepareAudio(audioFile);

        try {
            audio.prepare();
            audio.start();
            listAudio.add(audioFile);
        }
        catch (IllegalStateException ise){
            Log.i("Audio", "IllegalStateExcetion!");
            audio.release();
        }catch(IOException ioe){
            Log.i("Audio", "IOException!");
            audio.release();
        }

        cameraBtn = (ImageButton) findViewById(R.id.cameraBtn);
        pauseResBtn = (ImageButton) findViewById(R.id.pauseResBtn);
        finishBtn = (ImageButton) findViewById(R.id.finishBtn);


        pauseResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pauseResBtn.setEnabled(false);

                if(!isPaused){
                    Toast.makeText(getApplicationContext(), "Audio Paused", Toast.LENGTH_LONG).show();
                    audio.stop();
                    audio.release();
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    pauseResBtn.setImageResource(R.drawable.ic_play_circle_filled_blue_64dp);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Audio resumed", Toast.LENGTH_LONG).show();
                    numberAudio+=1;
                    audioFile = directory.toString()  + "/Audio/" + "audio" + numberAudio + ".3gp";
                    listAudio.add(audioFile);
                    prepareAudio(audioFile);
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    try {
                        audio.prepare();
                        audio.start();
                    }
                    catch (IllegalStateException ise){
                        Log.i("Audio", "IllegalStateExcetion!");
                        audio.release();
                    }catch(IOException ioe){
                        Log.i("Audio", "IOException!");
                        audio.release();
                    }
                    pauseResBtn.setImageResource(R.drawable.ic_pause_circle_filled_blue_64dp);
                }
                isPaused = !isPaused;
                pauseResBtn.setEnabled(true);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused){
                    Toast.makeText(getApplicationContext(), "Audio Stoped", Toast.LENGTH_LONG).show();
                    audio.stop();
                    audio.release();
                    audio = null;
                    startResume = true;
                }
                end = true;
                String audioFinal = directory.toString()  + "/Audio/audioFinal.mp4";
                String mergeList[] = new String[listAudio.size()];
                for(int i = 0; i < listAudio.size(); i++){
                    mergeList[i] = listAudio.get(i);
                }
                mergeMediaFiles(true, mergeList, audioFinal);
                Intent finish = new Intent(EntrevistaActivity.this, PosEntrevistaActivity.class);
                finish.putExtra("arrayListP", arrayPerguntas);
                finish.putExtra("name", name);
                finish.putExtra("directory", directory);
                EntrevistaActivity.this.startActivity(finish);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentC = new Intent(EntrevistaActivity.this, CameraActivity.class);
                intentC.setFlags(intentC.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                String diretoriaS = directory.toString();
                EntrevistaActivity.this.startActivity(intentC);
                intentC.putExtra("directory", diretoriaS);
                EntrevistaActivity.this.startActivity(intentC);
            }
        });
    }

    private void prepareAudio(String audioFile) {
        audio = new MediaRecorder();
        audio.setAudioSource(MediaRecorder.AudioSource.MIC);
        audio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audio.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audio.setOutputFile(audioFile);
    }



    @Override
    protected void onResume() {
        if(startResume) {
            Toast.makeText(getApplicationContext(), "Audio resumido", Toast.LENGTH_SHORT).show();
            startResume = false;
            numberAudio += 1;
            audioFile = directory.toString() + "/Audio/" + "audio" + numberAudio + ".3gp";
            listAudio.add(audioFile);
            prepareAudio(audioFile);
            try {
                audio.prepare();
                audio.start();
            } catch (IllegalStateException ise) {
                Log.i("Audio", "IllegalStateExcetion!");
                audio.release();
            } catch (IOException ioe) {
                Log.i("Audio", "IOException!");
                audio.release();
            }
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
        }
        end = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(!isPaused && !end){
            audio.stop();
            audio.release();
            audio = null;
            startResume = true;
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
        end = false;
        super.onPause();
    }

    public File createNewDirectory(String name) {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        // create a directory before creating a new file inside it.
        String pathDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        File directory = new File(pathDir + File.separator + name + "_" + dateFormat.format(date));

        // now creates 2 sub-directories to house photos and audio.
        if (!directory.exists()) {
            if(directory.mkdirs()){
                Log.i("Diretoria", directory.toString());
            }
            else {
                Log.i("Diretoria","Diretoria principal não criada!  " + directory.toString());
            }
            File photoDir = new File(directory, "Fotos");
            File audioDir = new File(directory, "Audio");
            if(photoDir.mkdirs() && audioDir.mkdirs()){
                Log.i("Diretoria","Subdiretorias Fotos e Audio criadas");
            }
            else{
                Log.i("Diretoria","Subdiretorias Fotos e Audio não criadas  " + photoDir);
            }
        }
        else{
            Log.i("Diretoria", "Diretoria já existe!");
        }
        return directory;
    }

    public static boolean mergeMediaFiles(boolean isAudio, String sourceFiles[], String targetFile) {
        try {
            String mediaKey = isAudio ? "soun" : "vide";
            List<Movie> listMovies = new ArrayList<>();
            for (String filename : sourceFiles) {
                listMovies.add(MovieCreator.build(filename));
            }
            List<Track> listTracks = new LinkedList<>();
            for (Movie movie : listMovies) {
                for (Track track : movie.getTracks()) {
                    if (track.getHandler().equals(mediaKey)) {
                        listTracks.add(track);
                    }
                }
            }
            Movie outputMovie = new Movie();
            if (!listTracks.isEmpty()) {
                outputMovie.addTrack(new AppendTrack(listTracks.toArray(new Track[listTracks.size()])));
            }
            Container container = new DefaultMp4Builder().build(outputMovie);
            FileChannel fileChannel = new RandomAccessFile(String.format(targetFile), "rw").getChannel();
            container.writeContainer(fileChannel);
            fileChannel.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Merge de audio", "Error merging media files. exception: "+e.getMessage());
            return false;
        }
    }

    private String timeToString(long time){
        time /= 1000;
        long hours = time /3600;
        time -= hours * 3600;
        long minutes = time/60;
        time -= minutes * 60;
        long seconds = time;
        return String.valueOf(hours + "h:" + minutes + "m:" + seconds + "s");
    }
}
