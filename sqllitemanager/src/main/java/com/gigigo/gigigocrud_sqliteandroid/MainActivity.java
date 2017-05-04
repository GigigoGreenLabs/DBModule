package com.gigigo.gigigocrud_sqliteandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  private Button btnManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_sql_main);


    btnManager = (Button) findViewById(R.id.btnEnterSample);

    btnManager.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,DatabaseActivity.class);
        startActivity(intent);
      }
    });
  }

  public static void open(Context context){

    Intent i = new Intent(context,MainActivity.class);
    context.startActivity(i);
  }
}