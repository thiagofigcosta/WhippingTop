package libgdxUtils;

/**
 *
 * @author reysguep
 */
public class Node<T> {
    public Node(T content){
        this.content = content;
        next = null;
    }
    
    private final T content;
    Node<T> next;
    
    public void setNext(Node<T> node){
        this.next = node;
    }
    
    public Node<T> getNext(){
        return next;
    }
    
    public T getContent(){
        return content;
    }
}
