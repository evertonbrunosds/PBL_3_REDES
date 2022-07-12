package uefs.ComumBase.classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import uefs.ComumBase.interfaces.DualStream;
import uefs.ComumBase.interfaces.Factory;
import static uefs.ComumBase.interfaces.Factory.dataDualStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.dataInputStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.dataOutputStreamBuilder;
import static uefs.ComumBase.interfaces.Factory.socketBuilder;
import uefs.ComumBase.interfaces.Future;
import uefs.ComumBase.interfaces.SingleStream;
import uefs.ComumBase.interfaces.Treatable;

/**
 * Classe responsável por comportar-se como um objeto de conexão para servidor.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public class ServerConnection implements uefs.ComumBase.interfaces.ServerConnection {

    /**
     * Refere-se a instância do ServerSocket.
     */
    private ServerSocket serverSocket;
    /**
     * Refere-se a porta da conexão.
     */
    public final int port;

    /**
     * Construtor responsável por instanciar uma conexão servidor.
     *
     * @param port Refere-se a porta da conexão.
     */
    public ServerConnection(final int port) {
        this.port = port;
        this.serverSocket = null;
    }

    /**
     * Método responsável por gerar instância de ServerSocket com porta
     * fornecida.
     *
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    private ServerSocket getServerSocketInstance() throws IOException {
        if (serverSocket == null) {
            serverSocket = new ServerSocket(port);
        }
        return serverSocket;
    }

    /**
     * Implementação de método responsável por construir fluxos de entrada de
     * dados para servidores.
     *
     * @param singleStream Refere-se ao dito fluxo de entrada de dados para
     * servidores.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void receive(final SingleStream<? super DataInputStream> singleStream) throws IOException {
        socketBuilder(getServerSocketInstance(), socketInstance -> {
            dataInputStreamBuilder(socketInstance, singleStream::accept);
        });
    }

    /**
     * Implementação de método responsável por construir fluxos de saída de
     * dados para servidores.
     *
     * @param singleStream Refere-se ao dito fluxo de saída de dados para
     * servidores.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void send(final SingleStream<? super DataOutputStream> singleStream) throws IOException {
        socketBuilder(getServerSocketInstance(), socketInstance -> {
            dataOutputStreamBuilder(socketInstance, singleStream::accept);
        });
    }

    /**
     * Implementação de método responsável por construir fluxos de entrada e
     * saída de dados para servidores.
     *
     * @param dualStream Refere-se ao dito fluxo de entrada e saída de dados.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void sendReceive(
            final DualStream<? super DataInputStream, ? super DataOutputStream> dualStream
    ) throws IOException {
        socketBuilder(getServerSocketInstance(), socketInstance -> {
            dataDualStreamBuilder(socketInstance, dualStream::accept);
        });
    }

    /**
     * Implementação de método responsável por possibilitar a excussão
     * assíncrona de instruções que envolvem um fluxo de entrada.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public Future<SingleStream<? super DataInputStream>> receiveFuture(final Treatable<? super IOException> iOException) throws IOException {
        final Socket socket = getServerSocketInstance().accept();
        return (final SingleStream<? super DataInputStream> singleStream) -> {
            Factory.thread(() -> {
                try {
                    try {
                        dataInputStreamBuilder(socket, singleStream::accept);
                    } finally {
                        socket.close();
                    }
                } catch (final IOException ex) {
                    iOException.toTreat(ex);
                }
            }).start();
        };
    }

    /**
     * Implementação de método responsável por possibilitar a excussão
     * assíncrona de instruções que envolvem um fluxo de saída.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public Future<SingleStream<? super DataOutputStream>> sendFuture(Treatable<? super IOException> iOException) throws IOException {
        final Socket socket = getServerSocketInstance().accept();
        return (final SingleStream<? super DataOutputStream> singleStream) -> {
            Factory.thread(() -> {
                try {
                    try {
                        dataOutputStreamBuilder(socket, singleStream::accept);
                    } finally {
                        socket.close();
                    }
                } catch (final IOException ex) {
                    iOException.toTreat(ex);
                }
            }).start();
        };
    }

    /**
     * Implementação de método responsável por possibilitar a excussão
     * assíncrona de instruções que envolvem um fluxo de entrada/saída.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public Future<DualStream<? super DataInputStream, ? super DataOutputStream>> sendReceive(Treatable<? super IOException> iOException) throws IOException {
        final Socket socket = getServerSocketInstance().accept();
        return (final DualStream<? super DataInputStream, ? super DataOutputStream> dualStream) -> {
            Factory.thread(() -> {
                try {
                    try {
                        dataDualStreamBuilder(socket, dualStream::accept);
                    } finally {
                        socket.close();
                    }
                } catch (final IOException ex) {
                    iOException.toTreat(ex);
                }
            }).start();
        };
    }

    /**
     * Implementação de método responsável por possibilitar o fechamento do
     * servidor.
     *
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            serverSocket = null;
        }
    }

}
