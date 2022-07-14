package control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import model.Container;
import static model.Runner.run;
import org.json.JSONObject;
import uefs.evertonbrunosds.comumbase.controller.MQTTManagerController;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import static uefs.evertonbrunosds.comumbase.util.Constants.Generic.METHOD;
import static uefs.evertonbrunosds.comumbase.util.Constants.Method.*;
import static uefs.evertonbrunosds.comumbase.util.Constants.Status.*;

public class Controller {

    private static Controller instance;
    private static final String SECTOR = "SECTOR";
    private static final String CONTACT = "CONTACT";
    private static final String DEVICE = "DEVICE";
    private static final String STATUS = "STATUS";
    private final LinkedList<JSONObject> requestQueue;
    private final LinkedList<JSONObject> recycleBins;
    private final MQTTManagerController receiver;
    private final String[] sectors;
    private final MQTTManagerController senderToSectorX;
    private final MQTTManagerController senderToSectorY;
    private final MQTTManagerController senderToSectorZ;
    private final Container<JSONObject> recycleBinXSectorContainer;
    private final Container<JSONObject> recycleBinYSectorContainer;
    private final Container<JSONObject> recycleBinZSectorContainer;

    private Controller() throws Throwable {
        recycleBinXSectorContainer = new Container<>();
        recycleBinYSectorContainer = new Container<>();
        recycleBinZSectorContainer = new Container<>();
        requestQueue = new LinkedList<>();
        recycleBins = new LinkedList<>();
        sectors = loadFromFile("sectors.txt").split(";");
        receiver = new MQTTManagerController(SECTOR.concat("/")
                .concat(sectors[0])
        );
        System.out.println("PRONTO PARA RECEBER MENSAGENS ATRAVÉS DE: "
                + SECTOR.concat("/").concat(sectors[0])
        );
        senderToSectorX = new MQTTManagerController(SECTOR.concat("/")
                .concat(sectors[1])
        );
        System.out.println("PRONTO PARA ENVIAR MENSAGENS ATRAVÉS DE: "
                + SECTOR.concat("/").concat(sectors[1])
        );
        senderToSectorY = new MQTTManagerController(SECTOR.concat("/")
                .concat(sectors[2])
        );
        System.out.println("PRONTO PARA ENVIAR MENSAGENS ATRAVÉS DE: "
                + SECTOR.concat("/").concat(sectors[2])
        );
        senderToSectorZ = new MQTTManagerController(SECTOR.concat("/")
                .concat(sectors[3])
        );
        System.out.println("PRONTO PARA ENVIAR MENSAGENS ATRAVÉS DE: "
                + SECTOR.concat("/").concat(sectors[3])
        );
        receiver.addMessagerListener(content -> {
            try {
                sectorPUTMethodListener(content);
            } catch (MqttException ex) {
                System.out.println("Falha, razão: ".concat(ex.getMessage()));
            }
            run(() -> addRequest(content));
        });
    }

    public static void start() throws Throwable {
        if (instance == null) {
            instance = new Controller();
        }
    }

    private void sendCriticalRecycleBinToSector(final MQTTManagerController sector) throws MqttException {
        sector.sendMessager(content -> {
            if (!recycleBins.isEmpty()) {
                recycleBins.removeFirst().toMap().entrySet().forEach(entry -> {
                    content.put(entry.getKey(), entry.getValue());
                });
                content.put(STATUS, FOUND);
            } else {
                content.put(STATUS, NOT_FOUND);
            }
        });
    }

    private void addRecycleBinToContainer(final JSONObject recycleBin, final Container<JSONObject> container) {
        if (!recycleBin.isEmpty()) {
            if (recycleBin.getString(STATUS).equals(FOUND)) {
                container.setContent(recycleBin);
            }
        }
    }

    private void sectorPUTMethodListener(final JSONObject request) throws MqttException { //TRATA PEDIDOS DE SETOR
        if (request.getString(DEVICE).equals(SECTOR)) {
            if (request.getString(METHOD).equals(PUT)) { //FIZERAM UM ENVIO DE LIXEIRA MAIS CRÍTICA
                final String contactX = SECTOR.concat("/").concat(sectors[1]);
                final String contactY = SECTOR.concat("/").concat(sectors[2]);
                final String contactZ = SECTOR.concat("/").concat(sectors[3]);
                System.out.println("RECEBENDO LIXEIRA MAIS CRÍTICA DA ORIGEM CITADA");
                if (request.getString(CONTACT).equals(contactX)) {
                    addRecycleBinToContainer(request, recycleBinXSectorContainer);
                } else if (request.getString(CONTACT).equals(contactY)) {
                    addRecycleBinToContainer(request, recycleBinYSectorContainer);
                } else if (request.getString(CONTACT).equals(contactZ)) {
                    addRecycleBinToContainer(request, recycleBinZSectorContainer);
                }
            }
        }
    }

    private void sectorGETMethodListener(final JSONObject request) throws MqttException {
        if (request.getString(METHOD).equals(GET)) { //FIZERAM UM PEDIDO DE LIXEIRA MAIS CRÍTICA
            final String contactX = SECTOR.concat("/").concat(sectors[1]);
            final String contactY = SECTOR.concat("/").concat(sectors[2]);
            final String contactZ = SECTOR.concat("/").concat(sectors[3]);
            System.out.println("ENVIANDO LIXEIRA MAIS CRÍTICA PARA A ORIGEM CITADA");
            if (request.getString(CONTACT).equals(contactX)) {
                sendCriticalRecycleBinToSector(senderToSectorX);
            } else if (request.getString(CONTACT).equals(contactY)) {
                sendCriticalRecycleBinToSector(senderToSectorY);
            } else if (request.getString(CONTACT).equals(contactZ)) {
                sendCriticalRecycleBinToSector(senderToSectorZ);
            }
        }
    }

