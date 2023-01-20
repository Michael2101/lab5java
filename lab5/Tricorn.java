package lab5;



import lab4.*;

import java.awt.geom.Rectangle2D;



public class Tricorn extends FractalGenerator {

    private static final int MAX_ITERATIONS = 2000;


    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.height = 4;
        range.width = 4;
    }



    
    public int numIterations(double x, double y) {
        double currentIY = 0;
        double currentX = 0;
        int iterationsCount = 0;
        while (iterationsCount < MAX_ITERATIONS) {
            iterationsCount++;
            double newX = currentX * currentX - currentIY * currentIY + x;
            double newIY = -(2 * currentX * currentIY) + y;
            currentX = newX;
            currentIY = newIY;
            if (currentX * currentX + currentIY * currentIY > 4)
                break;
        }
        if (iterationsCount == MAX_ITERATIONS) {
            return -1;
        } else {
            return iterationsCount;
        }
    }

    
    public String toString() {
        return "Tricorn";
    }
}
