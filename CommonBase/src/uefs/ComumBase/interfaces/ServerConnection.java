package uefs.ComumBase.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Interface responsável por fornecer as assinaturas de método para um objeto de
 * conexão para servidor.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public interface ServerConnection extends Connection<DataInputStream, DataOutputStream> {

    /**
     * Método responsável por possibilitar a excussão assíncrona de instruções
     * que envolvem um fluxo de entrada.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public Future<SingleStream<? super DataInputStream>> receiveFuture(final Treatable<? super IOException> iOException) throws IOException;

    /**
     * Método responsável por possibilitar a excussão assíncrona de instruções
     * que envolvem um fluxo de saída.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public Future<SingleStream<? super DataOutputStream>> sendFuture(final Treatable<? super IOException> iOException) throws IOException;

    /**
     * Método responsável por possibilitar a excussão assíncrona de instruções
     * que envolvem um fluxo de entrada/saída.
     *
     * @param iOException Refere-se as instruções de tratamento caso ocorram
     * erros na execução assíncrona.
     * @return Retorna um objeto de execução assíncrona.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public Future<DualStream<? super DataInputStream, ? super DataOutputStream>> sendReceive(final Treatable<? super IOException> iOException) throws IOException;

    /**
     * Método responsável por possibilitar o fechamento do servidor.
     *
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void close() throws IOException;

}
