package uefs.ComumBase.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Classe responsável por construir instâncias de objetos.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public interface Factory {

    /**
     * Método responsável por gerar instância de Socket com ServerSocket
     * fornecido.
     *
     * @param serverSocket Refere-se ao ServerSocket fornecido.
     * @param singleStream Refere-se ao transmissor do Socket.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public static void socketBuilder(final ServerSocket serverSocket, final SingleStream<Socket> singleStream) throws IOException {
        try (final Socket socket = serverSocket.accept()) {
            singleStream.accept(socket);
        }
    }

    /**
     * Método responsável por gerar instância de Socket com IP e porta
     * fornecida.
     *
     * @param ip Refere-se ao IP fornecido.
     * @param port Refere-se a porta fornecida.
     * @param singleStream Refere-se ao transmissor do Socket.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public static void socketBuilder(final String ip, final int port, final SingleStream<Socket> singleStream) throws IOException {
        try (final Socket socket = new Socket(ip, port)) {
            singleStream.accept(socket);
        }
    }

    /**
     * Método responsável por gerar instância de DataInputStream com Socket
     * fornecido.
     *
     * @param socket Refere-se ao Socket fornecido.
     * @param singleStream Refere-se ao transmissor do DataInputStream.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public static void dataInputStreamBuilder(final Socket socket, final SingleStream<DataInputStream> singleStream) throws IOException {
        try (final DataInputStream input = new DataInputStream(socket.getInputStream())) {
            singleStream.accept(input);
        }
    }

    /**
     * Método responsável por gerar instância de DataOutputStream com Socket
     * fornecido.
     *
     * @param socket Refere-se ao Socket fornecido.
     * @param singleStream Refere-se ao transmissor do DataOutputStream.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public static void dataOutputStreamBuilder(final Socket socket, final SingleStream<DataOutputStream> singleStream) throws IOException {
        try (final DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            singleStream.accept(output);
        }
    }

    /**
     * Método responsável por gerar instância de DataInputStream e
     * DataOutputStream com Socket fornecido.
     *
     * @param socket Refere-se ao Socket fornecido.
     * @param dualStream Refere-se ao transmissor do DataInputStream e
     * DataOutputStream.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public static void dataDualStreamBuilder(final Socket socket, final DualStream<DataInputStream, DataOutputStream> dualStream) throws IOException {
        try (
                final DataInputStream input = new DataInputStream(socket.getInputStream());
                final DataOutputStream output = new DataOutputStream(socket.getOutputStream());) {
            dualStream.accept(input, output);
        }
    }

    /**
     * Método responsável por gerar instância de thread sem uso de semáforos.
     *
     * @param worker Refere-se ao trabalhador que desempenhará dado trabalho.
     * @return Retorna instância de thread sem uso de semáforos.
     */
    public static Thread thread(final Worker worker) {
        return new java.lang.Thread() {
            @Override
            public void run() {
                worker.work();
            }
        };
    }

    /**
     * Método responsável por gerar instância de thread com uso de múltiplos
     * semáforos.
     *
     * @param worker Refere-se ao trabalhador que desempenhará dado trabalho.
     * @param semaphores Refere-se aos múltiplos semáforos.
     * @param treatable Refere-se ao modo como devem ser tratadas as
     * interrupções.
     * @return Retorna instância de thread com uso de múltiplos semáforos.
     */
    public static Thread thread(final Worker worker, final Semaphore[] semaphores, final Treatable<InterruptedException> treatable) {
        return new java.lang.Thread() {
            @Override
            public void run() {
                try {
                    for (final Semaphore semaphore : semaphores) {
                        semaphore.acquire();
                    }
                    worker.work();
                } catch (final InterruptedException ex) {
                    treatable.toTreat(ex);
                } finally {
                    for (final Semaphore semaphore : semaphores) {
                        semaphore.release();
                    }
                }
            }
        };
    }

}
