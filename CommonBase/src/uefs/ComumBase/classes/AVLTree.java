package uefs.ComumBase.classes;

import java.io.Serializable;
import java.util.Map.Entry;
import static java.lang.Math.max;
import static java.lang.Integer.compare;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import uefs.ComumBase.interfaces.Comparator;
import uefs.ComumBase.interfaces.Duplicable;
import uefs.ComumBase.interfaces.FileStream;
import uefs.ComumBase.interfaces.Receiver;

/**
 * Classe responsável por comportar-se como árvore AVL.
 *
 * @author Everton Bruno Silva dos Santos.
 * @param <K> Refere-se ao tipo de chave usada nas entradas.
 * @param <V> Refere-se ao tipo de valor usado nas entradas.
 * @version 1.0
 * @since 1.0
 */
public class AVLTree<K, V> implements Iterable<Entry<K, V>>, Duplicable<AVLTree<K, V>>, FileStream<AVLTree<K, V>> {

    /**
     * Refere-se ao número de série da árvore AVL.
     */
    private transient static final long serialVersionUID = -9185946958686492020L;
    /**
     * Refere-se ao objeto comparador de chaves.
     */
    private Comparator<K> comparator;
    /**
     * Refere-se ao objeto raiz da árvore.
     */
    private Node root;
    /**
     * Refere-se ao tamanho da árvore.
     */
    private int size;
    /**
     * Refere-se a indicativo de que as iterações devem ser reversas.
     */
    private boolean reverseIterations;

    /**
     * Construtor responsável pelo instanciamento da árvore.
     */
    public AVLTree() {
        this.comparator = (final K o1, final K o2) -> compare(o1.hashCode(), o2.hashCode());
        this.reverseIterations = false;
    }

    /**
     * Construtor responsável pelo instanciamento da árvore.
     *
     * @param comparator Refere-se ao objeto comparador de chaves.
     * @throws NullPointerException Exceção lançada caso o comparador de chaves
     * seja nulo.
     */
    public AVLTree(final Comparator<K> comparator) throws NullPointerException {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
        this.size = 0;
        this.reverseIterations = false;
    }

    /**
     * Construtor responsável pelo instanciamento da árvore.
     *
     * @param comparator Refere-se ao objeto comparador de chaves.
     * @param root Refere-se ao objeto raiz da árvore.
     * @param size Refere-se ao tamanho da árvore.
     * @param reverseIterations Refere-se a indicativo de que as iterações devem
     * ser reversas.
     */
    private AVLTree(final Comparator<K> comparator, final Node root, final int size, final boolean reverseIterations) {
        this.comparator = comparator;
        this.root = root;
        this.size = size;
        this.reverseIterations = reverseIterations;
    }

    /**
     * Método responsável por retornar o tamanho da árvore.
     *
     * @return Retorna o tamanho da árvore.
     */
    public int size() {
        return size;
    }

    /**
     * Método responsável por esvaziar a árvore.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Método responsável por indicar se a árvore está vazia.
     *
     * @return Retorna indicativo de que a árvore está vazia.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Método responsável por inserir uma nova entrada na árvore.
     *
     * @param key Refere-se a chave da dita entrada.
     * @param value Refere-se ao valor da dita entrada.
     * @throws KeyUsedException Exceção lançada no caso da chave estar em uso
     * por outra entrada.
     */
    public void put(final K key, final V value) throws KeyUsedException {
        root = put(key, value, root);
        size++;
    }

    /**
     * Método responsável por inserir uma nova entrada na árvore recursivamente.
     *
     * @param key Refere-se a chave da dita entrada.
     * @param value Refere-se ao valor da dita entrada.
     * @param node Refere-se ao elo atual da recursão.
     * @return Retorna árvore reconstruída com nova entrada incluída.
     * @throws KeyUsedException Exceção lançada no caso da chave estar em uso
     * por outra entrada.
     */
    private Node put(final K key, final V value, final Node node) throws KeyUsedException {
        if (node == null) {
            return new Node(key, value);
        }
        final int result = comparator.compare(node.key, key);
        if (result > 0) {
            node.left = put(key, value, node.left);
        } else if (result < 0) {
            node.right = put(key, value, node.right);
        } else {
            throw new KeyUsedException();
        }
        return adjustHeight(node);
    }

    /**
     * Método responsável por encontrar uma entrada contida na árvore.
     *
     * @param key Refere-se a chave de acesso à dita entrada.
     * @return Retorna a dita entrada detentora da chave.
     * @throws EntryNotFoundException Exceção lançada no caso da entrada não ser
     * encontrada.
     */
    public Entry<K, V> find(final K key) throws EntryNotFoundException {
        final Entry<K, V> entry = find(key, root);
        if (entry == null) {
            throw new EntryNotFoundException();
        }
        return entry;
    }

