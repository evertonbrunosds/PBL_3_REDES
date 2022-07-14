package model;

public class Container<T> {

    private T content;

    public Container() {
        this.content = null;
    }
    
    public boolean isEmpty() {
        return content == null;
    }

    public Container(final T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
