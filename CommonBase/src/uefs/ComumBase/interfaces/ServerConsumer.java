package uefs.ComumBase.interfaces;

import java.io.IOException;

/**
 * Interface responsável por fornecer os métodos de um consumidor de servidor.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <I> Refere-se ao tipo de conexão de entrada.
 * @param <O> Refere-se ao tipo de conexão de saída.
 */
public interface ServerConsumer<I, O> {

    /**
     * Método responsável por criar dados do consumidor.
     *
     * @param connection Refere-se ao objeto de conexão.
     * @return Retorna a resposta da ação.
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    O post(I connection) throws IOException;

    /**
     * Método responsável por buscar dados do consumidor.
     *
     * @param connection Refere-se ao objeto de conexão.
     * @return Retorna a resposta da ação.
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    O get(I connection) throws IOException;

    /**
     * Método responsável por atualizar dados do consumidor.
     *
     * @param connection Refere-se ao objeto de conexão.
     * @return Retorna a resposta da ação.
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    O put(I connection) throws IOException;

    /**
     * Método responsável por apagar dados do consumidor.
     *
     * @param connection Refere-se ao objeto de conexão.
     * @return Retorna a resposta da ação.
     * @throws IOException Refere-se a algum possível erro de entrada/saída.
     */
    O delete(I connection) throws IOException;

}
