package com.example.vlsm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NetworkListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Network> mNetwork;

    public NetworkListAdapter(Context mContext, List<Network> mNetwork) {
        this.mContext = mContext;
        this.mNetwork = mNetwork;
    }

    @Override
    public int getCount() {
        return mNetwork.size();
    }

    @Override
    public Object getItem(int position) {
        return mNetwork.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.network_list, null);

        TextView network    = (TextView)v.findViewById(R.id.network);
        TextView prefix     = (TextView)v.findViewById(R.id.prefix);
        TextView host       = (TextView)v.findViewById(R.id.host);
        TextView broadcast  = (TextView)v.findViewById(R.id.broadcast);
        TextView ipTersedia = (TextView)v.findViewById(R.id.ipTersedia);
        TextView ipButuh    = (TextView)v.findViewById(R.id.ipButuh);
        TextView ipSisa     = (TextView)v.findViewById(R.id.ipSisa);

        network.setText("Network :  "+mNetwork.get(position).getNetwork());
        prefix.setText("Prefix length :  /"+String.valueOf(mNetwork.get(position).getPrefix()));
        host.setText("Hosts (Range IP) :  "+mNetwork.get(position).getHost());
        broadcast.setText("Broadcast :  "+mNetwork.get(position).getBroadcast());
        ipTersedia.setText("Jumlah IP tersedia :  "+String.valueOf(mNetwork.get(position).getIpTersedia())+ " IP");
        ipButuh.setText("Jumlah IP terpakai :  "+String.valueOf(mNetwork.get(position).getIpButuh())+ " IP");
        ipSisa.setText("Jumlah IP sisa :  "+String.valueOf(mNetwork.get(position).getIpSisa())+ " IP");

        v.setTag(mNetwork.get(position).getId());

        return v;
    }
}
