package ru.mobnius.vote.data.manager.zip;

public class ZipResult {

    private final byte[] origin;

    private byte[] compress;
    private double k;

    public ZipResult(byte[] origin){
        this.origin = origin;
    }

    public ZipResult getResult(byte[] compress){
        this.compress = compress;
        k = (double) (compress.length * 100) / this.origin.length;
        return this;
    }

    public byte[] getCompress() {
        return compress;
    }

    public double getK() {
        return k;
    }
}
