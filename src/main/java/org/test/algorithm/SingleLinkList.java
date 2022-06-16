package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/05/26 14:43
 */
public class SingleLinkList<T> {

    private Node first;

    public void add(T data) {
        if (first == null) {
            first = new Node(data);
        } else {
            Node node = first;
            while (true) {
                Node n = node.next;
                if (n == null) {
                    n = (new Node(data));
                    node.next = n;
                    return;
                } else {
                    node = node.next;
                }
            }
        }
    }

    public void delete(T data) {
        if (first == null) {
            throw new NullPointerException();
        }
        if (first.data.equals(data)) {
            first = first.next;
        } else {
            Node n = first;
            while (true) {
                Node n1 = n.next;
                if (n1 == null) {
                    return;
                } else {
                    if (n1.data.equals(data)) {
                        n.next = n1.next;
                        n1.next = null; //让GC回收被删除的这个节点
                        return;
                    }
                }
                n = n.next;
            }
        }
    }

    public void update(T data, T update) {
        if (first == null) {
            throw new NullPointerException();
        }
        if (first.data.equals(data)) {
            first.data = update;
        } else {
            Node n = first.next;
            while (true) {
                if (n == null) {
                    return;
                } else {
                    if (n.data.equals(data)) {
                        n.data = update;
                        return;
                    }
                }
                n = n.next;
            }
        }
    }

    public int size() {
        if (first == null) {
            return 0;
        }
        int count = 0;
        Node node = first;
        while (true) {
            if (node == null) {
                return count;
            }
            node = node.next;
            count++;
        }
    }

    /**
     * 获取链表角标元素,大于0正向获取,小于0反向获取
     * @param index
     * @return
     */
    public T get(int index) {
        T data = null;
        int size = size();
        if (first == null) {
            return data;
        }
        if (index >= 0) {
            if (index >= size) {
                throw new IndexOutOfBoundsException();
            }
            Node node = first;
            for (int i = 1; i <= index; i++) {
                node = node.next;
            }
            data = node.data;
        } else {
            if (Math.abs(index) > size) {
                throw new IndexOutOfBoundsException();
            }
            Node node = first;
            for (int i = 1,j = size + index; i <= j; i++) {
                node = node.next;
            }
            data = node.data;
        }
        return data;
    }

    public void reverse(){
        if (first == null) {
            throw new NullPointerException();
        }
        Node newFirst = first;
        Node node = first.next;
        first.next = null;
        Node n = null;
        while (true) {
            if (node == null){
                break;
            }
            n = node.next;
            node.next = newFirst;
            newFirst = node;
            node = n;
        }
        first = newFirst;
    }



    public void list() {
        if (first == null) {
            throw new RuntimeException("空链表");
        }
        Node n = first;
        while (true) {
            if (n == null) {
                return;
            }
            System.out.println(n.data);
            n = n.next;
        }
    }

    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
        }

    }
}
