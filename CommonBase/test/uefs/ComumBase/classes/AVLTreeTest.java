package uefs.ComumBase.classes;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe responsável por efetuar testes na árvore AVL.
 * @author Everton Bruno Silva dos Santos.
 * @version 1.0
 * @since 1.0
 */
public class AVLTreeTest {
    private AVLTree<Integer, String> avl;
    private final String fileName;
    private int globalCounter;

    public AVLTreeTest() {
        fileName = "FileTest";
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        globalCounter = 0;
        avl = new AVLTree<>(Integer::compareTo);
    }

    @After
    public void tearDown() {
        final File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void tamanhoDeAVLRecemCriada() {
        assertSame(0, avl.size());
    }

    @Test
    public void estadoDeAVLRecemCriada() {
        assertTrue(avl.isEmpty());
    }

    @Test
    public void limparAVLRecemCriada() {
        avl.clear();
        assertSame(0, avl.size());
        assertTrue(avl.isEmpty());
    }

    @Test
    public void inserirEmAVLRecemCriada() {
        avl.put(2020, "A");
        assertSame(1, avl.size());
        assertFalse(avl.isEmpty());
        assertEquals("A", avl.find(2020).getValue());
        assertEquals("A", avl.iterator().next().getValue());
        for (final Entry<Integer, String> entry : avl) {
            assertEquals("A", entry.getValue());
        }
        avl.forEach(entry -> {
            assertEquals("A", entry.getValue());
        });
    }

    @Test
    public void buscarEmAVLRecemCriada() {
        try {
            avl.find(2020);
            fail();
        } catch (final EntryNotFoundException entryNotFoundException) {
            assertEquals("Entry not found.", entryNotFoundException.getMessage());
        }
    }

    @Test
    public void removerEmAVLRecemCriada() {
        try {
            avl.remove(2020);
            fail();
        } catch (final EntryNotFoundException entryNotFoundException) {
            assertEquals("Entry not found.", entryNotFoundException.getMessage());
        } finally {
            assertSame(0, avl.size());
            assertTrue(avl.isEmpty());
        }
    }

    @Test
    public void iteradorDeAVLRecemCriada() {
        assertNull(avl.iterator().next());
        assertFalse(avl.iterator().hasNext());
    }

    @Test
    public void forEachDeAVLRecemCriada() {
        avl.forEach(entry -> {
            fail();
        });
        assertTrue(true);
    }

    @Test
    public void forInDeAVLRecemCriada() {
        for (final Entry<Integer, String> entry : avl) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    public void alterarValorDeEntradaDeAVLRecemCriada() {
        try {
            avl.setValue(20, "B");
            fail();
        } catch (final EntryNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void alterarChaveDeEntradaDeAVLRecemCriada() {
        try {
            avl.setKey(20, 25);
            fail();
        } catch (final EntryNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void duplicarAVLRecemCriada() {
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        avl.put(1, "A");
        assertSame(1, avl.size());
        assertSame(0, avlDuplicated.size());
    }

    @Test
    public void verificarSeContemChaveEmAVLRecemCriada() {
        assertFalse(avl.containsKey(0));
    }

    @Test
    public void gravarEmArquivoAVLRecemCriada() throws IOException {
        avl.saveToFile(fileName);
    }

    @Test
    public void carregarDeArquivoAVLRecemCriada() throws IOException, ClassNotFoundException, ClassCastException {
        final AVLTree<Integer, String> avlTmp = new AVLTree<>(Integer::compareTo);
        avlTmp.put(1997, "Everton Bruno Silva dos Santos");
        avlTmp.saveToFile(fileName);
        avlTmp.clear();
        avl.loadFromFile(fileName);
        assertEquals("Everton Bruno Silva dos Santos", avl.find(1997).getValue());
    }

    @Test
    public void tamanhoDeAVLAposInserir() {
        avl.put(20, "A");
        assertSame(1, avl.size());
        avl.put(23, "B");
        assertSame(2, avl.size());
        avl.put(97, "B");
        assertSame(3, avl.size());
    }

    @Test
    public void limparAVLAposInserir() {
        avl.put(20, "A");
        avl.put(23, "B");
        avl.put(97, "B");
        avl.clear();
        assertSame(0, avl.size());
        assertTrue(avl.isEmpty());
    }

    @Test
    public void estadoDeAVLAposInserir() {
        avl.put(20, "A");
        avl.put(23, "B");
        avl.put(97, "B");
        assertFalse(avl.isEmpty());
    }

    @Test
    public void inserirEmAVLAposInserirChavesDiferentes() {
        avl.put(20, "A");
        avl.put(23, "B");
        assertSame(2, avl.size());
        assertFalse(avl.isEmpty());
        assertEquals("A", avl.find(20).getValue());
        assertEquals("B", avl.find(23).getValue());
        final Iterator<Entry<Integer, String>> iterator = avl.iterator();
        assertEquals("A", iterator.next().getValue());
        assertEquals("B", iterator.next().getValue());
        final String[] array = new String[] { "A", "B" };
        int index = 0;
        for (final Entry<Integer, String> entry : avl) {
            assertEquals(array[index], entry.getValue());
            index++;
        }
    }

    @Test
    public void inserirEmAVLAposInserirChavesIguais() {
        try {
            avl.put(20, "A");
            avl.put(20, "B");
            fail();
        } catch (final KeyUsedException keyUsedException) {
            assertEquals("Key used.", keyUsedException.getMessage());
        } finally {
            assertSame(1, avl.size());
            assertFalse(avl.isEmpty());
            assertEquals("A", avl.iterator().next().getValue());
            for (final Entry<Integer, String> entry : avl) {
                assertEquals("A", entry.getValue());
            }
        }
    }

    @Test
    public void buscarEmAVLAposInserir() {
        avl.put(20, "A");
        avl.put(23, "B");
        assertEquals("A", avl.find(20).getValue());
        assertEquals("B", avl.find(23).getValue());
    }

    @Test
    public void removerDeAVLAposInserir() {
        avl.put(20, "A");
        avl.remove(20);
        assertSame(0, avl.size());
        assertTrue(avl.isEmpty());
        assertNull(avl.iterator().next());
        for (final Entry<Integer, String> entry : avl) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    public void iteradorDeAVLAposInserir() {
        avl.put(20, "A");
        assertEquals("A", avl.iterator().next().getValue());
        assertSame(20, avl.iterator().next().getKey());
        assertTrue(avl.iterator().hasNext());
    }

    @Test
    public void forEachDeAVLAposInserir() {
        avl.put(20, "A");
        avl.forEach(entry -> {
            assertEquals("A", entry.getValue());
            assertSame(20, entry.getKey());
        });
    }

    @Test
    public void forInDeAVLAposInserir() {
        avl.put(20, "A");
        for (final Entry<Integer, String> entry : avl) {
            assertEquals("A", entry.getValue());
            assertSame(20, entry.getKey());
        }
    }

    @Test
    public void alterarValorDeEntrada() {
        avl.put(20, "A");
        assertEquals("A", avl.iterator().next().setValue("B"));
        assertEquals("B", avl.iterator().next().getValue());
        assertEquals("B", avl.find(20).getValue());
        for (final Entry<Integer, String> entry : avl) {
            assertEquals("B", entry.getValue());
        }
        avl.forEach(entry -> {
            assertEquals("B", entry.getValue());
        });
    }

    @Test
    public void alterarValorDeEntradaDeAVLAposInserir() {
        avl.put(20, "A");
        avl.setValue(20, "B");
        assertEquals("B", avl.iterator().next().getValue());
        assertEquals("B", avl.find(20).getValue());
        for (final Entry<Integer, String> entry : avl) {
            assertEquals("B", entry.getValue());
        }
        avl.forEach(entry -> {
            assertEquals("B", entry.getValue());
        });
    }

    @Test
    public void alterarChaveDeEntradaDeAVLAposInserir() {
        avl.put(20, "A");
        avl.setKey(20, 237);
        assertEquals("A", avl.find(237).getValue());
        for (final Entry<Integer, String> entry : avl) {
            assertEquals(new Integer(237), entry.getKey());
        }
        avl.forEach(entry -> {
            assertEquals(new Integer(237), entry.getKey());
        });
    }

    @Test
    public void alterarChaveDeEntradaDeAVLAposInserirChaveDiferenteTratadaComoIgualPeloComparador() {
        final AVLTree<String, Integer> avlString;
        avlString = new AVLTree<>((final String v1, final String v2) -> v1.toLowerCase().compareTo(v2.toLowerCase()));
        avlString.put("SDS", 123);
        avlString.put("SBS", 321);
        avlString.setKey("SDS", "sds");
        final Iterator<Entry<String, Integer>> iterator = avlString.iterator();
        assertSame("SBS", iterator.next().getKey());
        assertSame("sds", iterator.next().getKey());
        assertNull(iterator.next());
    }

    @Test
    public void alterarChaveDeEntradaComMesmaChaveDeAVLAposInserir() {
        avl.put(20, "A");
        avl.put(23, "C");
        try {
            avl.setKey(20, 23);
            fail();
        } catch (final KeyUsedException ex) {
            assertNotNull(ex);
        }
        assertEquals("A", avl.find(20).getValue());
        assertEquals("C", avl.find(23).getValue());
    }

    @Test
    public void duplicarAVLAposInserir() {
        avl.put(1, "A");
        avl.put(2, "B");
        avl.put(3, "C");
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        avlDuplicated.setValue(2, "D");
        avlDuplicated.remove(3);
        avlDuplicated.setKey(1, 0);
        assertSame(3, avl.size());
        assertEquals("A", avl.find(1).getValue());
        assertEquals("B", avl.find(2).getValue());
        assertEquals("C", avl.find(3).getValue());
        assertSame(2, avlDuplicated.size());
        assertEquals("A", avlDuplicated.find(0).getValue());
        assertEquals("D", avlDuplicated.find(2).getValue());
    }

    @Test
    public void verificarSeContemChaveEmAVLAposInserir() {
        avl.put(1, "A");
        assertTrue(avl.containsKey(1));
        assertFalse(avl.containsKey(2));
    }

    @Test
    public void gravarEmArquivoAVLAposInserir() throws IOException, ClassNotFoundException {
        avl.put(1997, "Everton Bruno Silva dos Santos");
        avl.saveToFile(fileName);
        avl.clear();
        avl.loadFromFile(fileName);
        assertEquals("Everton Bruno Silva dos Santos", avl.find(1997).getValue());
    }

    @Test
    public void carregarDeArquivoAVLAposInserir() throws IOException, ClassNotFoundException, ClassCastException {
        final AVLTree<Integer, String> avlTmp = new AVLTree<>(Integer::compareTo);
        avlTmp.saveToFile(fileName);
        avl.put(1997, "Everton Bruno Silva dos Santos");
        avl.loadFromFile(fileName);
        assertTrue(avl.isEmpty());
    }

    @Test
    public void carregarDeArquivoInexistenteEmAVL() {
        try {
            avl.loadFromFile("file not exists");
            fail();
        } catch (final IOException ex) {
            assertNotNull(ex);
        } catch (final ClassNotFoundException | ClassCastException ex) {
            fail();
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLAposInserçõesCrescentes() {
        for (int i = 0; i < 1000; i++) {
            avl.put(i, "A");
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(avl.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLAposInserçõesDecrescentes() {
        for (int i = 1000; i > 0; i--) {
            avl.put(i, "A");
        }
        for (int i = 1000; i > 0; i--) {
            assertTrue(avl.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLAposInserçõesCrescentesSemComparadorNaConstrução() {
        avl = new AVLTree<>();
        for (int i = 0; i < 1000; i++) {
            avl.put(i, "A");
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(avl.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLAposInserçõesDecrescentesSemComparadorNaConstrução() {
        avl = new AVLTree<>();
        for (int i = 1000; i > 0; i--) {
            avl.put(i, "A");
        }
        for (int i = 1000; i > 0; i--) {
            assertTrue(avl.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLDuplicataAposInserçõesCrescentes() {
        for (int i = 0; i < 1000; i++) {
            avl.put(i, "A");
        }
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        for (int i = 0; i < 1000; i++) {
            assertTrue(avlDuplicated.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLDuplicataAposInserçõesDecrescentes() {
        for (int i = 1000; i > 0; i--) {
            avl.put(i, "A");
        }
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        for (int i = 1000; i > 0; i--) {
            assertTrue(avlDuplicated.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLDuplicataAposInserçõesCrescentesSemComparadorNaConstrução() {
        avl = new AVLTree<>();
        for (int i = 0; i < 1000; i++) {
            avl.put(i, "A");
        }
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        for (int i = 0; i < 1000; i++) {
            assertTrue(avlDuplicated.containsKey(i));
        }
    }

    @Test
    public void verificarExistênciaDeChaveEmAVLDuplicataAposInserçõesDecrescentesSemComparadorNaConstrução() {
        avl = new AVLTree<>();
        for (int i = 1000; i > 0; i--) {
            avl.put(i, "A");
        }
        final AVLTree<Integer, String> avlDuplicated = avl.duplicate();
        for (int i = 1000; i > 0; i--) {
            assertTrue(avlDuplicated.containsKey(i));
        }
    }

    @Test
    public void verificarInexistênciaDeChaveEmAVLAposRemoções() {
        for (int i = 0; i < 100; i++) {
            avl.put(i, fileName);
        }
        for (int i = 0; i < 100; i++) {
            assertTrue(avl.containsKey(i));
            avl.remove(i);
            assertFalse(avl.containsKey(i));
            for (int j = i + 1; j < 100; j++) {
                assertTrue(avl.containsKey(j));
            }
        }
    }

    @Test
    public void forInReverso() {
        for (int i = 0; i <= 100; i++) {
            avl.put(i, "");
        }
        avl.setReverseIterations(true);
        int counter = 100;
        for (final Entry<Integer, String> entry : avl) {
            assertSame(counter, entry.getKey());
            counter--;
        }
    }

    @Test
    public void forEachReverso() {
        for (int i = 0; i <= 100; i++) {
            avl.put(i, "");
        }
        avl.setReverseIterations(true);
        globalCounter = 100;
        avl.forEach(entry -> {
            assertSame(globalCounter, entry.getKey());
            globalCounter--;
        });
    }

    @Test
    public void iteratorReverso() {
        for (int i = 0; i <= 100; i++) {
            avl.put(i, "");
        }
        avl.setReverseIterations(true);
        globalCounter = 100;
        final Iterator<Entry<Integer, String>> iterator = avl.iterator();
        while (iterator.hasNext()) {
            assertSame(globalCounter, iterator.next().getKey());
            globalCounter--;
        }
    }

}