package edu.sv.dynamic;

import java.util.*;

/**
 * Created by ampopov on 27.03.2017.
 */
public class FightClub2 {
    // Soldier #row wins soldier #column if Win[#row][#column] == 1
    static int[][] win = {
            {0, 1, 0, 1, 0},  //0
            {0, 0, 0, 0, 0},  //1
            {1, 1, 0, 1, 1},  //2
            {0, 1, 0, 0, 1},  //3
            {1, 1, 0, 0, 0},  //4

//            {0, 0, 1, 1},
//            {1, 0, 0, 0},
//            {0, 1, 0, 1},
//            {0, 1, 0, 0},

//            {-1, 0, 1,},  //0
//            {1, -1, 0,},  //1
//            {0, 1, -1,},  //2
//
//                {-1, 1, 0, 0,},  //0
//                {0, -1, 1, 0,},  //1
//                {1, 0, -1, 1,},  //2
//                {1, 1, 0, -1,},  //3
    };
    static int N = win.length;
    static boolean[][][] rangesAndWinners = new boolean[N][N][N];

    static Queue<RangeAndWinner> queue = new LinkedList<>();

    public static void main(String[] args) {
        Set<Integer> winners = getPotentialWinners();
        System.out.println("Potential winners:");
        for (int soldier : winners) {
            System.out.print(soldier + ", ");
        }
        System.out.println();
    }

    public static Set<Integer> getPotentialWinners() {
        init();
        fillRanges();

        Set<Integer> soldiers = new HashSet<>();
        for (int i = 0; i < N; i++) {
            int left = i;
            int right = (left + N - 1) % N;
            for (int j = 0; j < N; j++) {
                if (rangesAndWinners[left][right][j]) soldiers.add(j);
            }
        }
        return soldiers;
    }

    private static void fillRanges() {
        while (!queue.isEmpty()) {
            RangeAndWinner range = queue.poll();
            //check all right neighbor ranges:
            int rightNeighborLeftBound = (range.right + 1) % N;
            for (int rightNeighborRightBound = rightNeighborLeftBound;
                 rightNeighborRightBound != range.left; rightNeighborRightBound = (rightNeighborRightBound + 1) % N) {
                for (int j = 0; j < N; j++) {
                    if (rangesAndWinners[rightNeighborLeftBound][rightNeighborRightBound][j]) {
                        int winner = win[range.winner][j] != 0 ? range.winner : j;
                        if (!rangesAndWinners[range.left][rightNeighborRightBound][j]) {
                            rangesAndWinners[range.left][rightNeighborRightBound][winner] = true;
                            RangeAndWinner newRange = new RangeAndWinner(range.left, rightNeighborRightBound, winner);
                            queue.offer(newRange);
                        }
                    }
                }
            }
            //check all left neighbor ranges:
            int leftNeighborRightBound = ((range.left - 1) + N) % N;
            for (int leftNeighborLeftBound = leftNeighborRightBound;
                 leftNeighborLeftBound != range.right; leftNeighborLeftBound = ((leftNeighborLeftBound - 1) + N) % N) {
                for (int j = 0; j < N; j++) {
                    if (rangesAndWinners[leftNeighborLeftBound][leftNeighborRightBound][j]) {
                        int winner = win[range.winner][j] != 0 ? range.winner : j;
                        if (!rangesAndWinners[leftNeighborLeftBound][range.right][j]) {
                            rangesAndWinners[leftNeighborLeftBound][range.right][winner] = true;
                            RangeAndWinner newRange = new RangeAndWinner(leftNeighborLeftBound, range.right, winner);
                            queue.offer(newRange);
                        }
                    }
                }
            }
        }
    }

    private static void init() {
        for (int i = 0; i < N; i++) {
            rangesAndWinners[i][i][i] = true;
            queue.offer(new RangeAndWinner(i, i, i));
        }
    }

    static class RangeAndWinner {
        int left;
        int right;
        int winner;

        public RangeAndWinner(int left, int right, int winner) {
            this.left = left;
            this.right = right;
            this.winner = winner;
        }

        @Override
        public String toString() {
            return "RangeAndWinner{" +
                    "left=" + left +
                    ", right=" + right +
                    ", winner=" + winner +
                    '}';
        }
    }


}
