package edu.sv.dynamic;

public class Grasshopper {
    public static void main(String[] args) {
        int [] steps = {1, 2};
        // start from 1;
        try {
            int waysCnt = countWaysTo(5, steps);
            System.out.println("waysCnt = " + waysCnt);

        }catch(Exception e){
            System.out.println("ct2");
        }finally {
            System.out.println("fin2");
        }

    }

    private static int countWaysTo(int i, int [] steps) {
        try {
            if (steps.length == 2) {

            }
            System.out.println("try");
            throw new Exception("sdfvsdfg");

        }catch(Exception e){
            System.out.println("catch");
            System.exit(-2);
//            throw new RuntimeException(e);
        } finally {
            System.out.println("finally");
        }
        return -1;
    }
}
