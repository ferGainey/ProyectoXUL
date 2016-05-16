package com.example.fernando.prueba1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

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

   private void initPD() throws IOException{
        int sampleRate = AudioParameters.suggestSampleRate();
        PdAudio.initAudio(sampleRate, 0, 2, 8, true);
        dispatcher = new PdUiDispatcher();
       PdBase.setReceiver(dispatcher);
   }

    private void initGUI(){
        final Switch onOffSwitch = (Switch) findViewById(R.id.onOffSwitch);
        final Switch frecChangeSwitch = (Switch) findViewById(R.id.frecChangeSwitch);
        final SeekBar sliderDeFrecuencia = (SeekBar) findViewById(R.id.seekBarDeFrecuencia);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.i("onOffSwitch", String.valueOf(isChecked)); //esto estaba antes, despues lo saco para poder conectar el sonido a mi app
                float val = (isChecked) ? 1.0f : 0.0f;
                PdBase.sendFloat("onOff", val);
            }
        }); // se saco el switch para probar otra cosa*/

        frecChangeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                float val = (isChecked) ? 500.0f : 950.0f;
                PdBase.sendFloat("cambio", val);
            }
        });
        sliderDeFrecuencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                    float value = (float) (300.0 + progress * 7);
                    String valorDelFloat = Float.toString(value);
                    //PdBase.sendFloat("frecuencia", 1000.0f);
                    PdBase.sendFloat("frecuencia", value);
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
    }

    private void loadPDPatch() throws IOException{
        File dir = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.simplepatch), dir, true);
        File pdPatch = new File(dir, "simplepatch.pd");
        PdBase.openPatch(pdPatch.getAbsolutePath());
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
}