    /**
     * Método responsável por indicar se determinada entrada contém dada chave.
     *
     * @param key Refere-se a dita chave.
     * @return Retorna indicativo de que determinada entrada contém dada chave.
     */
    public boolean containsKey(final K key) {
        return find(key, root) != null;
    }

    /**
     * Método responsável por encontrar uma entrada contida na árvore
     * recursivamente.
     *
     * @param key Refere-se a chave de acesso à dita entrada.
     * @param node Refere-se ao elo atual da recursão.
     * @return Retorna a dita entrada detentora da chave.
     */
    private Node find(final K key, final Node node) {
        if (node == null) {
            return null;
        }
        final int result = comparator.compare(node.key, key);
        if (result > 0) {
            return find(key, node.left);
        } else if (result < 0) {
            return find(key, node.right);
        } else {
            return node;
        }
    }

    /**
     * Método responsável por alterar o valor de determinada entrada na árvore.
     *
     * @param key Refere-se a chave de acesso à entrada.
     * @param value Refere-se ao novo valor da entrada.
     * @return Retorna o antigo valor da chave.
     * @throws EntryNotFoundException Exceção lançada no caso da entrada não ser
     * encontrada.
     */
    public V setValue(final K key, final V value) throws EntryNotFoundException {
        return find(key).setValue(value);
    }

    /**
     * Método responsável por substituir dada chave de uma entrada por outra na
     * árvore.
     *
     * @param currentKey Refere-se a chave atual.
     * @param newKey Refere-se a nova chave.
     * @throws KeyUsedException Exceção lançada no caso da nova chave estar em
     * uso por outra entrada.
     * @throws EntryNotFoundException Exceção lançada no caso da entrada não ser
     * encontrada.
     */
    public void setKey(final K currentKey, final K newKey) throws KeyUsedException, EntryNotFoundException {
        final Node node = find(currentKey, root);
        if (node == null) {
            throw new EntryNotFoundException();
        } else if (comparator.compare(currentKey, newKey) == 0) {
            node.key = newKey;
        } else if (find(newKey, root) == null) {
            remove(currentKey);
            put(newKey, node.value);
        } else {
            throw new KeyUsedException();
        }
    }

    /**
     * Método responsável por duplicar a árvore.
     *
     * @return Retorna árvore duplicata.
     */
    @Override
    public AVLTree<K, V> duplicate() {
        return !isEmpty() ? new AVLTree<>(comparator, root.duplicate(), size, reverseIterations) : new AVLTree<>(comparator);
    }

    /**
     * Método responsável por remover uma entrada contida na árvore.
     *
     * @param key Refere-se a chave de acesso à dita entrada.
     * @throws EntryNotFoundException Exceção lançada no caso da entrada não ser
     * encontrada.
     */
    public void remove(final K key) throws EntryNotFoundException {
        root = remove(key, root);
        size--;
    }

    /**
     * Método responsável por remover uma entrada contida na árvore
     * recursivamente.
     *
     * @param key Refere-se a chave de acesso à dita entrada.
     * @param node Refere-se ao elo atual da recursão.
     * @return Retorna árvore reconstruída com o dita entrada ausente.
     * @throws EntryNotFoundException Exceção lançada no caso da entrada não ser
     * encontrada.
     */
    private Node remove(final K key, final Node node) throws EntryNotFoundException {
        if (node == null) {
            throw new EntryNotFoundException();
        }
        final int result = comparator.compare(node.key, key);
        if (result > 0) {
            node.left = remove(key, node.left);
        } else if (result < 0) {
            node.right = remove(key, node.right);
        } else if (node.isSubThree()) {
            Node tmpNode = node.left;
            while (node.right != null) {
                tmpNode = tmpNode.right;
            }
            final K tmpKey = tmpNode.key;
            final V tmpValue = tmpNode.value;
            tmpNode.key = node.key;
            tmpNode.value = node.value;
            node.key = tmpKey;
            node.value = tmpValue;
            node.left = remove(key, node.left);
        } else if (node.leftIsNotNull()) {
            return node.left;
        } else if (node.rightIsNotNull()) {
            return node.right;
        } else {
            return null;
        }
        return adjustHeight(node);
    }

    /**
     * Método responsável por retornar estrutura iterável de entradas contidas
     * na árvore.
     *
     * @return Retorna estrutura iterável de entradas contidas na árvore.
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        final MyIterator<Entry<K, V>> myIterator = new MyIterator<>();
        forEach(entry -> {
            myIterator.elements.add(entry);
        });
        return myIterator;
    }

    /**
     * Método responsável por percorrer por entradas contidas na árvore.
     *
     * @param entry Refere-se as entradas da árvore detentoras de valores e
     * chaves.
     */
    @Override
    public void forEach(final Consumer<? super Entry<K, V>> entry) {
        final Receiver<Node> forEach;
        if (isReverseIterations()) {
            forEach = new Receiver<Node>() {
                private transient static final long serialVersionUID = -2032273739428212712L;

                @Override
                public void receive(final Node node) {
                    if (node != null) {
                        receive(node.right);
                        entry.accept(node);
                        receive(node.left);
                    }
                }
            };
        } else {
            forEach = new Receiver<Node>() {
                private transient static final long serialVersionUID = 9019470629284711398L;

                @Override
                public void receive(final Node node) {
                    if (node != null) {
                        receive(node.left);
                        entry.accept(node);
                        receive(node.right);
                    }
                }
            };
        }
        forEach.receive(root);
    }

