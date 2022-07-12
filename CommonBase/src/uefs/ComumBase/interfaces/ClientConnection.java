package uefs.ComumBase.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Interface responsável por fornecer as assinaturas de método para um objeto de
 * conexão para clientes.
 *
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 */
public interface ClientConnection extends Connection<DataInputStream, DataOutputStream> {
}
