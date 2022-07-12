package uefs.ComumBase.interfaces;

import java.io.IOException;

/**
 * Interface funcional responsável por possibilitar o acesso aos fluxos de
 * entrada e saída de forma simultânea.
 *
 * @param <I> Refere-se ao tipo de fluxo de entrada.
 * @param <O> Refere-se ao tipo de fluxo de saída.
 */
@FunctionalInterface
public interface DualStream<I, O> {

    /**
     * Método responsável por receber fluxos de entrada e saída de forma
     * simultânea.
     *
     * @param i Refere-se ai tipo de fluxo de entrada.
     * @param o Refere-se ai tipo de fluxo de saída.
     * @throws IOException Exceção lançada no caso de haver falha de
     * entrada/saída.
     */
    public void accept(final I i, final O o) throws IOException;

}
