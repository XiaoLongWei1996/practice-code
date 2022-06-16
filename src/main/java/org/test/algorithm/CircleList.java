package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/05/27 10:53
 */
public class CircleList<T>{

    private Node first;

    private Node tail;

    private Node pre;

    private Node current;

    public void add(T data){
        Node node = new Node(data);
        if (first == null) {
            first = node;
            tail = node;
            tail.next = first;
        } else {
            Node n = first;
            while (true) {
                if (n.next == first) {
                    n.next = node;
                    tail = node;
                    tail.next = first;
                    break;
                }
                n = n.next;
            }
        }
    }

    public void show(){
        Node n = first;
        while (true) {
            System.out.print(n.data);
            if (n.next == first) {
                break;
            }
            System.out.print("---->");
            n = n.next;
        }
    }

    public T pop(int num){
        T s = null;
        if (pre ==null && current == null) {
            if (first == null) {
                return s;
            }
            pre = tail;
            current = first;
        }
        int index = 1;
        while (true) {
            if (index >= num) {
                break;
            }
            current = current.next;
            pre = pre.next;
            index++;
        }
        s = current.data;
        if (current == pre) {
            first = null;
            tail = null;
            current = null;
            pre = null;
        }
        if (current == first) {
            first = current.next;
        }
        if (current == tail) {
            tail = pre;
        }
        pre.next = current.next;
        return s;
    }

    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
        }

    }
}
