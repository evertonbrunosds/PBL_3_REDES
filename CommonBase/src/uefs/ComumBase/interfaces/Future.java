package uefs.ComumBase.interfaces;

/**
 * Interface funcional responsável por possibilitar a ocorrência de instruções
 * assíncronas de forma dinâmica.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @param <T> Refere-se ao tipo de stream.
 */
@FunctionalInterface
public interface Future<T> {

    /**
     * Método responsável por executar instruções assíncronas.
     *
     * @param param Refere-se ao parâmetro da execução assíncrona.
     */
    void then(final T param);

}
