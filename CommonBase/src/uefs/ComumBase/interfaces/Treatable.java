package uefs.ComumBase.interfaces;

/**
 * Interface funcional responsável por possibilitar o tratamento de exceções
 * internas de forma dinâmica.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <T> Refere-se ao tipo de exceção tratável.
 */
@FunctionalInterface
public interface Treatable<T> {

    /**
     * Método responsável por receber exceções tratáveis.
     *
     * @param exception Refere-se a exceção tratável
     */
    public void toTreat(final T exception);

}
