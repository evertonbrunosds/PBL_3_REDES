package view;

import control.Controller;
import static java.lang.Thread.sleep;
import org.eclipse.paho.client.mqttv3.MqttException;
import static uefs.evertonbrunosds.comumbase.util.Constants.RecycleBin.CLEAR;
import static uefs.evertonbrunosds.comumbase.util.Constants.RecycleBin.USAGE;
import static uefs.evertonbrunosds.comumbase.util.Constants.Generic.TRUE;
import static uefs.evertonbrunosds.comumbase.util.Constants.Generic.ID;
import static model.Runner.run;
import static model.Randomize.*;
import static uefs.evertonbrunosds.comumbase.util.Constants.Generic.DEVICE;

public class Main {

    public static void main(final String[] args) throws MqttException {
        Controller.getInstance().getReceiver().addMessagerListener(content -> {
            if (content.get(CLEAR).equals(TRUE)) {
                Controller.getInstance().getRecycleBin().clear();
            }
        });
        run(() -> {
            while (true)  {
                try {
                    sleep(7000 - sortIntPositiveNumber(4000));
                    Controller.getInstance().getRecycleBin().addUsage(sortIntPositiveNumber(25));
                    Controller.getInstance().getSender().sendMessager(content -> {
                        content.put(ID, Controller.getInstance().getRecycleBin().getId().toString());
                        content.put(USAGE, Controller.getInstance().getRecycleBin().getUsage().toString());
                        content.put(DEVICE, "RECYCLE_BIN");
                        content.put("SECTOR", Controller.getInstance().getRecycleBin().getQuadrant());
                        content.put("CONTACT", Controller.getInstance().CONTACT);
                        System.out.println("REQUISIÇÃO ENVIADA! DESTINO: ".concat(Controller.getInstance().DESTNY));
                    });
                } catch (final InterruptedException | MqttException ex) {
                    System.out.println("Falha! Razão: ".concat(ex.toString()));
                }
            }
        });
    }

}
