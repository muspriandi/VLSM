package com.example.vlsm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tampil   = (Button) findViewById(R.id.buttonTampil);
        tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahkanNetwork();
            }
        });
    }

    public void tambahkanNetwork() {
        EditText oktetSatu      = (EditText) findViewById(R.id.oktetSatu);
        EditText oktetDua       = (EditText) findViewById(R.id.oktetDua);
        EditText oktetTiga      = (EditText) findViewById(R.id.oktetTiga);
        EditText oktetEmpat     = (EditText) findViewById(R.id.oktetEmpat);
        EditText prefixLength   = (EditText) findViewById(R.id.prefixLength);
        EditText jumlah         = (EditText) findViewById(R.id.jumlahNetwork);

        if(oktetSatu.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Harap isi oktet pertama", Toast.LENGTH_SHORT).show();
        }
        else {
            if (oktetDua.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "Harap isi oktet kedua", Toast.LENGTH_SHORT).show();
            } else {
                if (oktetTiga.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Harap isi oktet ketiga", Toast.LENGTH_SHORT).show();
                } else {
                    if (oktetEmpat.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Harap isi oktet keempat", Toast.LENGTH_SHORT).show();
                    } else {
                        if (prefixLength.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Harap isi nilai prefix length", Toast.LENGTH_SHORT).show();
                        } else {
                            if (jumlah.getText().toString().equals("")) {
                                jumlah.setError("Harap isi bidang ini");
                            } else {
                                Integer jumlahNetwork = Integer.parseInt(jumlah.getText().toString());
                                LinearLayout layout = (LinearLayout) findViewById(R.id.loopLayout);
                                final ArrayList<EditText> network = new ArrayList<EditText>();

                                layout.removeAllViews();
                                int i = 1;
                                for (int j = 0; j < jumlahNetwork; j++) {
                                    textView = new TextView(this);
                                    textView.setText("Network " + i);
                                    textView.setWidth(50);
                                    layout.addView(textView);

                                    editText = new EditText(this);
                                    network.add(editText);
                                    editText.setError("Harap isi bidang ini");
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                                    editText.setHint("Masukkan banyak IP untuk network " + i);
                                    layout.addView(editText);

                                    i++;
                                }

                                button = new Button(this);
                                button.setText("Hitung");
                                layout.addView(button);

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hitung(network);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public void hitung(ArrayList<EditText> network) {
        EditText oktetSatu      = (EditText) findViewById(R.id.oktetSatu);
        EditText oktetDua       = (EditText) findViewById(R.id.oktetDua);
        EditText oktetTiga      = (EditText) findViewById(R.id.oktetTiga);
        EditText oktetEmpat     = (EditText) findViewById(R.id.oktetEmpat);
        EditText prefixLength   = (EditText) findViewById(R.id.prefixLength);

        if (oktetSatu.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Harap isi oktet pertama", Toast.LENGTH_SHORT).show();
        } else {
            if (oktetDua.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "Harap isi oktet kedua", Toast.LENGTH_SHORT).show();
            } else {
                if (oktetTiga.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Harap isi oktet ketiga", Toast.LENGTH_SHORT).show();
                } else {
                    if (oktetEmpat.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Harap isi oktet keempat", Toast.LENGTH_SHORT).show();
                    } else {
                        if (prefixLength.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Harap isi nilai prefix length", Toast.LENGTH_SHORT).show();
                        } else {
                            int i;
                            int j = 1;
                            int k = 0;

                            for (i = 0; i < network.size(); i++) {
                                if (network.get(i).getText().toString().equals("")) {
                                    Toast.makeText(MainActivity.this, "Harap isi kebutuhan IP untuk network "+j, Toast.LENGTH_SHORT).show();

                                    k = 1;
                                    i = network.size();
                                } else {
                                    j++;
                                }
                            }

                            if(k != 1) {
                                int m;
                                int n;
                                ArrayList<Integer> biner = new ArrayList<Integer>();

                                for(i = 0; i < 4 ; i++) {
                                    if(i==0) {
                                        m = 128;
                                        n = Integer.parseInt(oktetSatu.getText().toString());
                                    }
                                    else {
                                        if(i==1) {
                                            m = 128;
                                            n = Integer.parseInt(oktetDua.getText().toString());
                                        }
                                        else {
                                            if(i==2) {
                                                m = 128;
                                                n = Integer.parseInt(oktetTiga.getText().toString());
                                            }
                                            else {
                                                m = 128;
                                                n = Integer.parseInt(oktetEmpat.getText().toString());
                                            }
                                        }
                                    }
                                    for (j = 0; j < 8; j++) {
                                        if (n >= m) {
                                            biner.add(1);

                                            n = n - m;
                                        } else {
                                            biner.add(0);
                                        }
                                        m = m / 2;
                                    }
                                }

                                int prefix  = Integer.parseInt(prefixLength.getText().toString());
                                ArrayList<Integer> binerNetwork = new ArrayList<Integer>(biner);
                                ArrayList<ArrayList<Integer>> hasilNetwork = new ArrayList<ArrayList<Integer>>();
                                ArrayList<EditText> vlsm = new ArrayList<EditText>(network);

                                while(vlsm.isEmpty() != TRUE) {
                                    int x=0;
                                    int slash           = slash(vlsm);
                                    int pangkat         = slash-prefix;
                                    double kombinasi    = Math.pow(2, pangkat);

                                    int nilaiMax   = max(vlsm);
                                    for(i=0; i < vlsm.size(); i++) {
                                        if(Integer.parseInt(vlsm.get(i).getText().toString()) == nilaiMax) {
                                            vlsm.remove(i);
                                        }
                                    }

                                    while(prefix < slash) {
                                        prefix      = prefix+1;

                                        double y   = kombinasi/2;

                                        for(j=0; j < kombinasi; j++) {
                                            binerNetwork.set(prefix, x);

                                            hasilNetwork.add(binerNetwork);

                                            if(j < (y/2)) {
                                                x   = 1;
                                            }
                                            else {
                                                x   = 0;
                                            }
                                        }
                                    }
                                }

                                for(i = 0; i < hasilNetwork.size(); i++) {
                                    Toast.makeText(MainActivity.this, hasilNetwork.get(i).toString(), Toast.LENGTH_LONG).show();
                                }
//                                StringBuilder builder = new StringBuilder();
//                                for(i = 0; i < hasilNetwork.size(); i++) {
//                                    builder.append(hasilNetwork.get(i).toString());
//                                }
//                                Toast.makeText(MainActivity.this, " "+builder+" ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }
    }

    public int max(ArrayList<EditText> vlsm) {
        int i;

        int max = 0;
        for(i = 0; i < vlsm.size(); i++) {
            if(Integer.parseInt(vlsm.get(i).getText().toString()) > max) {
                max = Integer.parseInt(vlsm.get(i).getText().toString());
            }
        }

        return max;
    }

    public int slash(ArrayList<EditText> vlsm) {
        int i;
        int slash = 31;
        int nilaiMax = max(vlsm);

        for(i = 1; i <= 32; i++) {
            if((Math.pow(2, i)-2) >= nilaiMax) {
                i   = 33;
            }
            else {
                slash = slash - 1;
            }
        }

        return slash;
    }
}