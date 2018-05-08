package libgdxUtils;

/**
 *
 * @author reysguep
 */
public class CircularLinkedList<T> {

    public CircularLinkedList() {
        head = null;
        p = null;
    }
    Node<T> head, p;

    public void add(T content) {
        if (head == null) {
            head = new Node<T>(content);
            head.setNext(head);
            p = head;
        } else {
            Node<T> point;
            point = head;
            while(point.getNext() != head){
                point = point.getNext();
            }
            
            point.setNext(new Node<T>(content));
            point.getNext().setNext(head);
        }
    }
    
    public T get(){
        T content = p.getContent();
        p = p.getNext();
        return content;
    }
}
