public class LinkedList {
    Node head;
    Node tail;

    public LinkedList(int x, int y) {
        head = new Node(x, y);
        tail = head;
    }

    // Add a new node at the end (when the snake grows)
    public void addNode(int x, int y) {
        Node newNode = new Node(x, y);
        tail.next = newNode;
        tail = newNode;
    }

    // Move the snake by updating positions of the nodes
    public void move(int x, int y, boolean grow) {
        Node newNode = new Node(x, y);
        newNode.next = head;
        head = newNode;

        if (!grow) {
            // Remove the tail
            Node temp = head;
            while (temp.next != tail) {
                temp = temp.next;
            }
            temp.next = null;
            tail = temp;
        }
    }

    // Check if the snake collides with itself
    public boolean checkCollision() {
        Node current = head.next;
        while (current != null) {
            if (current.x == head.x && current.y == head.y) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
