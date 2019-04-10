/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package projeto.suporteentrevista.Camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import projeto.suporteentrevista.EntrevistaActivity;
import projeto.suporteentrevista.R;


public class CameraActivity extends AppCompatActivity {

    String directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            Intent intent = getIntent();
            directory = intent.getStringExtra("directory");
            Bundle args = new Bundle();
            try {
                if (!directory.isEmpty()) {
                    args.putString("directory", directory);
                    Log.i("Diretoria", "Foi recebida a diretoria com sucesso!" + directory);
                } else {
                    Log.i("Diretoria", "Não foi enviada a diretoria!");
                }
            }catch (NullPointerException e){
            }
            Camera2BasicFragment fragment = Camera2BasicFragment.newInstance();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).disallowAddToBackStack()
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
