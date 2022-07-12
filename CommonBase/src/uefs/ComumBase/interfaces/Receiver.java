package uefs.ComumBase.interfaces;

/**
 * Interface responsável por fornecer a assinatura de método de um receptor.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <T> Refere-se ao tipo de dados a ser recebido.
 */
public interface Receiver<T> {

    /**
     * Método responsável por receber uma entrada de dados.
     *
     * @param data Refere-se a dita entrada de dados.
     */
    public void receive(T data);

}
