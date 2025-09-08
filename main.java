import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Fraction class for rational arithmetic
class Fraction {
    BigInteger num;
    BigInteger den;

    public Fraction(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        // Normalize denominator
        if (den.compareTo(BigInteger.ZERO) < 0) {
            num = num.negate();
            den = den.negate();
        }
        BigInteger gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }

    public Fraction add(Fraction other) {
        BigInteger n = this.num.multiply(other.den).add(other.num.multiply(this.den));
        BigInteger d = this.den.multiply(other.den);
        return new Fraction(n, d);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.num.multiply(other.num), this.den.multiply(other.den));
    }
}

public class PolynomialConstant {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java PolynomialConstant <inputfile1.json> <inputfile2.json> ...");
            return;
        }

        for (String fileName : args) {
            System.out.println("Processing file: " + fileName);

            // Read JSON content
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONObject json = new JSONObject(content);

            JSONObject keys = json.getJSONObject("keys");
            int k = keys.getInt("k");

            List<BigInteger> xVals = new ArrayList<>();
            List<BigInteger> yVals = new ArrayList<>();

            // Collect numeric keys and sort
            List<Integer> sortedKeys = new ArrayList<>();
            for (String key : json.keySet()) {
                if (!key.equals("keys")) {
                    sortedKeys.add(Integer.parseInt(key));
                }
            }
            Collections.sort(sortedKeys);

            // Use first k points for interpolation
            for (int i = 0; i < k; i++) {
                int sk = sortedKeys.get(i);
                JSONObject obj = json.getJSONObject(String.valueOf(sk));
                int base = Integer.parseInt(obj.getString("base"));
                String val = obj.getString("value");

                BigInteger decimalValue = new BigInteger(val, base);
                xVals.add(BigInteger.valueOf(sk));
                yVals.add(decimalValue);
            }

            // Lagrange interpolation for constant term
            Fraction constant = new Fraction(BigInteger.ZERO, BigInteger.ONE);
            for (int i = 0; i < k; i++) {
                Fraction term = new Fraction(yVals.get(i), BigInteger.ONE);
                for (int j = 0; j < k; j++) {
                    if (i == j) continue;
                    BigInteger num = BigInteger.ZERO.subtract(xVals.get(j));
                    BigInteger den = xVals.get(i).subtract(xVals.get(j));
                    term = term.multiply(new Fraction(num, den));
                }
                constant = constant.add(term);
            }

            // Print result
            if (constant.den.equals(BigInteger.ONE)) {
                System.out.println("Constant term of polynomial = " + constant.num);
            } else {
                System.out.println("Constant term of polynomial = " + constant.num + "/" + constant.den);
            }

            System.out.println(); // empty line between files
        }
    }
}
