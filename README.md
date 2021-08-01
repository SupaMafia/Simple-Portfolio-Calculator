# Simple-Portfolio-Calculator
A program that calculates optimum portfolio for either maximizing return or minimizing risk. 

You need an txt file as input, file name is [indices.txt].

Format for input is [rank indexA indexB].

You need to rank the indices from the newest date to the latest date with the newest data on the first line.

Blank lines after data need to be removed.

The [indices.txt] needs to be at the same folder as [SimplePortfolioCalculator.java] file.

OBS! Computer Resgion Format Setting is preferably to be [English (United States)] since under certain region setting computer would read "," instead of "." causing an InputMismatchException. 

To run the Java program through Command Prompt under windows in gernal: https://www.baeldung.com/java-lang-unsupportedclassversion. Compile code under JDK 8 to ensure compatibility

An [Indices.txt] is provided for testing and debuging purpose. 

Result interpretation: Maximum Sharpe-Ratio Portfolio suggests a ratio for highest excess return. Minimum Variance Portfolio suggests a ratio for lower risk. 
