import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;
import static java.util.Arrays.fill;

public class Main {

    public static void main(String[] args) {
        //System.out.println(Cake.solution("abccbaabccba"));
        //System.out.println(Cake.solution("abcabcabcabc"));

        Gun gun = new Gun();

        int[] dimsRoom1 = new int[]{3,2};
        int[] myLoc1 = new int[]{1,1};
        int[] target1 = new int[]{2,1};

        int[] dimsRoom2 = new int[]{300,275};
        int[] myLoc2 = new int[]{150,150};
        int[] target2 = new int[]{185,100};

        System.out.println("Mijn antwoord voorlopig:\t" + gun.solution(dimsRoom1, myLoc1, target1, 4));
        System.out.println("Juiste antwoord:\t\t\t" + 7);

        System.out.println();

        System.out.println("Mijn antwoord voorlopig:\t" + gun.solution(dimsRoom2, myLoc2, target2, 500));
        System.out.println("Juiste antwoord:\t\t\t" + 9);
    }
}

