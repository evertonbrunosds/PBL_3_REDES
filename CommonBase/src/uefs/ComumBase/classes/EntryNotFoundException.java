package uefs.ComumBase.classes;

/**
 * Classe responsável por comportar-se como exceção de entrada não encontrada.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @since 1.0
 */
public class EntryNotFoundException extends RuntimeException {

    /**
     * Refere-se ao número de série da exceção de entrada não encontrada.
     */
    private transient static final long serialVersionUID = -2020384390486923525L;

    /**
     * Construtor responsável pelo instanciamento da exceção de entrada não
     * encontrada.
     */
    public EntryNotFoundException() {
        super("Entry not found.");
    }

}
