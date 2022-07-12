package uefs.ComumBase.interfaces;

import java.io.IOException;

/**
 * Interface responsável por fornecer os métodos de um consumidor de cliente.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public interface ClientConsumer {

    /**
     * Método responsável por criar dados do consumidor.
     *
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    void post() throws IOException;

    /**
     * Método responsável por buscar dados do consumidor.
     *
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    void get() throws IOException;

    /**
     * Método responsável por atualizar dados do consumidor.
     *
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    void put() throws IOException;

    /**
     * Método responsável por apagar dados do consumidor.
     *
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    void delete() throws IOException;

}
