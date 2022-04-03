import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Gun {

    static int maxX;
    static int maxY;
    static int maxBeam;

    static int myX;
    static int myY;

    static int targetX;
    static int targetY;

    static List<Integer> resX;
    static List<Integer> resY;

    static Map<Integer, List<Integer>> map;

    static public int solution(int[] dimsRoom, int[] myLoc, int[] target, int maxBeamLength) {
        maxX = dimsRoom[0];
        maxY = dimsRoom[1];
        maxBeam = maxBeamLength;

        myX = myLoc[0];
        myY = myLoc[1];

        targetX = target[0];
        targetY = target[1];

        resX = new ArrayList<>();
        resY = new ArrayList<>();

        map = new TreeMap<>();

        // Calculating stuff once and storing it, so it doesn't have to be done multiple times.
        //      To see what exactly I'm doing, see InformationOneDirection class.
        InformationOneDirection infoX = new InformationOneDirection(myX, targetX, maxX, maxBeam);
        InformationOneDirection infoY = new InformationOneDirection(myY, targetY, maxY, maxBeam);

        // Covering edge-case when me and the target happen to be on one horizontal of vertical line.
        if (myX == targetX) {
            resX.add(0);
            resY.add(1);

            map.put(0, new ArrayList<>());
            map.get(0).add(1);
        }
        if (myY == targetY) {
            resX.add(1);
            resY.add(0);

            map.put(1, new ArrayList<>());
            map.get(1).add(0);
        }

        // Finding all of the vectors that end up in target before hitting myself.
        for (int indexX = 0; indexX < infoX.potentialDeltas.size(); indexX++) {
            for (int indexY = 0; indexY < infoY.potentialDeltas.size(); indexY++) {
                // TODO
                //      preform test to determine if deltaX-deltaY pair is a multiple of a deltaX-deltaY pair
                //      that's already been found.
                //          (use greatest common divider)
                //          Should increase performance, just can't be bothered implementing it atm.

                if (infoX.stepsTillMe.get(indexX).size() > 0 && infoY.stepsTillMe.get(indexY).size() > 0) {

                    int commonStepOnMe = findFirstCommon(infoX.stepsTillMe.get(indexX), infoY.stepsTillMe.get(indexY));
                    int commonStepOnTarget = findFirstCommon(infoX.stepsTillTarget.get(indexX), infoY.stepsTillTarget.get(indexY));

                    if (commonStepOnMe == -1 && commonStepOnTarget != -1) {
                        finalChecksBeforeAddingXY(infoX.potentialDeltas.get(indexX), infoY.potentialDeltas.get(indexY), commonStepOnTarget);
                    }

                    else if (commonStepOnMe != -1 && commonStepOnTarget != -1) {

                        if (commonStepOnMe > commonStepOnTarget) {
                            finalChecksBeforeAddingXY(infoX.potentialDeltas.get(indexX), infoY.potentialDeltas.get(indexY), commonStepOnTarget);
                        }
                    }
                }
                else {
                    // No need worrying about the bullet hitting me.

                    int common = findFirstCommon(infoX.stepsTillTarget.get(indexX), infoY.stepsTillTarget.get(indexY));

                    if (common != -1) {
                        finalChecksBeforeAddingXY(infoX.potentialDeltas.get(indexX), infoY.potentialDeltas.get(indexY), common);

                    }
                }
            }
        }

        //System.out.println(infoX.potentialDeltas);
        //System.out.println(infoY.potentialDeltas);

        //System.out.println();

        //System.out.println("x: " + resX);
        //System.out.println("y: " + resY);

        System.out.println("Map structure om makkelijk duplicates er uit te halen, (key is x, value is list van y's): \n\t\t" + map);

        // Final count to determine the amount of vectors with a unique direction.
        int som = 0;
        for (List<Integer> l : map.values()) {
            som += l.size();
        }

        return som;
        //return resX.size();
    }

    // Receiving two sorted lists, finds the smallest element that's present in both lists.
    //      Returns -1 if no such element is present.
    private static int findFirstCommon(List<Integer> one, List<Integer> two) {
        int iOne = 0;
        int iTwo = 0;

        while (iOne < one.size() && iTwo < two.size() && one.get(iOne) != two.get(iTwo)) {
            if (one.get(iOne) > two.get(iTwo)) {
                iTwo++;
            }
            else {
                iOne++;
            }
        }

        if (iOne < one.size() && iTwo < two.size()) {
            return one.get(iOne);
        }
        else {
            return -1;
        }
    }

    // Calculates greatest common divider of two integers.
    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

    // Before a vector can be added to map, two things need to be done / checked.
    //      First, check if the distance traveled is smaller than the max length of the beam via pythagoras.
    //      Second, to avoid vectors that are multiples of each other from being counted double,
    //          dividing each component with gcd(x, y).
    //          (-x, -y) and (x, y) can occur together as these represent two different directions to aim.
    public static void finalChecksBeforeAddingXY(int x, int y, int multiplier) {

        if (1.0 * multiplier * Math.sqrt(x * x + y * y) <= maxBeam) {
            int gcd = gcd(Math.abs(x), Math.abs(y));
            x /= gcd;
            y /= gcd;
            resX.add(x);
            resY.add(y);

            if (map.containsKey(x)) {
                if (!map.get(x).contains(y)) {
                    map.get(x).add(y);
                }
            }
            else {
                map.put(x, new ArrayList<>());
                map.get(x).add(y);
            }
        }
    }
}