    /**
     * Método responsável por alterar os dados da árvore em fluxo.
     *
     * @param newData Refere-se aos novos dados da árvore.
     */
    @Override
    public void set(final AVLTree<K, V> newData) {
        this.comparator = newData.comparator;
        this.root = newData.root;
        this.size = newData.size;
    }

    /**
     * Método responsável por alterar indicação de que as iterações devem ser
     * reversas.
     *
     * @param reverseIterations Refere-se a reversão das iterações.
     */
    public void setReverseIterations(final boolean reverseIterations) {
        this.reverseIterations = reverseIterations;
    }

    /**
     * Método responsável por indicar se as iterações devem ser reversas.
     *
     * @return Retorna indicativo de que as iterações devem ser reversas.
     */
    public boolean isReverseIterations() {
        return reverseIterations;
    }

    /**
     * Método responsável por efetuar rotações simples a esquerda em elos da
     * árvore.
     *
     * @param newRoot Refere-se ao novo elo raiz.
     * @param oldRoot Refere-se ao antigo elo raiz.
     * @return Retorna elo raiz de árvore rotacionada a esquerda.
     */
    private Node simpleRotationLeft(final Node newRoot, final Node oldRoot) {
        oldRoot.left = newRoot.right;
        oldRoot.updateHeightAndBalancing();
        newRoot.right = oldRoot;
        newRoot.updateHeightAndBalancing();
        return newRoot;
    }

    /**
     * Método responsável por efetuar rotações simples a direita em elos da
     * árvore.
     *
     * @param newRoot Refere-se ao novo elo raiz.
     * @param oldRoot Refere-se ao antigo elo raiz.
     * @return Retorna elo raiz de árvore rotacionada a direita.
     */
    private Node simpleRotationRight(final Node newRoot, final Node oldRoot) {
        oldRoot.right = newRoot.left;
        oldRoot.updateHeightAndBalancing();
        newRoot.left = oldRoot;
        newRoot.updateHeightAndBalancing();
        return newRoot;
    }

    /**
     * Método responsável por efetuar rotações duplas a esquerda em elos da
     * árvore.
     *
     * @param left Refere-se ao elo a esquerda.
     * @param oldRoot Refere-se ao antigo elo raiz.
     * @return Retorna elo raiz de árvore duplamente rotacionada a esquerda.
     */
    private Node doubleRotationLeft(final Node left, final Node oldRoot) {
        oldRoot.left = left.right;
        left.right = oldRoot.left.left;
        left.updateHeightAndBalancing();
        oldRoot.left.left = left;
        oldRoot.left.updateHeightAndBalancing();
        oldRoot.updateHeightAndBalancing();
        return simpleRotationLeft(oldRoot.left, oldRoot);
    }

    /**
     * Método responsável por efetuar rotações duplas a direita em elos da
     * árvore.
     *
     * @param right Refere-se ao elo a direita.
     * @param oldRoot Refere-se ao antigo elo raiz.
     * @return Retorna elo raiz de árvore duplamente rotacionada a direita.
     */
    private Node doubleRotationRight(final Node right, final Node oldRoot) {
        oldRoot.right = right.left;
        right.left = oldRoot.right.right;
        right.updateHeightAndBalancing();
        oldRoot.right.right = right;
        oldRoot.right.updateHeightAndBalancing();
        oldRoot.updateHeightAndBalancing();
        return simpleRotationRight(oldRoot.right, oldRoot);
    }

    /**
     * Método responsável por ajustar a altura de determinado elo da árvore.
     *
     * @param node Refere-se ao dito elo a ter sua altura ajustada.
     * @return Retorna elo raiz da árvore reconstruída com altura ajustada.
     */
    private Node adjustHeight(final Node node) {
        node.updateHeightAndBalancing();
        if (node.balancing <= -2) {
            if (node.balancing * node.left.balancing > 0) {
                return simpleRotationLeft(node.left, node);
            } else {
                return doubleRotationLeft(node.left, node);
            }
        } else if (node.balancing >= 2) {
            if (node.balancing * node.right.balancing > 0) {
                return simpleRotationRight(node.right, node);
            } else {
                return doubleRotationRight(node.right, node);
            }
        }
        return node;
    }

