package uefs.ComumBase.interfaces;

import uefs.ComumBase.classes.ServerConnection;
import uefs.ComumBase.classes.ClientConnection;
import java.io.IOException;
import static java.lang.Thread.sleep;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe responsável por fornecer os testes para um objeto de conexão.
 *
 * @author Everton Bruno Silva dos Santos - 1911746
 * @version 1.0
 */
public class ConnectionTest {

    private ClientConnection connectionClient;
    private ServerConnection connectionServer;

    public ConnectionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        final int port = 1997;
        connectionClient = new ClientConnection("127.0.0.1", port);
        connectionServer = new ServerConnection(port);
    }

    @After
    public void tearDown() throws IOException {
        connectionServer.close();
    }

    @Test
    public void testInputClientOutputServer() throws IOException, InterruptedException {
        Factory.thread(() -> {
            try {
                connectionServer.send(stream -> {
                    stream.flush();
                    stream.writeUTF("Olá, mundo!");
                });
            } catch (final IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.receive(stream -> {
            assertEquals("Olá, mundo!", stream.readUTF());
        });
    }

    @Test
    public void testInputServerOutputClient() throws IOException, InterruptedException {
        Factory.thread(() -> {
            try {
                connectionServer.receive(stream -> {
                    assertEquals("Olá, Brasil!", stream.readUTF());
                });
            } catch (final IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.send(stream -> {
            stream.flush();
            stream.writeUTF("Olá, Brasil!");
        });
    }

    @Test
    public void testGetAndPoust() throws IOException, InterruptedException {
        Factory.thread(() -> {
            try {
                connectionServer.sendReceive((inputStream, outputStream) -> {
                    final String msg = inputStream.readUTF();
                    outputStream.flush();
                    outputStream.writeUTF(msg.concat(" Bruno"));
                });
            } catch (final IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.sendReceive((inputStream, outputStream) -> {
            outputStream.flush();
            outputStream.writeUTF("Everton");
            assertEquals("Everton Bruno", inputStream.readUTF());
        });
    }

    @Test
    public void testInputFutureServerOutputClient() throws InterruptedException, IOException {
        Factory.thread(() -> {
            try {
                connectionServer.receiveFuture(iOException -> {
                    fail(iOException.getMessage());
                }).then(stream -> {
                    assertEquals("Olá, Brasil!", stream.readUTF());
                });
            } catch (IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.send(stream -> {
            stream.flush();
            stream.writeUTF("Olá, Brasil!");
        });
    }

    @Test
    public void testInputClientOutputFutureServer() throws IOException, InterruptedException {
        Factory.thread(() -> {
            try {
                connectionServer.sendFuture(iOException -> {
                    fail(iOException.getMessage());
                }).then(stream -> {
                    stream.flush();
                    stream.writeUTF("Olá, mundo!");
                });
            } catch (IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.receive(stream -> {
            assertEquals("Olá, mundo!", stream.readUTF());
        });
    }

    @Test
    public void testGetFutureAndPoustFuture() throws IOException, InterruptedException {
        Factory.thread(() -> {
            try {
                connectionServer.sendReceive(iOException -> {
                    fail(iOException.getMessage());
                }).then((inputStream, outputStream) -> {
                    final String msg = inputStream.readUTF();
                    outputStream.flush();
                    outputStream.writeUTF(msg.concat(" Bruno"));
                });
            } catch (IOException ex) {
                fail("Falha de entrada/saída");
            }
        }).start();
        sleep(1000);
        connectionClient.sendReceive((inputStream, outputStream) -> {
            outputStream.flush();
            outputStream.writeUTF("Everton");
            assertEquals("Everton Bruno", inputStream.readUTF());
        });
    }

}
