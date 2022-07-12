package uefs.ComumBase.interfaces;

import java.io.IOException;

/**
 * Interface responsável por fornecer as assinaturas de método para um objeto de
 * conexão.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <I> Refere-se ao objeto tipo de entrada.
 * @param <O> Refere-se ao objeto tipo de saída.
 */
public interface Connection<I, O> {

    /**
     * Método responsável por construir fluxos de entrada e saída de dados.
     *
     * @param dualStream Refere-se ao dito fluxo de entrada e saída de dados.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void sendReceive(final DualStream<? super I, ? super O> dualStream) throws IOException;

    /**
     * Método responsável por construir fluxos de entrada de dados.
     *
     * @param singleStream Refere-se ao dito fluxo de entrada de dados.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void receive(final SingleStream<? super I> singleStream) throws IOException;

    /**
     * Método responsável por construir fluxos de saída de dados.
     *
     * @param singleStream Refere-se ao dito fluxo de saída de dados.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void send(final SingleStream<? super O> singleStream) throws IOException;

}
