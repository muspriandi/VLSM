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

import java.sql.Array;
import java.util.ArrayList;

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
                                EditText x;

                                for(i = 0; i < network.size(); i++) {
                                    for(j = 0; j < network.size()-i-1; j++) {
                                        if(Integer.parseInt(network.get(j).getText().toString()) < Integer.parseInt(network.get(j+1).getText().toString())) {
                                            x = (EditText) network.get(j);
                                            network.set(j, network.get(j+1));
                                            network.set(j+1, x);
                                        }
                                    }
                                }

                                ArrayList<Integer> biner1 = new ArrayList<Integer>();
                                ArrayList<Integer> biner2 = new ArrayList<Integer>();
                                ArrayList<Integer> biner3 = new ArrayList<Integer>();
                                ArrayList<Integer> biner4 = new ArrayList<Integer>();

                                Integer m = 128;
                                Integer n = Integer.parseInt(oktetSatu.getText().toString());
                                for (i = 1; i <= 8; i++) {
                                    if (n >= m) {
                                        biner1.add(1);

                                        n = n - m;
                                    }
                                    else {
                                        biner1.add(0);
                                    }
                                    m = m/2;
                                }
                                m = 128;
                                n = Integer.parseInt(oktetDua.getText().toString());
                                for (i = 1; i <= 8; i++) {
                                    if (n >= m) {
                                        biner2.add(1);

                                        n = n - m;
                                    }
                                    else {
                                        biner2.add(0);
                                    }
                                    m = m/2;
                                }
                                m = 128;
                                n = Integer.parseInt(oktetTiga.getText().toString());
                                for (i = 1; i <= 8; i++) {
                                    if (n >= m) {
                                        biner3.add(1);

                                        n = n - m;
                                    }
                                    else {
                                        biner3.add(0);
                                    }
                                    m = m/2;
                                }
                                m = 128;
                                n = Integer.parseInt(oktetEmpat.getText().toString());
                                for (i = 1; i <= 8; i++) {
                                    if (n >= m) {
                                        biner4.add(1);

                                        n = n - m;
                                    }
                                    else {
                                        biner4.add(0);
                                    }
                                    m = m/2;
                                }

                                StringBuilder builder = new StringBuilder();
                                StringBuilder builder2 = new StringBuilder();
                                StringBuilder builder3 = new StringBuilder();
                                StringBuilder builder4 = new StringBuilder();
                                for(i = 0; i < biner1.size(); i++) {
                                    builder.append(biner1.get(i));
                                }
                                for(i = 0; i < biner2.size(); i++) {
                                    builder2.append(biner2.get(i));
                                }
                                for(i = 0; i < biner3.size(); i++) {
                                    builder3.append(biner3.get(i));
                                }
                                for(i = 0; i < biner4.size(); i++) {
                                    builder4.append(biner4.get(i));
                                }

                                Toast.makeText(MainActivity.this, "Biner IPv4 :"+builder+"."+builder2+"."+builder3+"."+builder4, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }
    }
}
