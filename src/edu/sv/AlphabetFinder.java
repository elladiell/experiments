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
        for (int j = 0; alphabetIdx < ALPHABET.length() && j < LONG_STRING.length(); j++) {
            if (ALPHABET.charAt(alphabetIdx) == LONG_STRING.charAt(j)) {
                ++alphabetIdx;
            }
        }
        return alphabetIdx == ALPHABET.length();
    }

    public static RangeHolder shortestAlphabetSequenceLength(String s) {
        List<RangeHolder> ranges = new ArrayList<>();

        for (int j = 0; j < s.length(); j++) {
            if (ALPHABET.charAt(0) == s.charAt(j)) {
                boolean createNewList = true;
                for (RangeHolder indicesHolder : ranges) {
                    if (indicesHolder.getCount() <= 1) {
                        createNewList = false;
                        break;
                    }
                }
                if (createNewList) {
                    ranges.add(new RangeHolder());
                }
            }
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
            }
        }
        RangeHolder minRange = null;
        if (!ranges.isEmpty()) {
            if (ranges.get(0).getCount() == ALPHABET.length()) {
                minRange = ranges.get(0);
            }
        }
        return minRange;
    }
}

class RangeHolder {
    private int start;
    private int end;
    private int count;

    public RangeHolder() {
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



