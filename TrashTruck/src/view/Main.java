package view;

import control.Controller;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {
    
    public static void main(final String[] args) throws MqttException, InterruptedException {
        Controller.getInstance().run();
    }
    
}
