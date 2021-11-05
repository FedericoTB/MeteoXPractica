package ioutils;

public abstract class RunnableXMLReader implements Runnable {
    private String file;


    public RunnableXMLReader(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
