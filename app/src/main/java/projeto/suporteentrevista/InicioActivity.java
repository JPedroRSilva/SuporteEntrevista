package projeto.suporteentrevista;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class InicioActivity  extends AppCompatActivity {

    private static final int PERMISSION_ALL = 4;
    private ImageButton newBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions();
        setContentView(R.layout.inicio);

        newBtn = (ImageButton) findViewById(R.id.novaBtn);

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, PreEntrevistaActivity.class);
                InicioActivity.this.startActivity(intent);
            }
        });
    }


    private void permissions() {
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            permissionsGranted();
        }
    }

    private void permissionsGranted() {
        Toast.makeText(this, "Permissões configuradas.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case PERMISSION_ALL:
                if(hasAllPermissionsGranted(grantResults)){
                    permissionsGranted();
                }else{
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            new AlertDialog.Builder(this)
                                    .setMessage("É necessário dê as autorizações para o uso da aplicação")
                                    .setPositiveButton("Allow", (dialog, which) ->  ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL))
                                    .setNegativeButton("Cancel", (dialog, which) -> this.finish())
                                    .create()
                                    .show();
                        }
                    }
                }
                break;
        }
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
