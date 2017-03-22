package edu.sv.dynamic;

import java.util.*;

/**
 * Task:
 * Fight Club.
 * По кругу стоят n бойцов.
 * Результат боя каждой пары бойцов известен (есть матрица N x N). От перестановки бойцов результат боя не меняется, т.е. (i , j) дает тот же результат что и (j, i).
 * На каждом шаге случайно выбирается один боец который сражается с тем, кто стоит от него справа. Проигравший выбывает.
 * Найти список всех бойцов, которые могут потенциально (при каком то из раскладов случайных выборов) победить в этом соревновании.
 *
 * @author ampopov
 */
public class FightClub {
    // Soldier #row wins soldier #column if Win[#row][#column] == 1
    public static int[][] Win = {
//            {0, 1, 0, 1, 0},  //0
//            {0, 0, 0, 0, 0},  //1
//            {1, 1, 0, 1, 1},  //2
//            {0, 1, 0, 0, 1},  //3
//            {1, 1, 0, 0, 0},  //4

//            {-1, 0, 1,},  //0
//            {1, -1, 0,},  //1
//            {0, 1, -1,},  //2
//
            {-1, 0, 1, 0,},  //0
            {1, -1, 1, 0,},  //1
            {0, 0, -1, 1,},  //2
            {1, 1, 0, -1,},  //3
    };
    public static int N = Win.length;

    public static void main(String[] args) {
        System.out.println("Potential winners:");
        for (int soldier : getPotentialWinners()) {
            System.out.print(soldier + ", ");
        }
        System.out.println();
    }

    public static List<Integer> getPotentialWinners() {
        List<Integer> soldiers = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (checkSoldierCouldWin(i)) soldiers.add(i);
        }
        return soldiers;
    }

    /**
     * Determin if soldier <code>k</code> could win.
     * Complexity ~ n^2 for one soldier.
     *
     * @param k
     * @return
     */
    private static boolean checkSoldierCouldWin(int k) {
        List<Boolean> notAddedToSeq = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            notAddedToSeq.add(true);
        }
        SeqList seq = new SeqList();
        SeqList.Node n = seq.init(k);
//        System.out.println(Arrays.toString(seq.toArray()));

        //Kind of breadth-first search
        Queue<SeqList.Node> queueNotVisited = new LinkedList<>();
        queueNotVisited.offer(n);
        while (!queueNotVisited.isEmpty()) {
            SeqList.Node nodeToVisit = queueNotVisited.poll();
//            System.out.println("el = " + nodeToVisit.val);
            for (int idx = ((nodeToVisit.val - 1) % N + N) % N; idx != nodeToVisit.val; idx = ((idx - 1) % N + N) % N) {
                //we start from the most distant element to the right (left neighbor)
                if (!notAddedToSeq.get(idx)) continue;
                if (Win[nodeToVisit.val][idx] == 1) {
                    SeqList.Node addedNode = seq.tryToAdd(nodeToVisit, idx, k);
                    if (addedNode != null) {
                        queueNotVisited.offer(addedNode);
                        notAddedToSeq.set(addedNode.val, false);
                    }
//                    System.out.println(Arrays.toString(seq.toArray()));
                }
            }
            notAddedToSeq.set(nodeToVisit.val, false);
        }
        return seq.size() == N;
    }
}

class SeqList {
    private int size;

    class Node {
        int val;
        Node next;
        Node prev;

        public Node(int val) {
            this.val = val;
        }

        public Node(int newVal, Node nex, Node pre) {
            val = newVal;
            next = nex;
            prev = pre;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    '}';
        }
    }

    private Node head;
    private Node tail;

    public SeqList() {
    }

    public Node init(int initVal) {
        ++size;
        return this.head = tail = new Node(initVal);
    }

    /**
     * Head is always min.  Tail - max.
     * Adds <code>newVal</code> to suitable place near <code>old</code>.
     * <code>old</code> should be able to have a fight with new node and win - so new
     * node should become new neighbor of
     * <code>old</code>. If it is impossible method returns null.
     *
     * @param old
     * @param newVal
     * @return null if no suitable place to add
     */
    Node tryToAdd(Node old, int newVal, int soldierPotentialWinner) {
        if (old == null) throw new IllegalArgumentException();
        Node r = null;
        if (newVal > old.val) {
            if (old.next != null) {
                if (newVal < old.next.val) {
                    r = old.next = new Node(newVal, old.next, old);
                    r.next.prev = r;
                } else if (old == head && tail.val < newVal) {
                    r = tail = new Node(newVal, null, tail);
                    r.prev.next = r;
                } else {
                    //на случай если old.value бьёт newVal, но между ними уже стоят другие бойцы.
                    //проверяем, может ли old.val пробиться
                    Node prevNode = old;
                    Node node = prevNode.next;
                    boolean canAdd = true;
                    while (node != null && node.val < newVal) {
                        if (FightClub.Win[old.val][node.val] == 0 || node.val == soldierPotentialWinner) {
                            canAdd = false;
                            break;
                        }
                        prevNode = node;
                        node = node.next;
                    }
                    if (canAdd) {
                        r = prevNode.next = new Node(newVal, prevNode.next, prevNode);
                        if (prevNode == tail) tail = r;
                    }
                }
            } else {
                r = tail = old.next = new Node(newVal, null, old);
            }
        } else if (newVal < old.val) {
            if (old.prev != null) {
                if (old.prev.val < newVal) {
                    r = old.prev = new Node(newVal, old, old.prev);
                    r.prev.next = r;
                } else if (old == tail && head.val > newVal) {
                    r = head = new Node(newVal, head, null);
                    r.next.prev = r;
                } else {
                    //на случай если old.value бьёт newVal, но между ними уже стоят другие бойцы.
                    //проверяем, может ли old.val пробиться
                    Node prevNode = old;
                    Node node = prevNode.next;
                    boolean canAdd = true;
                    while (node != null && node.val < newVal) {
                        if (FightClub.Win[old.val][node.val] == 0 || node.val == soldierPotentialWinner) {
                            canAdd = false;
                            break;
                        }
                        prevNode = node;
                        node = node.next;
                    }
                    if(canAdd) {
                        node = head;
                        prevNode = null;
                        while (node != null && node.val < newVal) {
                            if (FightClub.Win[old.val][node.val] == 0 || node.val == soldierPotentialWinner) {
                                canAdd = false;
                                break;
                            }
                            prevNode = node;
                            node = node.next;
                        }
                    }
                    if (canAdd) {
                        r = new Node(newVal, prevNode.next, prevNode);
                        if (prevNode == null) {
                            r.next = head;
                            head = r;
                        }else{
                            prevNode.next = r;
                        }
                        if (prevNode == tail) tail = r;
                    }
                }
            } else {
                r = head = old.prev = new Node(newVal, old, null);
            }
        }
        if (r != null) size++;
        return r;
    }

    public int size() {
        return size;
    }


    public int[] toArray() {
        int[] a = new int[size];
        Node n = head;
        int i = 0;
        while (n != null) {
            a[i++] = n.val;
            n = n.next;
        }
        return a;
    }
}
