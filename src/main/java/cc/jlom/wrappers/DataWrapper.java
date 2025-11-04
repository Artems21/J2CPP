package cc.jlom.wrappers;

public class DataWrapper {
    private final String name;
    private final byte[] data;

    public DataWrapper(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public String name() {
        return name;
    }

    public byte[] data() {
        return data;
    }
}
