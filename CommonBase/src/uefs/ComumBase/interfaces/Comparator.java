package uefs.ComumBase.interfaces;

import java.io.Serializable;

/**
 * Interface responsável por fornecer as assinaturas de métodos de um
 * comparador.
 *
 * @author Everton Bruno Silva dos Santos.
 * @param <T> Refere-se ao tipo do comparador.
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface Comparator<T> extends Serializable {

    /**
     * Método responsável por obiter indicativo de que um objeto é maior que
     * outro.
     *
     * @param comparableOne Refere-se ao objeto 1.
     * @param comparableTwo Refere-se ao objeto 2.
     * @return Retorna indicativo de que um objeto é maior que outro.
     */
    public int compare(final T comparableOne, final T comparableTwo);
}
