package com.example.wolfssl;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wolfssl.provider.jsse.WolfSSLProvider;

import java.security.Security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Enable WolfJSEE Debug Messages
                            System.setProperty("wolfjsse.debug", "true");
                            Security.insertProviderAt(new WolfSSLProvider(), 1);

                            /* not setting up KeyStore or TrustStore, wolfJSSE will load
                             * CA certs from the Android system KeyStore by default. */
                            SSLContext context = SSLContext.getInstance("TLS");
                            context.init(null, null, null);

                            SSLSocketFactory socketFactory = context.getSocketFactory();
                            SSLSocket socket = (SSLSocket) socketFactory.createSocket("www.wolfssl.com", 443);
                            socket.startHandshake();

                            socket.close();
                            console.log(TAG, "TLS Connection Successful");
                        } catch (Exception e) {
                            e.printStackTrace();
                            console.log(TAG, "TLS Connection Failed");
                        }
                    }
                });
            }
        });
    }
}