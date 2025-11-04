package cc.jlom.logger;

import java.security.PublicKey;
import java.util.concurrent.CyclicBarrier;

public class CustomLogger {
    private boolean is_debug;

    private static CustomLogger instance;

    private CustomLogger(boolean is_debug) {
        this.is_debug = is_debug;
    }

    public void log(String message){
        System.out.printf("[LOG] %s\n", message);
    }

    public void debug(String message) {
        if (is_debug)
            System.out.printf("[DEBUG] %s\n", message);
    }

    public static CustomLogger create_logger(boolean is_debug) {
        instance = new CustomLogger(is_debug);
        return instance;
    }

    public static CustomLogger get_logger() {
        if (instance != null)
            return instance;
        return create_logger(true);
    }
}
