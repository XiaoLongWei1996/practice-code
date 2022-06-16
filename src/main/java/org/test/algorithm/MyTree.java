package org.test.algorithm;

/**
 * @author 肖龙威
 * @date 2022/06/10 15:47
 */
public class MyTree<T> {

    private Node head;

    public void add(int id, T data){
        if (this.head == null) {
            this.head = new Node(id, data);
        } else {
            Node cur = head;
            while (true) {
                if (cur.id == id) {
                    cur.data = data;
                    break;
                } else if (cur.id > id) {
                    if (cur.left == null) {
                        cur.left = new Node(id, data);
                        break;
                    } else {
                        cur = cur.left;
                    }
                } else {
                    if (cur.right == null) {
                        cur.right = new Node(id, data);
                        break;
                    } else {
                        cur = cur.right;
                    }
                }
            }
        }
    }

    public void deleteNode(int id) {
        if (head == null) {
            return;
        }

        if (head.id == id) {
            head = null;
        } else {
            head.deleteAll(id);
        }
    }

    public void deleteNode01(int id) {
        if (head == null) {
            return;
        }

        if (head.id == id) {
            head = null;
        } else {
            head.deleteOne(id);
        }
    }

    public void breList() {
        if (head == null) {
            return;
        }
        head.breOrder();
    }

    public Node breFind(int id) {
        if (head == null) {
            return null;
        }
        return head.breFind(id);
    }

    public void centreList() {
        if (head == null) {
            return;
        }
        head.centreOrder();
    }

    public Node centreFind(int id) {
        if (head == null) {
            return null;
        }
        return head.centreFind(id);
    }

    public void postList() {
        if (head == null) {
            return;
        }
        head.postOrder();
    }

    public Node postFind(int id) {
        if (head == null) {
            return null;
        }
        return head.postFind(id);
    }


    public class Node {
        public int id;
        public T data;
        Node left;
        Node right;

        public Node(int id, T data){
            this.data = data;
            this.id = id;
        }

        /**
         * 前序遍历
         */
        public void breOrder() {
            System.out.println(this.id + "===" + this.data);

            if (left != null) {
                left.breOrder();
            }

            if (right != null) {
                right.breOrder();
            }
        }

        public Node breFind(int id){
            if (this.id == id) {
                return this;
            }

            Node node = null;
            if (left != null) {
                node = left.breFind(id);
            }
            if (node != null){
                return node;
            }

            if (right != null) {
                node = right.breFind(id);
            }
            return node;
        }

        /**
         * 中序遍历
         */
        public void centreOrder() {
            if (left != null) {
                left.centreOrder();
            }

            System.out.println(this.id + "===" + this.data);

            if (right != null) {
                right.centreOrder();
            }
        }

        public Node centreFind(int id) {
            Node node = null;
            if (this.left != null) {
                node = left.centreFind(id);
            }

            if (node != null) {
                return node;
            }

            if (this.id == id) {
                return this;
            }

            if (right != null) {
                node = right.centreFind(id);
            }
            return node;
        }

        /**
         * 后续遍历
         */
        public void postOrder() {
            if (left != null) {
                left.postOrder();
            }

            if (right != null) {
                right.postOrder();
            }

            System.out.println(this.id + "===" + this.data);
        }

        /**
         * 直接删除节点及子节点
         * @param id
         */
        public void deleteAll(int id) {
            if (left != null) {
                if (left.id == id) {
                    this.left = null;
                    return;
                }
                left.deleteAll(id);
            }

            if (right != null) {
                if (right.id == id) {
                    this.right = null;
                    return;
                }
                right.deleteAll(id);
            }
        }

        /**
         * 直接删除节点及子节点
         * @param id
         */
        public void deleteOne(int id) {
            if (left != null) {
                if (left.id == id) {
                    left = left.right;
                    return;
                }
                left.deleteOne(id);
            }

            if (right != null) {
                if (right.id == id) {
                    this.right = right.left;
                    return;
                }
                right.deleteOne(id);
            }
        }

        public Node postFind(int id) {
            Node node = null;
            if (left != null) {
                node = left.postFind(id);
            }

            if (node != null) {
                return node;
            }

            if (right != null) {
                node = right.postFind(id);
            }

            if (this.id == id) {
                return this;
            }

            return node;
        }
    }
}