/*
 There's one object per axis. One for the x-axis and one for the y-axis.
      These objects being created avoids a huge amount of double work from happening.
      For one axis, all the possible step-sizes are tested. Tested in what way?
            Let's say you start with a step-size of delta and you keep on doing steps of delta until
            you traverse the max distance of the beam.
            Along the way you keep note at what steps you end up having one corresponding coordinate with yourself of the target.
                --> remember, we are only doing the for one axis. So only one coordinate needs to be right.
            We do this for all stepsizes ranging from -maxLengthBeam till maxLengthBeam.

       The results of this operation can be found in the parameters.
            potentialDeltas contains a list with all of the deltas/stepsizes that end up on the "target".
            stepsTillTarget is a List of lists. The list at index i lists all of the steps
                (adding delta to the projectile is 1 step) at which the projectile is on the same spot as the target
                according to one axis. You can find the stepsize that was used at potentialDeltas[i].
            stepssTillMe follows the same principle as stepsTillTarget but now for your own location.

        nextSlow is a way of calculating where the target ends up after bouncing on the wall.

*/
class InformationOneDirection {
    public List<Integer> potentialDeltas = new ArrayList<>();
    public List<List<Integer>> stepsTillTarget = new ArrayList<>();
    public List<List<Integer>> stepsTillMe = new ArrayList<>();

    public InformationOneDirection(int me, int target, int maxSize, int maxBeam) {
        for (int stepSize = -maxBeam; stepSize <= maxBeam; stepSize++) {
            if (stepSize != 0) {
                List<Integer> targetSteps = new ArrayList<>();
                List<Integer> meSteps = new ArrayList<>();

                int nSteps = maxBeam/Math.abs(stepSize);
                int current = me;
                int stepWithDirection = stepSize;

                for (int i = 1; i <= nSteps; i++) {
                    //Pair<Integer, Integer> nextAndNextStep = next(current, stepWithDirection, maxSize);
                    Pair<Integer, Integer> nextAndNextStep = nextSlow(current, stepWithDirection, maxSize);

                    /*if (stepSize == 1) {
                        System.out.print("\t");
                        System.out.println(nextAndNextStep);
                    }*/
                    if (nextAndNextStep.getKey().equals(target)) {
                        targetSteps.add(i);
                    }
                    if (nextAndNextStep.getKey().equals(me)) {
                        meSteps.add(i);
                    }
                    current = nextAndNextStep.getKey();
                    stepWithDirection = nextAndNextStep.getValue();
                }

                if (targetSteps.size()>0) {
                    potentialDeltas.add(stepSize);
                    stepsTillTarget.add(targetSteps);
                    stepsTillMe.add(meSteps);
                }

                /*if (stepSize == 1) {
                    System.out.println();
                }*/
            }
        }
    }

    public Pair<Integer, Integer> nextSlow(int current, int step, int max) {
        current += step;
        while (current < 0 || current > max) {
            if (current < 0) {
                current = -current;
            }
            else {
                current = max - (current - max);
            }
            step *= -1;
        }
        return new Pair<Integer, Integer> (current, step);
    }
}
