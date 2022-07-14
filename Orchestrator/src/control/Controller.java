package control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import model.Container;
import org.eclipse.paho.client.mqttv3.MqttException;
import uefs.evertonbrunosds.comumbase.controller.MQTTManagerController;

public class Controller {

    private static Controller instance;
    private final MQTTManagerController[] sectors;

    private Controller(final String[] contacts) throws Throwable {
        sectors = new MQTTManagerController[contacts.length];
        for (int i = 0; i < contacts.length; i++) {
            System.out.println("Setor " + contacts[i] + " listado.");
            sectors[i] = new MQTTManagerController("SECTOR"
                    .concat("/").concat(contacts[i])
            );
        }
    }

    private static String loadFromFile(final String fileName) throws Throwable {
        final Container<String> container = new Container<>();
        try (final BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            container.setContent(fileStream.readLine());
        }
        return container.getContent();
    }

    public synchronized static Controller getInstance() throws Throwable {
        if (instance == null) {
            instance = new Controller(loadFromFile("sectors.txt").split(";"));
        }
        return instance;
    }

    public void run() throws InterruptedException, MqttException {
        while (true) {
            for (final MQTTManagerController sector : sectors) {
                sleep(3000);
                sector.sendMessager(content -> {
                    content.put("DEVICE", "ORCHESTRATOR");
                });
            }
        }
    }

}
