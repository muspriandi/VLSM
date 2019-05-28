package com.example.vlsm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class OutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        TextView textView    = (TextView) findViewById(R.id.networkLama);
        ListView listView    = (ListView) findViewById(R.id.networkBaru);

        Intent intent = getIntent();
        String networkLama      = intent.getStringExtra("networkLama");
        ArrayList<String> networkBaru   = intent.getStringArrayListExtra("networkBaru");
        ArrayList<String> hasilSlash    = intent.getStringArrayListExtra("hasilSlash");
        ArrayList<Integer> kebutuhanIP  = intent.getIntegerArrayListExtra("kebutuhanIP");

//        for(int i = 0 ; i < ;i++) {

//        }

//        ArrayAdapter network = new ArrayAdapter <ArrayList<Integer>>(OutputActivity.this, android.R.layout.simple_list_item_1,newNetwork);

        textView.setText("" + networkLama);

//        ArrayAdapter adapter = new ArrayAdapter <String>(OutputActivity. this, android.R.layout.simple_list_item_1, networkLama);
//        listView.setAdapter(adapter);
    }
}
