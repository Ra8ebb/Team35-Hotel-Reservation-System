package com.hotel.threads;

/**
 * Background service that periodically triggers a room availability refresh.
 * Uses a simple Runnable callback so the caller (RoomBrowseController) decides
 * how to update the UI safely via Platform.runLater().
 *
 * This keeps the threading logic educational and straightforward.
 */
public class RoomAvailabilityService implements Runnable {

    private final int intervalMillis;
    private final Runnable onRefresh;  // Callback executed after each interval
    private volatile boolean running = true;

    /**
     * @param intervalMillis How often (in ms) to trigger a refresh.
     * @param onRefresh      Callback to invoke on each refresh tick.
     */
    public RoomAvailabilityService(int intervalMillis, Runnable onRefresh) {
        this.intervalMillis = intervalMillis;
        this.onRefresh      = onRefresh;
    }

    @Override
    public void run() {
        System.out.println("[Thread] Room availability service started.");
        while (running) {
            try {
                Thread.sleep(intervalMillis);
                if (running) {
                    onRefresh.run();
                }
            } catch (InterruptedException e) {
                System.out.println("[Thread] Room availability service interrupted.");
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }
}
