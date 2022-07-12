package uefs.ComumBase.classes;

/**
 * Classe responsável por comportar-se como exceção de chave em uso.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @since 1.0
 */
public class KeyUsedException extends RuntimeException {

    /**
     * Refere-se ao número de série da exceção de chave em uso.
     */
    private transient static final long serialVersionUID = -5226384390486923525L;

    /**
     * Construtor responsável pelo instanciamento da exceção de chave em uso.
     */
    public KeyUsedException() {
        super("Key used.");
    }

}
