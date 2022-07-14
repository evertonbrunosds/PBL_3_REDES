package model;

public interface Runner {
    
    public static void run(final Runnable runnable) {
        final Thread thread = new Thread(runnable);
        thread.start();
    }
    
}
