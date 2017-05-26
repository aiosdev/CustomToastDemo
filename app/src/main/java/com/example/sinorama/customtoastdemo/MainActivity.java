package com.example.sinorama.customtoastdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExToast.makeText(MainActivity.this, "Hello, customTost-java", ExToast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, "This is native Toast", Toast.LENGTH_LONG).show();
            }
        });

        //JAVA代码调用Kotlin代码
        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ExToastKt.Companion.makeText( MainActivity.this, "Hello, customTost-Kotlin", ExToastKt.Companion.getLENGTH_LONG()).show();
                ExToastKt.Companion.makeText( MainActivity.this, "Hello, customTost-Kotlin", 1000).show();
            }
        });
    }
}
