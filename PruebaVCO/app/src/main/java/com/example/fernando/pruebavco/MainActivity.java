package com.example.fernando.pruebavco;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private PdUiDispatcher dispatcher;

    private void initPD() throws IOException {
        int sampleRate = AudioParameters.suggestSampleRate();
        PdAudio.initAudio(sampleRate, 0, 2, 8, true);
        dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);
    }

    private void initGUI(){

        final SeekBar sliderDeFrecuencia = (SeekBar) findViewById(R.id.seekBarDeFrecuencia);
        //botones
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.button3);
        final Button button4 = (Button) findViewById(R.id.button4);
        //COMIENZO SLIDER 1
        sliderDeFrecuencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                float value = (float) (300.0 + progress * 7);
                String valorDelFloat = Float.toString(value);
                //PdBase.sendFloat("frecuencia", 1000.0f);
                PdBase.sendFloat("frecuencia_VCO", value);
                // dispatcher.receiveFloat("frecuencia", value); //linea agregada para probar lo de frecuencia
                Toast.makeText(MainActivity.this,
                        valorDelFloat, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){

            }
        });
        //FIN SLIDER 1

    }

    private void loadPDPatch() throws IOException{
        //CARGO PATCH 1
        File dir = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.vcopatch), dir, true);
        File pdPatch = new File(dir, "X_VCO.pd");
        PdBase.openPatch(pdPatch.getAbsolutePath());
        File pdPatch2 = new File(dir, "prueba_X_VCO.pd");
        PdBase.openPatch(pdPatch2.getAbsolutePath());
        /*
        //CARGO PATCH 2
        File dir2 = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.simplepatch2), dir, true);
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            initPD();
            loadPDPatch();
        }catch(IOException e){
            finish();
        }

        initGUI();
    }

    @Override
    protected void onResume(){
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        PdAudio.stopAudio();
    }


    //reconocer botones (emprolijarlo)!!!
    public void generarNota1(View view){
        Float value = 1.0f;
        PdBase.sendFloat("nota", value);
    }

    public void generarNota2(View view){
        Float value = 2.0f;
        PdBase.sendFloat("nota", value);
    }

    public void generarNota3(View view){
        Float value = 3.0f;
        PdBase.sendFloat("nota", value);
    }

    public void generarOctava(View view){
        Float value = 4.0f;
        PdBase.sendFloat("num_de_octava", value);
    }
}