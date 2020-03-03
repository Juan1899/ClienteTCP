package com.example.clientetcp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button accion;
    Socket socket;
    BufferedWriter writer;
    boolean isUP = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accion = findViewById(R.id.accion);

        new Thread(

                ()->{
                    try {
                        socket = new Socket("10.0.2.2",5000);
                        //escritor
                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        ).start();

        accion.setOnTouchListener(
                (v, event)->{

                    switch(event.getAction()){

                        case MotionEvent.ACTION_DOWN:
                            accion.setText("DOWN");
                            isUP=false;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            accion.setText("MOVE");
                            break;

                        case MotionEvent.ACTION_UP:
                            accion.setText("UP");
                            isUP = true;
                            break;
                    }

                    return true;
                }

        );

        new Thread(
                ()->{
                    while(true){

                        while(isUP){}

                        try {
                            Thread.sleep(100);
                            writer.write("UP\n");
                            writer.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).start();
    }
}

