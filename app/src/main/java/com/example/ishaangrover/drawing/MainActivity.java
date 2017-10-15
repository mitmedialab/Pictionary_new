package com.example.ishaangrover.drawing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends Activity {
    byte[] drawing;

    private String serverIpAddress = "192.168.1.187";

    private boolean connected = false;

    private Handler handler = new Handler();

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit_button = (Button) findViewById(R.id.button_submit);
        submit_button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                DrawingView drawingBoard = (DrawingView) findViewById(R.id.single_touch_view);
                drawing = drawingBoard.getDrawing();

                if (!connected) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }



            }
        });
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