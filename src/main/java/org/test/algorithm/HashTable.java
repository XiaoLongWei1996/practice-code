package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/06/09 15:58
 */
public class HashTable<T> {

    private HashTable.LinkList[] linkLists;

    private int size;

    public HashTable(int size){
        this.size = size;
        linkLists = new HashTable.LinkList[size];
        for (int i = 0; i < size; i++){
            linkLists[i] = new HashTable.LinkList();
        }
    }

    public void add(int id, T data){
        int index = hashCode(id);
        HashTable.LinkList linkLists = this.linkLists[index];
        linkLists.addNode(id, data);
    }

    public T get(int id) {
        int index = hashCode(id);
        HashTable.LinkList linkLists = this.linkLists[index];
        return (T) linkLists.getNodeData(id);
    }

    public void delete(int id) {
        int index = hashCode(id);
        HashTable.LinkList linkLists = this.linkLists[index];
        linkLists.delete(id);
    }

    public void list(){
        for (LinkList linkList : linkLists) {
            linkList.list();
            System.out.println();
        }
    }

    public int hashCode(int id) {
        return id % this.size;
    }

    private class LinkList {
        HashTable.Node head;

        public void addNode(int id, T data){
            if (head == null) {
                head = new HashTable.Node(id, data);
            } else {
                head.next = new HashTable.Node(id, data);
            }
        }

        public void delete(int id) {
            if (head.id == id) {
                head = head.next;
            } else {
                HashTable.Node node = head;
                while (true) {
                    HashTable.Node next = node.next;
                    if (next == null) {
                        break;
                    } else {
                        if (id == next.id) {
                            node.next = next.next;
                            break;
                        }
                    }
                    node = next;
                }
            }
        }

        public T getNodeData(int id) {
            if (head.id == id) {
                return (T) head.data;
            } else {
                HashTable.Node node = head;
                while (true) {
                    HashTable.Node next = node.next;
                    if (next == null) {
                        break;
                    } else {
                        if (id == next.id) {
                            return (T) next.data;
                        }
                    }
                    node = next;
                }
            }
            return null;
        }

        public void list(){
            HashTable.Node node = head;
            while (true) {
                if (node == null) {
                    break;
                }
                System.out.print(node.data + " ");
                node = node.next;
            }
        }

    }

    private class Node {
        int id;
        T data;
        Node next;
        public Node(int id, T data){
            this.data = data;
            this.id = id;
        }
    }
}
