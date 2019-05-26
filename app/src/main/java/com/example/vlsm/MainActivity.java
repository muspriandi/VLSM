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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tampil = (Button) findViewById(R.id.buttonTampil);
        tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahkanNetwork();
            }
        });
    }

    public void tambahkanNetwork() {
        EditText jumlah = (EditText) findViewById(R.id.jumlahNetwork);

        if (validasi() == 1) {
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

    public void hitung(ArrayList<EditText> network) {
        EditText oktetSatu = (EditText) findViewById(R.id.oktetSatu);
        EditText oktetDua = (EditText) findViewById(R.id.oktetDua);
        EditText oktetTiga = (EditText) findViewById(R.id.oktetTiga);
        EditText oktetEmpat = (EditText) findViewById(R.id.oktetEmpat);
        EditText prefixLength = (EditText) findViewById(R.id.prefixLength);

        if (validasi() == 1) {
            int i;
            int j = 1;
            int k = 0;

            for (i = 0; i < network.size(); i++) {
                if (network.get(i).getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Harap isi kebutuhan IP untuk network " + j, Toast.LENGTH_SHORT).show();

                    k = 1;
                    i = network.size();
                } else {
                    j++;
                }
            }

            if (k != 1) {
                int m;
                int n;
                ArrayList<Integer> biner = new ArrayList<Integer>();

                for (i = 0; i < 4; i++) {
                    if (i == 0) {
                        m = 128;
                        n = Integer.parseInt(oktetSatu.getText().toString());
                    } else {
                        if (i == 1) {
                            m = 128;
                            n = Integer.parseInt(oktetDua.getText().toString());
                        } else {
                            if (i == 2) {
                                m = 128;
                                n = Integer.parseInt(oktetTiga.getText().toString());
                            } else {
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

                int prefix = Integer.parseInt(prefixLength.getText().toString());
                ArrayList<EditText> vlsm = new ArrayList<EditText>(network);
                ArrayList<ArrayList<Integer>> hasilNetwork = new ArrayList<ArrayList<Integer>>();
                boolean selesai = FALSE;

                while (vlsm.size() != 0 && selesai == FALSE) {
                    int slash = slash(vlsm);
                    int pangkat = slash - prefix;
                    double kombinasi = Math.pow(2, pangkat);

                    int seNetwork = satuNetwork(slash, vlsm);

                    vlsm = hapusNetwork(slash, vlsm);

                    int kombinasiArray[][] = new int[(int) kombinasi][pangkat];
                    double a = kombinasi/2;

                    for (i = 0; i < pangkat; i++) {
                        int b = 1;
                        int c;

                        for (j = 0; j < kombinasi; j++) {
                            if (a < b) {
                                c = 1;
                            } else {
                                c = 0;
                            }

                            kombinasiArray[j][i] = c;

                            if (a + a == b) {
                                b = 1;
                            } else {
                                b++;
                            }
                        }
                        a = a/2;
                    }

                    int x = 0;
                    int y = 0;
                    int z = seNetwork;
                    ArrayList<Integer> binerNetwork = new ArrayList<Integer>(biner);

                    while(z != 0) {
                        m = prefix;

                        for(i=0; i< pangkat; i++) {
                            binerNetwork.set(m, kombinasiArray[x][y]);
                            m++;
                            y++;

                            if(y == pangkat) {
                                y=0;
                            }
                        }
                        x++;

                        if(x == kombinasi) {
                            x=0;
                        }

                        while (m < 32) {
                            binerNetwork.set(m, 0);
                            m++;
                        }
                        hasilNetwork.add(binerNetwork);

                        z--;

                        binerNetwork = new ArrayList<Integer>(biner);
                        if(z == 0 && kombinasi - seNetwork > 0) {
                            m = prefix;

                            for(i=0; i< pangkat; i++) {
                                binerNetwork.set(m, kombinasiArray[x][y]);
                                m++;
                                y++;
                            }
                            for (i = 0; i < biner.size(); i++) {
                                biner.set(i, binerNetwork.get(i));
                            }
                        }

                    }
                    prefix = slash;

                    if(kombinasi - seNetwork <= 0) {
                        selesai = TRUE;
                    }
                }

//                    while (prefix < slash) {
//                        prefix = prefix + 1;
//                        int count = 0;
//                        int z;
//                        m = 1;
//
//                        for (j = 0; j < kombinasi; j++) {
//
//                            double y = (kombinasi / 2) - count;
//                            if (y < m) {
//                                x = 1;
//                            } else {
//                                x = 0;
//                            }
//                            z = prefix;
//
//                            ArrayList<Integer> binerNetwork = new ArrayList<Integer>(biner);
//
//                            binerNetwork.set((prefix - 1), x);
//                            while (z < 32) {
//                                binerNetwork.set(z, 0);
//                                z++;
//                            }
//
//                            if (seNetwork > j) {
//                                  hasilNetwork.add(binerNetwork);
//                            } else {
//                                j = (int) kombinasi;
//                            }
//
//                            for (i = 0; i < biner.size(); i++) {
//                                biner.set(i, binerNetwork.get(i));
//                            }
//
//
//                            if (y + y == m) {
//                                m = 1;
//                            } else {
//                                m++;
//                            }
//                            count++;
//                        }
//                    }

                for (i = 0; i < hasilNetwork.size(); i++) {
                    Toast.makeText(MainActivity.this, hasilNetwork.get(i).toString(), Toast.LENGTH_LONG).show();
                }
//                               StringBuilder builder = new StringBuilder();
//                               for(i = 0; i < hasilNetwork.size(); i++) {
//                                   builder.append(hasilNetwork.get(i).toString());
//                               }
//                               Toast.makeText(MainActivity.this, " "+builder+" ", Toast.LENGTH_LONG).show();
            }
        }
    }

    public int validasi() {
        int m = 0;
        EditText oktetSatu = (EditText) findViewById(R.id.oktetSatu);
        EditText oktetDua = (EditText) findViewById(R.id.oktetDua);
        EditText oktetTiga = (EditText) findViewById(R.id.oktetTiga);
        EditText oktetEmpat = (EditText) findViewById(R.id.oktetEmpat);
        EditText prefixLength = (EditText) findViewById(R.id.prefixLength);

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
                            m = 1;
                        }
                    }
                }
            }
        }

        return m;
    }

    public int max(ArrayList<EditText> vlsm) {
        int i;

        int max = 0;
        for (i = 0; i < vlsm.size(); i++) {
            if (Integer.parseInt(vlsm.get(i).getText().toString()) > max) {
                max = Integer.parseInt(vlsm.get(i).getText().toString());
            }
        }

        return max;
    }

    public int slash(ArrayList<EditText> vlsm) {
        int i;
        int slash = 31;
        int nilaiMax = max(vlsm);

        for (i = 1; i <= 31; i++) {
            if ((Math.pow(2, i) - 2) >= nilaiMax) {
                i = 33;
            } else {
                slash = slash - 1;
            }
        }

        return slash;
    }

    public ArrayList<EditText> hapusNetwork(Integer slash, ArrayList<EditText> vlsm) {
        int i = 2;
        double host = 0;
        double hostBawah = 0;
        int slashBanding = 31;

        while (slash != slashBanding) {
            host = Math.pow(2, i) - 2;
            hostBawah = Math.pow(2, (i - 1)) - 2;
            slashBanding = slashBanding - 1;
            i = i + 1;
        }

        int satuNetwork = 0;
        for (i = 0; i < vlsm.size(); i++) {
            if (Integer.parseInt(vlsm.get(i).getText().toString()) <= host && Integer.parseInt(vlsm.get(i).getText().toString()) > hostBawah) {
                satuNetwork = satuNetwork + 1;
            }
        }

        i = 0;
        int j = 0;
        while (i != satuNetwork) {
            if (Integer.parseInt(vlsm.get(j).getText().toString()) <= host && Integer.parseInt(vlsm.get(j).getText().toString()) > hostBawah) {
                vlsm.remove(j);
                i++;
            } else {
                j++;
            }
        }

        return vlsm;
    }

    public int satuNetwork(int slash, ArrayList<EditText> vlsm) {
        int i = 2;
        double host = 0;
        double hostBawah = 0;
        int slashBanding = 31;

        while (slash != slashBanding) {
            host = Math.pow(2, i) - 2;
            hostBawah = Math.pow(2, (i - 1)) - 2;
            slashBanding = slashBanding - 1;
            i = i + 1;
        }

        int satuNetwork = 0;
        for (i = 0; i < vlsm.size(); i++) {
            if (Integer.parseInt(vlsm.get(i).getText().toString()) <= host && Integer.parseInt(vlsm.get(i).getText().toString()) > hostBawah) {
                satuNetwork = satuNetwork + 1;
            }
        }

        return satuNetwork;
    }
}