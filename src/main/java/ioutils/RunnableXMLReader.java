package ioutils;

/**
 * runnable xml reader abstract class that stablish common methods for all the runnable xml readers
 * @author sps169, FedericoTB
 */
public abstract class RunnableXMLReader implements Runnable {
    private String file;

    public RunnableXMLReader(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
