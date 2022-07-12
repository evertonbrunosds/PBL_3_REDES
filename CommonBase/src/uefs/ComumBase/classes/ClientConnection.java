package uefs.ComumBase.classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import uefs.ComumBase.interfaces.DualStream;
import static uefs.ComumBase.interfaces.Factory.dataDualStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.dataInputStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.dataOutputStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.socketBuilder;
import uefs.ComumBase.interfaces.SingleStream;

/**
 * Interface responsável por comportar-se como um objeto de conexão para
 * clientes.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public class ClientConnection implements uefs.ComumBase.interfaces.ClientConnection {

    /**
     * Refere-se ao número de IP da conexão.
     */
    public final String ip;
    /**
     * Refere-se a porta da conexão.
     */
    public final int port;

    /**
     * Construtor responsável por instanciar uma conexão cliente.
     *
     * @param ip Refere-se ao número de IP da conexão.
     * @param port Refere-se a porta da conexão.
     */
    public ClientConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Implementação de método responsável por construir fluxos de entrada de
     * dados para clientes.
     *
     * @param singleStream Refere-se ao dito fluxo de entrada de dados para
     * clientes.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void receive(final SingleStream<? super DataInputStream> singleStream) throws IOException {
        socketBuilder(ip, port, socketInstance -> dataInputStreamBuilder(socketInstance, singleStream::accept));
    }

    /**
     * Implementação de método responsável por construir fluxos de saída de
     * dados para clientes.
     *
     * @param singleStream Refere-se ao dito fluxo de saída de dados para
     * clientes.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void send(final SingleStream<? super DataOutputStream> singleStream) throws IOException {
        socketBuilder(ip, port, socketInstance -> dataOutputStreamBuilder(socketInstance, singleStream::accept));
    }

    /**
     * Implementação de método responsável por construir fluxos de entrada e
     * saída de dados para clientes.
     *
     * @param dualStream Refere-se ao dito fluxo de entrada e saída de dados.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void sendReceive(final DualStream<? super DataInputStream, ? super DataOutputStream> dualStream) throws IOException {
        socketBuilder(ip, port, socketInstance -> dataDualStreamBuilder(socketInstance, dualStream::accept));
    }

}
