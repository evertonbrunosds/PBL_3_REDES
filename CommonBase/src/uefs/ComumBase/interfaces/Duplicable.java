package uefs.ComumBase.interfaces;

/**
 * Interface responsável por fornecer a assinatura de objeto duplicável.
 *
 * @author Everton Bruno Silva dos Santos.
 * @param <T> Refere-se ao tipo de objeto duplicável.
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface Duplicable<T> {

    /**
     * Método responsável por duplicar objeto duplicável.
     *
     * @return Retorna objeto duplicata.
     */
    public T duplicate();

}