    private void updateDataRecycleBins(final JSONObject request) {
        final Container<JSONObject> recycleBinContainer = new Container<>();
        for (final JSONObject oldData : recycleBins) {
            if (oldData.getString(CONTACT).equals(request.getString(CONTACT))) {
                recycleBinContainer.setContent(oldData);
                break;
            }
        }
        if (!recycleBinContainer.isEmpty()) {
            recycleBins.remove(recycleBinContainer.getContent());
        }
        recycleBins.addLast(request);
        recycleBins.sort(criticalRecycleBin());
    }

    private void addRequestInQueueWaint(final JSONObject request) {
        final LinkedList<JSONObject> oldRequestList = new LinkedList<>();
        requestQueue.forEach(oldRequest -> {
            if (oldRequest.getString(CONTACT).equals(request.getString(CONTACT))) {
                oldRequestList.addLast(oldRequest);
            }
        });
        oldRequestList.forEach(requestQueue::remove);
        requestQueue.addLast(request);
    }

    private void meetRequest() throws MqttException, InterruptedException {
        recycleBinXSectorContainer.setContent(null);
        recycleBinYSectorContainer.setContent(null);
        recycleBinZSectorContainer.setContent(null);
        System.out.println("SOLICITANDO LIXEIRA MAIS CRÍTICA DOS DEMAIS SETORES!");
        this.senderToSectorX.sendMessager(content -> {
            content.put(DEVICE, SECTOR);
            content.put(METHOD, GET);
            content.put(CONTACT, SECTOR.concat("/").concat(sectors[1]));
        });
        this.senderToSectorY.sendMessager(content -> {
            content.put(DEVICE, SECTOR);
            content.put(METHOD, GET);
            content.put(CONTACT, SECTOR.concat("/").concat(sectors[1]));
        });
        this.senderToSectorZ.sendMessager(content -> {
            content.put(DEVICE, SECTOR);
            content.put(METHOD, GET);
            content.put(CONTACT, SECTOR.concat("/").concat(sectors[1]));
        });
        sleep(1500);
        final JSONObject r1 = recycleBinXSectorContainer.getContent();
        final JSONObject r2 = recycleBinYSectorContainer.getContent();
        final JSONObject r3 = recycleBinZSectorContainer.getContent();
        if (r1 != null) {
            recycleBins.addLast(r1);
        }
        if (r2 != null) {
            recycleBins.addLast(r2);
        }
        if (r3 != null) {
            recycleBins.addLast(r3);
        }
        recycleBins.sort(criticalRecycleBin());
        if (!recycleBins.isEmpty() && !requestQueue.isEmpty()) {
            final JSONObject recycleBin = recycleBins.removeFirst();
            final JSONObject truck = requestQueue.removeFirst();
            System.out.println("ATENDENDO CAMINHÃO: ".concat(truck.getString(CONTACT)));
            final MQTTManagerController sender = new MQTTManagerController(truck.getString(CONTACT));
            sender.sendMessager(content -> {
                recycleBin.toMap().entrySet().forEach(entry -> {
                    content.put(entry.getKey(), entry.getValue());
                });
            });
        } else {
            System.out.println("NÃO HÁ SOLICITAÇÕES DE CAMINHÃO NESSE SETOR!");
        }
        recycleBins.remove(r1);
        recycleBins.remove(r2);
        recycleBins.remove(r3);
    }

    private synchronized Queue<JSONObject> addRequest(final JSONObject request) {
        try {
            if (!request.isEmpty()) {
                switch (request.getString(DEVICE)) {
                    case "SECTOR": //TRATA PEDIDOS DE SETOR
                        System.out.println("REQUISIÇÃO RECEBIDA! ORIGEM: " + request.getString(CONTACT));
                        sectorGETMethodListener(request);
                        break;
                    case "RECYCLE_BIN": //ATUALIZA DADOS DE LIXEIRA
                        System.out.println("REQUISIÇÃO RECEBIDA! ORIGEM: " + request.getString(CONTACT));
                        updateDataRecycleBins(request);
                        break;
                    case "TRASH_TRUCK": //PÕEM REQUISIÇÃO DE CAMINHÃO EM ESPERA
                        System.out.println("REQUISIÇÃO RECEBIDA! ORIGEM: " + request.getString(CONTACT));
                        addRequestInQueueWaint(request);
                        break;
                    case "ORCHESTRATOR": //ATNDE A REQUISIÇÃO DE UM CAMINHÃO
                        System.out.println("ATENDIMENTO A CAMINHÃO AUTORIZADO!");
                        meetRequest();
                        break;
                    default:
                        System.out.println("Falha! Requisição de dispositivo desconhecido!");
                        break;
                }
            }
        } catch (final MqttException | JSONException | InterruptedException th) {
            System.out.println("Falha: ".concat(th.getMessage()));
        }
        return requestQueue;
    }

    private Comparator<JSONObject> criticalRecycleBin() {
        return (r1, r2) -> {
            final Integer usage1 = Integer.parseInt(r1.getString("USAGE"));
            final Integer usage2 = Integer.parseInt(r2.getString("USAGE"));
            return usage2.compareTo(usage1);
        };
    }

    private static String loadFromFile(final String fileName) throws Throwable {
        final Container<String> container = new Container<>();
        try (final BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            container.setContent(fileStream.readLine());
        }
        return container.getContent();
    }

}
