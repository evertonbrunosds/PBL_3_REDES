package uefs.ComumBase.interfaces;

/**
 * Interface responsável por fornecer a assinatura de método de um trabalhador.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
@FunctionalInterface
public interface Worker {

    /**
     * Método responsável por trabalhar.
     */
    public void work();

}
