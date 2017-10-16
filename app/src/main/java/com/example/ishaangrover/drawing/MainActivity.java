package com.example.ishaangrover.drawing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends Activity {
    byte[] drawing;

    private String serverIpAddress = "192.168.1.241";// "192.168.1.187";

    private boolean connected = false;

    private Handler handler = new Handler();

    private Socket socket;

    // correct, incorrect, erase, done --> bottom
    Button correct, incorrect, erase, done, letter, number, picture;

    // drawing canvas
    DrawingView drawingBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingBoard = (DrawingView) findViewById(R.id.single_touch_view);

        drawButtons();
        //setSubmitButton();
        setButtons(); // You can fill inside this method
    }

    public void drawButtons() {
        correct = new Button(this);
        incorrect = new Button(this);
        erase = new Button(this);
        done = new Button(this);
        letter = new Button(this);
        number = new Button(this);
        picture = new Button(this);

        // set text inside buttons
        correct.setText("Correct");
        incorrect.setText("Incorrect");
        erase.setText("Start Over");
        done.setText("Done");
        letter.setText("Letter");
        number.setText("Number");
        picture.setText("Picture");

        // set font size
        correct.setTextSize(30);
        incorrect.setTextSize(30);
        erase.setTextSize(30);
        done.setTextSize(30);
        letter.setTextSize(38);
        number.setTextSize(38);
        picture.setTextSize(38);

        // set layouts
        LinearLayout bottomButtons = new LinearLayout(this.getApplicationContext());
        bottomButtons.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout topButtons = new LinearLayout(this.getApplicationContext());
        topButtons.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams topLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams bottomLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        bottomLp.gravity =  Gravity.BOTTOM;

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1;

        bottomButtons.addView(correct, lp2);
        bottomButtons.addView(incorrect, lp2);
        bottomButtons.addView(erase, lp2);
        bottomButtons.addView(done, lp2);
        bottomButtons.setGravity(Gravity.BOTTOM);
        bottomButtons.setPadding(0, 0, 0, 100);

        topButtons.addView(letter, lp2);
        topButtons.addView(number, lp2);
        topButtons.addView(picture, lp2);
        topButtons.setPadding(0, 100, 0, 0);

        this.addContentView(topButtons, topLp);
        this.addContentView(bottomButtons, bottomLp);
    }

    public void setSubmitButton() {
        // submit button?
        Button submit_button = (Button) findViewById(R.id.button_submit);
        submit_button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                //drawingBoard = (DrawingView) findViewById(R.id.single_touch_view);
                //drawing = drawingBoard.getDrawing();

                //if (!connected) {
                //    Thread cThread = new Thread(new ClientThread());
                //    cThread.start();
               // }
            }
        });
    }

    public void setButtons() {
        correct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "correct clicked");
            }}
        );

        incorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "incorrect clicked");
            }}
        );

        erase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "erase clicked");
                drawingBoard.clearCanvas();
            }}
        );

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "done clicked");
                drawingBoard = (DrawingView) findViewById(R.id.single_touch_view);
                drawing = drawingBoard.getDrawing();

                if (!connected) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }}
        );

        letter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "letter clicked");
            }}
        );

        number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "number clicked");
            }}
        );

        picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "picture clicked");
            }}
        );
    }

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                socket = new Socket(serverAddr, 12344);
                connected = true;
                while (connected) {
                    try {


                    /*File myFile = new File (filepath);
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    OutputStream os = socket.getOutputStream();
                    Log.d("ClientActivity", "C: Sending command.");
                    //System.out.println("Sending...");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();*/

                        Log.d("ClientActivity", "C: Sending command.");
                    /*PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);*/
                        // WHERE YOU ISSUE THE COMMANDS

                        OutputStream output = socket.getOutputStream();
                        Log.d("ClientActivity", "C: image writing.");
                        output.write(drawing);
                        output.flush();
                        connected = false;
                        // out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }

    protected void onStop() {
        super.onStop();
        try {
            // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
            socket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}