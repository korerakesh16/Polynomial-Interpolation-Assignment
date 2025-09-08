# Hashira Placements Assignment – Polynomial Constant Finder

This project is a solution for the **Hashira Placements Online Assignment**.  
It reads polynomial roots from JSON input, performs **Lagrange interpolation**, and calculates the **constant term** of the polynomial.

---

## 📂 Project Structure
Hashira/
│── PolynomialConstant.java # Main Java program
│── input1.json # Sample test case 1
│── input2.json # Sample test case 2
│── json-20240303.jar # JSON parsing library
│── README.md # Documentation

## 🚀 How to Run

### 1. Compile
javac -cp ".;json-20240303.jar" PolynomialConstant.java

### Run

Pass the input JSON file as an argument:
java -cp ".;json-20240303.jar" PolynomialConstant input1.json

### For second test case:
java -cp ".;json-20240303.jar" PolynomialConstant input2.json

### Input Format

### Example JSON:

{
  "keys": {
    "n": 4,
    "k": 3
  },
  "1": {
    "base": "10",
    "value": "4"
  },
  "2": {
    "base": "2",
    "value": "111"
  },
  "3": {
    "base": "10",
    "value": "12"
  },
  "6": {
    "base": "4",
    "value": "213"
  }
}


n = number of roots provided
k = minimum roots required (k = m+1 for degree m)
Each root has:
base: numeric base (2, 8, 10, 16, etc.)
value: root value in that base

### Tech Stack
Java 21
org.json library for JSON parsing
Lagrange interpolation for polynomial constant calculation
