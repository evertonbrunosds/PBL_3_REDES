package uefs.ComumBase.interfaces;

/**
 * Interface responsável por fornecer a assinatura de método de um remetente.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <T> Refere-se ao tipo de dados a ser enviado.
 */
public interface Sender<T> {

    /**
     * Método responsável por enviar uma remereça de dados.
     *
     * @return Retorna a dita remereça de dados.
     */
    T send();

}
