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

    public static void main(String[] args) {
        // Soldier #row wins soldier #column if Win[#row][#column] == 1
        int[][] win = {
            {0, 1, 0, 1, 0},  //0
            {0, 0, 0, 0, 0},  //1
            {1, 1, 0, 1, 1},  //2
            {0, 1, 0, 0, 1},  //3
            {1, 1, 0, 0, 0},  //4

//            {-1, 0, 1,},  //0
//            {1, -1, 0,},  //1
//            {0, 1, -1,},  //2
//
//                {-1, 1, 0, 0,},  //0
//                {0, -1, 1, 0,},  //1
//                {1, 0, -1, 1,},  //2
//                {1, 1, 0, -1,},  //3
        };
        List<Integer> winners = getPotentialWinners(win);
        System.out.println("Potential winners:");
        for (int soldier : winners) {
            System.out.print(soldier + ", ");
        }
        System.out.println();
    }

    public static List<Integer> getPotentialWinners(int [][] win) {
        List<Integer> soldiers = new ArrayList<>();
        for (int i = 0; i < win.length; i++) {
            if (checkSoldierCouldWinDfs(i, win)) soldiers.add(i);
        }
        return soldiers;
    }

    /**
     * Determin if soldier <code>k</code> could win.
     * @param k
     * @return
     */
    private static boolean checkSoldierCouldWinDfs(int k, int [][] win) {
        CyclicSequence seq = new CyclicSequence();
        System.out.println("For k = " + k);
        return dfs(k, seq, win);
    }

    private static boolean dfs(int k, CyclicSequence seq, int [][] win) {
        CyclicSequence.Node addedNode = seq.tryToAdd(k);
        if (addedNode != null) {
            System.out.println("k = " + k);
            System.out.println(seq);
            Set<Integer> alreadyInSeq = seq.getElementsSet();
            for (int i = 0; i < win.length; i++) {
                if (win[k][i] == 1) {
                    if (!alreadyInSeq.contains(i)) {
                        CyclicSequence cyclicSequenceCopy = seq.copyWithInsertionCursor();
                        if (dfs(i, cyclicSequenceCopy, win)) return true;
                    }
                }
            }
        }
        return seq.size() == win.length;
    }
}

class CyclicSequence {
    private int size;
    private Node currentWinnerCursor;

    public Set<Integer> getElementsSet() {
        return Collections.unmodifiableSet(elementsSet);
    }


    class Node {
        int val;
        Node next;//to the right in cycle
        Node prev; //th the left in cycle

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

    private Set<Integer> elementsSet = new HashSet<>();

    /**
     * Head is always min.  Tail - max.
     * Adds <code>newVal</code> to suitable place near <code>old</code>.
     * <code>currentWinnerCursor</code> should be able to have a fight with new node and win - so new
     * node should become new neighbor of
     * <code>currentWinnerCursor</code>. If it is impossible method returns null.
     *
     * @param newVal
     * @return null if no suitable place to addToEnd
     */
    Node tryToAdd(int newVal) {
        Node r = null;
        if (currentWinnerCursor == null) {
            r = this.head = tail = this.currentWinnerCursor = new Node(newVal);
        } else if (newVal > currentWinnerCursor.val) {
            if (currentWinnerCursor.next != null) {
                if (newVal < currentWinnerCursor.next.val) {
                    r = currentWinnerCursor.next = new Node(newVal, currentWinnerCursor.next, currentWinnerCursor);
                    r.next.prev = r;
                } else if (currentWinnerCursor == head && tail.val < newVal) {
                    r = tail = new Node(newVal, null, tail);
                    r.prev.next = r;
                }
            } else {
                r = tail = currentWinnerCursor.next = new Node(newVal, null, currentWinnerCursor);
            }
        } else if (newVal < currentWinnerCursor.val) {
            if (currentWinnerCursor.prev != null) {
                if (currentWinnerCursor.prev.val < newVal) {
                    r = currentWinnerCursor.prev = new Node(newVal, currentWinnerCursor, currentWinnerCursor.prev);
                    r.prev.next = r;
                } else if (currentWinnerCursor == tail && head.val > newVal) {
                    r = head = new Node(newVal, head, null);
                    r.next.prev = r;
                }
            } else {
                r = head = currentWinnerCursor.prev = new Node(newVal, currentWinnerCursor, null);
            }
        }
        if (r != null) {
            size++;
            this.currentWinnerCursor = r;
            elementsSet.add(newVal);
        }
        return r;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }


    public CyclicSequence copyWithInsertionCursor() {
        CyclicSequence sq = new CyclicSequence();
        if (head != null) {
            Node n = head;
            while (true) {
                Node added = sq.addToEnd(n.val);
                if (currentWinnerCursor != null && added.val == currentWinnerCursor.val) sq.currentWinnerCursor = added;
                if (n == tail) break;
                n = n.next;
            }
        }
        return sq;
    }

    private Node addToEnd(int val) {
        ++size;
        if (head == null) {
            head = tail = new Node(val);
        } else {
            tail.next = new Node(val, null, tail);
            tail = tail.next;
        }
        elementsSet.add(val);
        return tail;
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
