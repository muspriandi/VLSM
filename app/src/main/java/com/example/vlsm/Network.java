package com.example.vlsm;

public class Network {
    private int id;
    private String network;
    private int prefix;
    private String host;
    private String broadcast;
    private int ipTersedia;
    private int ipButuh;
    private int ipSisa;

    public Network(int id, String network, int prefix, String host, String broadcast, int ipTersedia, int ipButuh, int ipSisa) {
        this.id = id;
        this.network = network;
        this.prefix = prefix;
        this.host = host;
        this.broadcast = broadcast;
        this.ipTersedia = ipTersedia;
        this.ipButuh = ipButuh;
        this.ipSisa = ipSisa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public int getIpTersedia() {
        return ipTersedia;
    }

    public void setIpTersedia(int ipTersedia) {
        this.ipTersedia = ipTersedia;
    }

    public int getIpButuh() {
        return ipButuh;
    }

    public void setIpButuh(int ipButuh) {
        this.ipButuh = ipButuh;
    }

    public int getIpSisa() {
        return ipSisa;
    }

    public void setIpSisa(int ipSisa) {
        this.ipSisa = ipSisa;
    }
}
