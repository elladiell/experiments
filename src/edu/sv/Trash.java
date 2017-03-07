package edu.sv;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Trash {
    class E{}
    enum D{w, e;
    };

    public static void main(String[] args) {
        D d = D.e;
        E ee ;
        String s3 = new String("lis");
        String s4 = s3.intern();
        String s1 = "lis";
        String s2 = "lis";
        System.out.println(s2.toString());
//        StringBuilder sb;
//        sb.append().reverse().

        System.out.println("s2 == s1 = " + (s2 == s1));
        System.out.println("s3 == s1 = " + (s3 == s1));
        System.out.println("s4 == s1 = " + (s4 == s1));

        int[][] a = {{1,2}, {3,4}};
        int[] b = a[1];

        Object o1 = a;
        int[][] a2 = (int[][]) o1;
//        int[] b2 = (int[]) o1;
        System.out.println(b[1]);


	// write your code here
        PriorityQueue pq = new PriorityQueue();

        byte s = 5;
        s &= 2;



        Comparator<String> comparator = new StringLengthComparator();
        PriorityQueue<String> queue =
                new PriorityQueue<String>(10, comparator);
        queue.add("short");
        queue.add("very long indeed");
        queue.add("medium");
        while (queue.size() != 0)
        {
            System.out.println(queue.poll());
        }
    }
}


class StringLengthComparator implements Comparator<String>
{
    @Override
    public int compare(String x, String y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.length() < y.length())
        {
            return 1;
        }
        if (x.length() > y.length())
        {
            return -1;
        }
        return 0;
    }
}

