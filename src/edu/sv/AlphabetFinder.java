package edu.sv;

import java.util.*;

public class AlphabetFinder {
    static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    static String LONG_STRING = "wdfvwsadmwaert55bwefxccdeddfg  abcegergeeg_________defghijklmnopqrstuvabcwx yzewerzkhgdefghijklmnopqrstuvwxyzhnkih";


    public static void main(String[] args) {
        System.out.println("ALPHABET = " + ALPHABET);
        System.out.println("LONG_STRING = " + LONG_STRING);
        boolean b = simpleTask(LONG_STRING);
        System.out.println("b = " + b);
        RangeHolder rh = shortestAlphabetSequenceLength(LONG_STRING);
        System.out.println("Range = " + rh);
        System.out.println("Seq = " + LONG_STRING.substring(rh.getStart(), rh.getEnd() + 1));
    }

    public static boolean simpleTask(String s) {
        int alphabetIdx = 0;
        for (int j = 0; alphabetIdx < ALPHABET.length() && j < s.length(); j++) {
            if (ALPHABET.charAt(alphabetIdx) == s.charAt(j)) {
                ++alphabetIdx;
            }
        }
        return alphabetIdx == ALPHABET.length();
    }

    public static RangeHolder shortestAlphabetSequenceLength(String s) {
        System.gc();
        Runtime r = Runtime.getRuntime();
        long mem0 = r.totalMemory() - r.freeMemory();
        List<RangeHolder> ranges = new ArrayList<>(3);
        boolean createNewList = true;
        for (int j = 0; j < s.length(); j++) {
            if (ALPHABET.charAt(0) == s.charAt(j) && createNewList) {
                    ranges.add(new RangeHolder());
            }
            createNewList = true;
            // older ranges start earlier
            for (int i = ranges.size() - 1; i >= 0; --i) {
                RangeHolder rangeHolder = ranges.get(i);
                if (i < ranges.size() - 1 && rangeHolder.getCount() <= ranges.get(i + 1).getCount()) {
                    if (rangeHolder.getCount() == ranges.get(i + 1).getCount()
                            && rangeHolder.getLength() <= ranges.get(i + 1).getLength()) {
                        // no need to spend memory for longer finished sequences
                        ranges.remove(i + 1);
                    } else {
                        // no need to spend memory for longer sequences that wait for same next symbol or one of previous ones
                        ranges.remove(i);
                    }
                    continue;
                }
                int alphabetIdx = rangeHolder.getCount();
                if (alphabetIdx < ALPHABET.length()) {
                    if (alphabetIdx <= 1 && ALPHABET.charAt(0) == s.charAt(j)) {
                        rangeHolder.setStart(j);
                    }
                    if (ALPHABET.charAt(alphabetIdx) == s.charAt(j)) {
                        rangeHolder.setCount(++alphabetIdx);
                        rangeHolder.setEnd(j);
                    }
                }
                if (rangeHolder.getCount() <= 1) {
                    createNewList = false;
                }
            }
        }
        RangeHolder minRange = null;
        if (!ranges.isEmpty()) {
            if (ranges.get(0).getCount() == ALPHABET.length()) {
                minRange = ranges.get(0);
            }
        }
        long mem1 = r.totalMemory() - r.freeMemory();
        System.out.println("Mem = " + (mem1 - mem0));
        return minRange;
    }


    static class Pos {
        int posA = -1;
        int end = -1;
    }

    public static RangeHolder shortestAEDenis(String s) {
        System.gc();
        Runtime r = Runtime.getRuntime();
        long mem0 = r.totalMemory() - r.freeMemory();
        int minLength = Integer.MAX_VALUE;
        int minStart = -1, minEnd = -1;
        Pos[] pos = new Pos[26]; // 0..25
        for (int i = 0; i < 26; i++) pos[i] = new Pos();
        // Time Complexity: O(n)   n = s.length()
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z') {
                int idx = c - 'a';
                if (c == 'a') {
                    pos[idx].posA = i;
                    pos[idx].end = i;
                } else {
                    if (pos[idx - 1].posA != -1) {
                        pos[idx].posA = pos[idx - 1].posA;
                        pos[idx].end = i;
                        if (c == 'z') {
                            int len = pos[idx].end - pos[idx].posA + 1;
                            if (len < minLength) {
                                minLength = len;
                                minStart = pos[idx].posA;
                                minEnd = pos[idx].end;
                            }
                        }
                    }
                }
            }
        }
        long mem1 = r.totalMemory() - r.freeMemory();
        System.out.println("Mem denis = " + (mem1 - mem0));
        if (minLength == Integer.MAX_VALUE)
            return null;
        else
            return new RangeHolder(minStart, minEnd);//"Min length = " + minLength + " " + minStart + ".." + minEnd;
    }
}

class RangeHolder {
    private int start;
    private int end;
    private int count;

    public RangeHolder() {
    }

    public RangeHolder(int start, int end) {

        this.start = start;
        this.end = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "RangeHolder{" +
                "start=" + start +
                ", end=" + end +
                ", count=" + count +
                ", length=" + getLength() +
                '}';
    }


    public int getLength() {
        return end - start + 1;
    }
}



