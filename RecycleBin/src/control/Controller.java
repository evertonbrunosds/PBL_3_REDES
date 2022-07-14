package control;

import static model.Randomize.*;
import model.RecycleBin;
import uefs.evertonbrunosds.comumbase.controller.MQTTManagerController;

public class Controller {

    private static Controller instance;
    private final static String RECYCLE_BIN = "RECYCLE_BIN";
    private final static String SECTOR = "SECTOR";
    private final MQTTManagerController receiver;
    private final MQTTManagerController sender;
    private final RecycleBin recycleBin;
    public final String CONTACT;
    public final String DESTNY;

    private Controller() {
        final Long id = sortLongNumber();
        final int latitude = sortIntNumber();
        final int longitude = sortIntNumber();
        final Integer quadrant = getQuadrant(latitude, longitude);
        DESTNY = SECTOR.concat("/").concat(quadrant.toString());
        sender = new MQTTManagerController(DESTNY);
        CONTACT = SECTOR.concat("/")
                .concat(quadrant.toString()).concat("/")
                .concat(RECYCLE_BIN).concat("/")
                .concat(id.toString());
        receiver = new MQTTManagerController(CONTACT);
        final int limit = 100;
        recycleBin = new RecycleBin(id, latitude, longitude, limit);
    }

    public synchronized static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public MQTTManagerController getReceiver() {
        return receiver;
    }

    public MQTTManagerController getSender() {
        return sender;
    }

    public RecycleBin getRecycleBin() {
        return recycleBin;
    }

    private int getQuadrant(final int y, final int x) {
        return (y >= 0)
                ? (x >= 0) ? 1 : 2
                : (x >= 0) ? 3 : 4;
    }

}
