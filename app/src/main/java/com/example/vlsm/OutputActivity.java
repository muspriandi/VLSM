package com.example.vlsm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class OutputActivity extends AppCompatActivity {
    private  ListView listNetowrk;
    private  NetworkListAdapter adapter;
    private List<Network> mNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        TextView textView   = (TextView) findViewById(R.id.networkLama);
        listNetowrk         = (ListView) findViewById(R.id.networkBaru);

        Intent intent = getIntent();
        String networkLama                  = intent.getStringExtra("networkLama");
        ArrayList<String> networkBaru       = intent.getStringArrayListExtra("networkBaru");
        ArrayList<String> firstHost         = intent.getStringArrayListExtra("firstHost");
        ArrayList<String> lastHost          = intent.getStringArrayListExtra("lastHost");
        ArrayList<String> broadcastAddress  = intent.getStringArrayListExtra("broadcastAddress");
        ArrayList<Integer> prefix           = intent.getIntegerArrayListExtra("hasilSlash");
        ArrayList<Integer> ipButuh          = intent.getIntegerArrayListExtra("kebutuhanIP");

        textView.setText("" + networkLama);

        mNetwork = new ArrayList<>();
        int i;

        for (i=0; i<networkBaru.size(); i++) {
            mNetwork.add(new Network(i, networkBaru.get(i), prefix.get(i), firstHost.get(i)+" - "+lastHost.get(i), broadcastAddress.get(i), ipTersedia(prefix.get(i)), ipButuh.get(i), ipTersedia(prefix.get(i)) - ipButuh.get(i)));
        }
        adapter = new NetworkListAdapter(getApplicationContext(), mNetwork);
        listNetowrk.setAdapter(adapter);
    }

    private int ipTersedia(int prefix) {
        int ipTersedia = 0;
        int i = 1;
        int cek = 31;
        boolean selesai = FALSE;

        while(selesai != TRUE) {
            ipTersedia   = (int) (Math.pow(2, i))-2;

            if(cek == prefix) {
                selesai = TRUE;
            }
            i++;
            cek--;
        }

        return ipTersedia;
    }
}
