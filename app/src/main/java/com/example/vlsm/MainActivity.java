package com.example.vlsm;

import android.content.Intent;
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

        // Perintah dibawah dijalankan jika tombol 'tampil' di atas ditekan
        tampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memanggil method tambahkanNetwork()
                tambahkanNetwork();
            }
        });
    }

    // Method untuk menambah input(EditText) berdasarkan input dari user
    public void tambahkanNetwork() {
        EditText jumlah = (EditText) findViewById(R.id.jumlahNetwork);

        // Membandingkan return value dari method validasi dengan angka 1
        if (validasi() == 1) {
            if (jumlah.getText().toString().equals("")) {
                jumlah.setError("Harap isi bidang ini");
            }
            else {
                Integer jumlahNetwork = Integer.parseInt(jumlah.getText().toString());
                LinearLayout layout = (LinearLayout) findViewById(R.id.loopLayout);
                final ArrayList<EditText> network = new ArrayList<EditText>();

                // Menghapus komponen yang sebelumnya ada pada LinearLayout(id:Looplayout) di XML
                layout.removeAllViews();
                int i = 1;

                // Membuat TextView(label/keterangan) dan EditText(inputan) berdasarkan inputan dari user
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

                // Membuat satu button untuk proses selanjutnya (hitung)
                button = new Button(this);
                button.setText("Hitung");
                layout.addView(button);

                // Perintah dibawah dijalankan jika tombol 'hitung' di atas ditekan
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Memanggil method hitung dengan parameter berupa array (network)
                        hitung(network);
                    }
                });
            }
        }
    }

    // Method untuk menghitung alokasi IP dengan cara VLSM
    public void hitung(ArrayList<EditText> network) {
        EditText oktetSatu      = (EditText) findViewById(R.id.oktetSatu);
        EditText oktetDua       = (EditText) findViewById(R.id.oktetDua);
        EditText oktetTiga      = (EditText) findViewById(R.id.oktetTiga);
        EditText oktetEmpat     = (EditText) findViewById(R.id.oktetEmpat);
        EditText prefixLength   = (EditText) findViewById(R.id.prefixLength);

        if (validasi() == 1) {
            int i;
            int j = 1;
            int k = 0;

            // Cek apabila ada kolom inputan (EditText) yang masih kosong
            for (i = 0; i < network.size(); i++) {
                if (network.get(i).getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Harap isi kebutuhan IP untuk network " + j, Toast.LENGTH_SHORT).show();

                    // ubah value nilai k
                    k = 1;
                    i = network.size();
                } else {
                    j++;
                }
            }

            // Cek value nilai k, jika value nilai k masih 0 maka perintah dibawah tidak dijalankan
            if (k != 1) {
                int m;
                int n;
                ArrayList<Integer> biner = new ArrayList<Integer>();

                // Konversi dari desimal ke biner untuk IP network oktet per-oktet
                for (i = 0; i < 4; i++) {
                    if (i == 0) {   // Mengubah nilai oktet ke 1 menjadi desimal
                        m = 128;
                        n = Integer.parseInt(oktetSatu.getText().toString());
                    } else {
                        if (i == 1) {   // Mengubah nilai oktet ke 2 menjadi desimal
                            m = 128;
                            n = Integer.parseInt(oktetDua.getText().toString());
                        } else {
                            if (i == 2) {   // Mengubah nilai oktet ke 3 menjadi desimal
                                m = 128;
                                n = Integer.parseInt(oktetTiga.getText().toString());
                            } else {
                                m = 128;
                                n = Integer.parseInt(oktetEmpat.getText().toString());
                            }
                        }
                    }
                    // Mengisi nilai array(biner) dan menggabungkan biner dari oktet setiap oktet
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
                int slashAwal[] = new int[network.size()];
                int p = 0;
                boolean selesai = FALSE;

                /* Rumus inti VLSM */

                // Loop dengan kondisi apabila kombinasi IP yang ada telah habis (digunakan)
                while (vlsm.size() != 0 && selesai != TRUE) {
                    int slash = slash(vlsm);

                    // Mencari kombinasi yang muncul berdasarkan nilai slash di atas
                    int pangkat = slash - prefix;
                    double kombinasi = Math.pow(2, pangkat);

                    int seNetwork = satuNetwork(slash, vlsm);

                    // Mengisi array slahAwal ke i dengan nilai slash
                    for(i = 0; i < seNetwork ; i++) {
                        slashAwal[p] = slash;
                        p++;
                    }

                    vlsm = hapusNetwork(slash, vlsm);

                    int kombinasiArray[][] = new int[(int) kombinasi][pangkat];
                    double a = kombinasi/2;

                    // Mencari kombinkasi yang munkin muncul
                    for (i = 0; i < pangkat; i++) {
                        int b = 1;
                        int c;

                        for (j = 0; j < kombinasi; j++) {
                            if (a < b) {
                                c = 1;
                            }
                            else {
                                c = 0;
                            }

                            kombinasiArray[j][i] = c;

                            if (a + a == b) {
                                b = 1;
                            }
                            else {
                                b++;
                            }
                        }
                        a = a/2;
                    }

                    int x = 0;
                    int y = 0;
                    int z;

                    if(kombinasi > seNetwork) {
                        z = seNetwork;
                    }
                    else {
                        z = (int) kombinasi;
                    }

                    ArrayList<Integer> binerNetwork = new ArrayList<Integer>(biner);

                    // Mengubah isi dari array binerNetwork dengan kombinasiArray
                    while(z != 0) {
                        m = prefix;

                        // Merubah nilai biner berdasarkan nilai m(prefix)
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

                        // Mengenolkan nilai dibelakang kombinasi nilai slash
                        while (m < 32) {
                            binerNetwork.set(m, 0);
                            m++;
                        }

                        hasilNetwork.add(binerNetwork);

                        z--;

                        binerNetwork = new ArrayList<Integer>(biner);
                        // Perintah untuk mengubah nilai array biner untuk slash berikutnya
                        if(z == 0 && kombinasi > seNetwork) {
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

                    // Jika kombinasi telah habis terpakai
                    if(kombinasi <= seNetwork) {
                        selesai = TRUE;
                    }
                }

                /* Rumus inti VLSM */

                String networkLama = oktetSatu.getText().toString()+"."+oktetDua.getText().toString()+"."+oktetTiga.getText().toString()+"."+oktetEmpat.getText().toString()+" /"+prefixLength.getText().toString();
                ArrayList<String> networkBaru       = new ArrayList<String>();
                StringBuilder builder               = new StringBuilder();
                int desimal;

                // Konversi dari biner ke desimal untuk masing-masing network baru yang tercipta dari rumus inti VLSM
                for(i=0; i < hasilNetwork.size(); i++) {
                    int z = 0;
                    int y = 0;

                    for(j=0; j < (hasilNetwork.get(i).size())/8; j++) {
                        desimal = 0;
                        int x = 128;

                        for(k=0; k <(hasilNetwork.get(i).size())/4 ; k++) {
                            if(hasilNetwork.get(i).get(z) == 1) {
                                desimal  = desimal+x;
                            }
                            x = x/2;
                            z++;
                        }
                        builder.append(desimal);
                        if(y <= 2) {
                            builder.append(".");
                        }
                        y++;
                    }
                    networkBaru.add(builder.toString());
                    builder.delete(0,31);
                }

                int IPawal[] = new int[network.size()];
                // Copy kebutuhkan masing-masing network ke array IPawal
                for(i = 0; i < network.size(); i++) {
                    IPawal[i]  = Integer.parseInt(network.get(i).getText().toString());
                }
                // Bubble sort pada array IPawal
                for(i = 0; i <= network.size()-2; i++) {
                    for(j = 0; j <=network.size()-2-i ; j++) {
                        if(IPawal[j] < IPawal[j+1]) {
                            int x       = IPawal[j];
                            IPawal[j]   = IPawal[j+1];
                            IPawal[j+1] = x;
                        }
                    }
                }

                ArrayList<Integer> kebutuhanIP = new ArrayList<Integer>();
                ArrayList<Integer> hasilSlash   = new ArrayList<Integer>();

                for(i = 0; i < networkBaru.size(); i++) {
                    // Mengisi nilai array IPawal ke arrayList kebutuhanIP
                    kebutuhanIP.add(IPawal[i]);
                    // Mengisi nilai array slashAwal ke arrayList hasilSlash
                    hasilSlash.add(slashAwal[i]);
                }

                int broadcast[][]   = new int[networkBaru.size()][32];
                int hostAwal[][]    = new int[networkBaru.size()][32];
                int hostAkhir[][]   = new int[networkBaru.size()][32];
                ArrayList<String> broadcastAddress = new ArrayList<String>();
                ArrayList<String> firstHost = new ArrayList<String>();
                ArrayList<String> lastHost = new ArrayList<String>();

                // Copy biner network awal untuk dirubah menjadi Hosts dan Broadcast
                for(i=0; i<networkBaru.size(); i++) {
                    for(j=0; j<32; j++) {
                        broadcast[i][j] = hasilNetwork.get(i).get(j);
                        hostAwal[i][j]  = hasilNetwork.get(i).get(j);
                        hostAkhir[i][j] = hasilNetwork.get(i).get(j);
                    }
                }
                // Membuat array Host(awal dan akhir) dan Broadcast berdasarkan prefix
                for(i=0; i<networkBaru.size(); i++) {
                    int x = hasilSlash.get(i);
                    while (x < 32) {
                        hostAwal[i][x]  = 0;
                        hostAkhir[i][x] = 1;
                        broadcast[i][x] = 1;

                        if(x == 31) {
                            hostAwal[i][x]  = 1;
                            hostAkhir[i][x] = 0;
                        }
                        x++;
                    }
                }

                int desimal2;
                int desimal3;
                StringBuilder builder2 = new StringBuilder();
                StringBuilder builder3 = new StringBuilder();

                // Konversi biner Host(awal dan akhir) dan Broadcast ke desimal
                for(i=0; i < hasilNetwork.size(); i++) {
                    int z = 0;
                    int y = 0;

                    for(j=0; j < 4; j++) {
                        desimal     = 0;
                        desimal2    = 0;
                        desimal3    = 0;
                        int x = 128;

                        for(k=0; k < 8 ; k++) {
                            if(broadcast[i][z] == 1) {
                                desimal  = desimal+x;
                            }
                            if(hostAwal[i][z] == 1) {
                                desimal2  = desimal2+x;
                            }
                            if(hostAkhir[i][z] == 1) {
                                desimal3  = desimal3+x;
                            }
                            x = x/2;
                            z++;
                        }
                        builder.append(desimal);
                        builder2.append(desimal2);
                        builder3.append(desimal3);
                        if(y <= 2) {
                            builder.append(".");
                            builder2.append(".");
                            builder3.append(".");
                        }
                        y++;
                    }
                    broadcastAddress.add(builder.toString());
                    builder.delete(0,31);
                    firstHost.add(builder2.toString());
                    builder2.delete(0,31);
                    lastHost.add(builder3.toString());
                    builder3.delete(0,31);
                }

                // Mengirim nilai networkLama, network baru yang muncul, slash, kebutuhan IP tiap network, dan host awal serta host akhir
                openActivityDua(networkLama, networkBaru, hasilSlash, kebutuhanIP, broadcastAddress, firstHost, lastHost);
            }
        }
    }

    // Cek apabila masih ada input (EditText) yang belum diisi(kosong)
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
                            // Akan mengubah nilai m apabila semua inputan telah terisi
                            m = 1;
                        }
                    }
                }
            }
        }

        return m;
    }

    // Mencari nilai terbesar dalam suatu array kebutuhan network
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

    // Mencari nilai slash dari network terbesar
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

    // Menghapus network yang telah digunakan untuk perhitungan
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

    // Mencari nilai kebutuhan network yang ternyata ada dalam satu prefix
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

    // Method untuk mengirim hasil-hasil(array) ke 'OutputActivity'
    public void openActivityDua(String networkLama, ArrayList<String> networkBaru, ArrayList<Integer> hasilSlash, ArrayList<Integer> kebutuhanIP, ArrayList<String> broadcastAddress, ArrayList<String> firstHost, ArrayList<String> lastHost) {
        Intent intent   = new Intent(this, OutputActivity.class);
        intent.putExtra("networkLama", networkLama);
        intent.putExtra("networkBaru", networkBaru);
        intent.putExtra("hasilSlash", hasilSlash);
        intent.putExtra("kebutuhanIP", kebutuhanIP);
        intent.putExtra("broadcastAddress", broadcastAddress);
        intent.putExtra("firstHost", firstHost);
        intent.putExtra("lastHost", lastHost);
        startActivity(intent);
    }
}