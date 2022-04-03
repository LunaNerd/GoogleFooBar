import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;
import static java.util.Arrays.fill;

public class Cake {
    public static int solution(String x) {
        int N = x.length();

        //List of primes found with Sieve of Eratosthenes
        List<Integer> primes = sieve(N);

        //Prime-decomposition, number at index indicates the power to which the raise prime at same index
        List<Integer> decomposition = decomposition(N, primes);

        //System.out.println(decomposition);
        //System.out.println(primes);

        //Case covered where there's no repetition
        int res = 1;

        int end = x.length();

        //Checks if string (ever decreasing in size), is dividable in n parts.
        //  n is a prime in the prime-decomposition of the size of the string
        for (int i = 0; i < decomposition.size(); i++) {
            boolean partsAreEqual = true;

            while (decomposition.get(i) != 0 && partsAreEqual) {
                int sizePart = (end) / primes.get(i);
                int startFirstPart = 0;
                int startSecondPart = sizePart;

                partsAreEqual = true;

                while (startSecondPart < end && partsAreEqual) {
                    String firstPart = x.substring(startFirstPart, startFirstPart + sizePart);
                    String secondPart = x.substring(startSecondPart, startSecondPart + sizePart);

                    partsAreEqual &= firstPart.equals(secondPart);

                    startFirstPart = startSecondPart;
                    startSecondPart += sizePart;
                }

                if (partsAreEqual) {
                    end = sizePart;
                    res *= primes.get(i);
                }
                decomposition.set(i, decomposition.get(i)-1);
            }
        }
        return res;
    }

    private static List<Integer> sieve(int N) {

        List<Integer> res = new ArrayList();
        int n = (int) sqrt(N);

        boolean[] numberPool = new boolean[N+1];
        fill(numberPool, 2, N+1, true);

        for (int i = 2; i <= n; i++) {
            if (numberPool[i]) {
                for (int j = i*i; j<=N; j+=i) {
                    numberPool[j] = false;
                }
            }
        }

        for (int i = 0; i<numberPool.length; i++) {
            //System.out.println(i + ": " + numberPool[i]);
            if (numberPool[i]) {
                res.add(i);
            }
        }

        return res;
    }

    private static List<Integer> decomposition(int N, List<Integer> primes) {
        List<Integer> res = new ArrayList();
        res.add(0);

        int primeI = 0;

        while (N != 1) {
            if (N % primes.get(primeI) == 0) {
                N /= primes.get(primeI);
                res.set(primeI, res.get(primeI) + 1);
            }
            else {
                primeI++;
                res.add(0);
            }
        }
        return res;
    }
}
