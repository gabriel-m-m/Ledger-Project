package ui;

import model.EventLog;

public class Main {
    public static void main(String[] args) {
        new LedgerUI();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                EventLog.getInstance().printLog();
            }
        });
    }
}