    /**
     * Classe responsável por comportar-se como elo da árvore.
     *
     * @author Everton Bruno Silva dos Santos.
     * @version 1.0
     * @since 1.0
     */
    private class Node implements Entry<K, V>, Serializable, Duplicable<Node> {

        /**
         * Refere-se ao número de série do elo da árvore AVL.
         */
        private transient static final long serialVersionUID = -9185946958686495728L;
        /**
         * Refere-se a chave contida no elo.
         */
        private K key;
        /**
         * Refere-se ao valor contido no elo.
         */
        private V value;
        /**
         * Refere-se a altura do elo.
         */
        private int height;
        /**
         * Refere-se ao valor de balanceamento contido no elo.
         */
        private int balancing;
        /**
         * Refere-se ao elo a esquerda.
         */
        private Node left;
        /**
         * Refere-se ao elo a direita.
         */
        private Node right;

        /**
         * Construtor responsável pelo instanciamento do elo.
         *
         * @param key Refere-se a chave contida no elo.
         * @param value Refere-se ao valor contido no elo.
         */
        private Node(final K key, final V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
            this.balancing = 0;
        }

        /**
         * Construtor responsável pelo instanciamento do elo.
         *
         * @param key Refere-se a chave contida no elo.
         * @param value Refere-se ao valor contido no elo.
         * @param height Refere-se a altura do elo.
         * @param balancing Refere-se ao valor de balanceamento contido no elo.
         */
        private Node(final K key, final V value, final int height, final int balancing) {
            this.key = key;
            this.value = value;
            this.height = height;
            this.balancing = balancing;
        }

        /**
         * Método responsável por indicar se o elo é uma sub-árvore.
         *
         * @return Retorna indicativo de que o elo é uma sub-árvore.
         */
        private boolean isSubThree() {
            return leftIsNotNull() && rightIsNotNull();
        }

        /**
         * Método responsável por indicar se o elo não é nulo a esquerda.
         *
         * @return Retorna indicativo de que o elo não é nulo a esquerda.
         */
        private boolean leftIsNotNull() {
            return left != null;
        }

        /**
         * Método responsável por indicar se o elo não é nulo a direita.
         *
         * @return Retorna indicativo de que o elo não é nulo a direita.
         */
        private boolean rightIsNotNull() {
            return right != null;
        }

        /**
         * Método responsável por atualizar a altura e o balanceamento contido
         * no elo.
         */
        private void updateHeightAndBalancing() {
            if (isSubThree()) {
                height = 1 + max(left.height, right.height);
                balancing = -left.height + right.height;
            } else if (leftIsNotNull()) {
                height = 1 + left.height;
                balancing = -left.height;
            } else if (rightIsNotNull()) {
                height = 1 + right.height;
                balancing = +right.height;
            } else {
                height = 1;
                balancing = 0;
            }
        }

        /**
         * Método responsável por retornar chave contida no elo.
         *
         * @return Retorna chave contida no elo.
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Método responsável por retornar valor contido no elo.
         *
         * @return Retorna valor contido no elo.
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Método responsável por alterar valor contido no elo.
         *
         * @param value Refere-se ao novo valor contido no elo.
         * @return Retorna antigo valor contido no elo.
         */
        @Override
        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        /**
         * Método responsável por duplicar o elo.
         *
         * @return Retorna elo duplicata.
         */
        @Override
        public Node duplicate() {
            final Node node = new Node(key, value, height, balancing);
            if (leftIsNotNull()) {
                node.left = left.duplicate();
            }
            if (rightIsNotNull()) {
                node.right = right.duplicate();
            }
            return node;
        }

    }

    /**
     * Classe responsável por comportar-se como uma estrutura iterável.
     *
     * @author Everton Bruno Silva dos Santos.
     * @param <T> Refere-se ao tipo de dados iterável.
     * @version 1.0
     * @since 1.0
     */
    private class MyIterator<T> implements Iterator<T>, Serializable {

        /**
         * Refere-se ao número de série do iterador da árvore.
         */
        private transient static final long serialVersionUID = -2020946958686495728L;
        /**
         * Refere-se aos elementos contidos no iterador.
         */
        private final LinkedList<T> elements;

        /**
         * Construtor responsável pelo instanciamento da estrutura iterável.
         */
        private MyIterator() {
            this.elements = new LinkedList<>();
        }

        /**
         * Método responsável por indicar se há um próximo elemento.
         *
         * @return Retorna indicativo de que há um próximo elemento.
         */
        @Override
        public boolean hasNext() {
            return !elements.isEmpty();
        }

        /**
         * Método responsável por retornar um próximo elemento.
         *
         * @return Retorna um próximo elemento.
         */
        @Override
        public T next() {
            return !elements.isEmpty() ? elements.removeFirst() : null;
        }

    }

}
