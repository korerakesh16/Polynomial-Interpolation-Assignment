import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Fraction {
    BigInteger num, den;

    public Fraction(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ZERO)) throw new ArithmeticException("Denominator cannot be zero");
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
            System.out.println("Usage: java -cp .;json-20240303.jar PolynomialConstant <inputfile.json>");
            return;
        }

        String fileName = args[0];
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        JSONObject json = new JSONObject(content);

        JSONObject keysObj = json.getJSONObject("keys");
        int k = keysObj.getInt("k");

        // Collect the roots in insertion order (as per JSON)
        List<BigInteger> xVals = new ArrayList<>();
        List<BigInteger> yVals = new ArrayList<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            if (xVals.size() == k) break;  // Only take first k roots

            JSONObject rootObj = json.getJSONObject(key);
            int base = Integer.parseInt(rootObj.getString("base"));
            String val = rootObj.getString("value");

            BigInteger x = new BigInteger(key);  // Use the key as x
            BigInteger y = new BigInteger(val, base);

            xVals.add(x);
            yVals.add(y);
        }

        // Compute constant term using Lagrange interpolation
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

        if (constant.den.equals(BigInteger.ONE)) {
            System.out.println("Constant term of polynomial = " + constant.num);
        } else {
            System.out.println("Constant term of polynomial = " + constant.num + "/" + constant.den);
        }
    }
}
