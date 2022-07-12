package uefs.ComumBase.interfaces;

import java.io.IOException;

/**
 * Interface funcional responsável por possibilitar o acesso aos fluxos de
 * entrada e saída de forma dinâmica.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <T> Refere-se ao tipo de fluxo.
 */
@FunctionalInterface
public interface SingleStream<T> {

    /**
     * Método responsável por receber fluxos de entrada e saída.
     *
     * @param t Refere-se ai tipo de fluxo de entrada e saída.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void accept(final T t) throws IOException;

}
