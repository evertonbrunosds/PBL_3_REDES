package uefs.ComumBase.interfaces;

/**
 * Interface responsável por fornecer as assinaturas de métodos de um container.
 *
 * @author Everton Bruno Silva dos Santos.
 * @param <I> Refere-se ao tipo de entrada.
 * @param <O> Refere-se ao tipo de saída.
 */
public interface Container<I, O> {

    /**
     * Método responsável por alterar o conteúdo do container.
     *
     * @param content Refere-se ao novo conteúdo do container.
     */
    void setContent(final I content);

    /**
     * Método responsável por obiter o conteúdo do container.
     *
     * @return Retorna o conteúdo do container.
     */
    O getContent();

}
