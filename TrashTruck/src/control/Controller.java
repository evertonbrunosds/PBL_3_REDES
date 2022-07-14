package control;

import static java.lang.Thread.sleep;
import static model.Randomize.sortIntNumber;
import static model.Randomize.sortIntPositiveNumber;
import static model.Randomize.sortLongNumber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import uefs.evertonbrunosds.comumbase.controller.MQTTManagerController;
import static uefs.evertonbrunosds.comumbase.util.Constants.RecycleBin.CLEAR;
import static uefs.evertonbrunosds.comumbase.util.Constants.Generic.TRUE;

public class Controller {

    private static Controller instance;
    private final static String TRASH_TRUCK = "TRASH_TRUCK";
    private final static String CONTACT = "CONTACT";
    private final static String SECTOR = "SECTOR";
    private static final String DEVICE = "DEVICE";
    private final String truckContact;
    private final MQTTManagerController senderSector;
    private final MQTTManagerController receiver;
    private final Integer quadrant;

    private Controller() throws MqttException {
        final Long id = sortLongNumber();
        final int latitude = sortIntNumber();
        final int longitude = sortIntNumber();
        quadrant = getQuadrant(latitude, longitude);
        senderSector = new MQTTManagerController(SECTOR.concat("/")
                .concat(quadrant.toString())
        );
        truckContact = SECTOR.concat("/")
                .concat(quadrant.toString())
                .concat("/")
                .concat(TRASH_TRUCK.concat("/").concat(id.toString()));
        receiver = new MQTTManagerController(truckContact);
    }

    public synchronized static Controller getInstance() throws MqttException {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void run() throws MqttException, InterruptedException {
        receiver.addMessagerListener(content -> {
            try {
                if (!content.isEmpty()) {
                    final MQTTManagerController sender = new MQTTManagerController(content.getString(CONTACT));
                    System.out.println("ESVAZIANDO A LIXEIRA DE CONTATO: ".concat(content.getString(CONTACT)));
                    sender.sendMessager(messager -> {
                        messager.put(CLEAR, TRUE);
                    });
                } else {
                    System.out.println("NÃO HÁ LIXEIRAS PARA ESVAZIAR!");
                }
            } catch (final MqttException | JSONException th) {
                System.out.println("FALHA! RAZÃO: ".concat(th.getMessage()));
            }
        });
        while (true) {
            sleep(5000 - sortIntPositiveNumber(2000));
            senderSector.sendMessager(content -> {
                System.out.println("PEDINDO LIXEIRAS AO SETOR: ".concat(SECTOR.concat("/")
                .concat(quadrant.toString())));
                content.put(DEVICE, TRASH_TRUCK);
                content.put(CONTACT, truckContact);
            });
        }
    }

    private int getQuadrant(final int y, final int x) {
        return (y >= 0)
                ? (x >= 0) ? 1 : 2
                : (x >= 0) ? 3 : 4;
    }

}
