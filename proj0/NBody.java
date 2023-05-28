
public class NBody {

    //read the radius of the universe from the text file.
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int no = in.readInt();
        double radius = in.readDouble();

        return radius;
    }

    //read the list of planets in the universe from the text file.
    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int no = in.readInt();
        double radius = in.readDouble();

        // a list containing all planets in the universe.
        Planet[] planetList = new Planet[no];

        int count = 0;

        while(count < no) {
            double xPos = in.readDouble();
            double yPos = in.readDouble(); 
            double xVel = in.readDouble();
            double yVel = in.readDouble(); 
            double m = in.readDouble();
            String imgName = in.readString();
            imgName = "images/" + imgName;
            Planet planet = new Planet(xPos, yPos, xVel, yVel, m, imgName);
            planetList[count] = planet;
            count += 1; 
        }
        
        return planetList;
    }

    public static void main(String[] args) {

        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        //draw the background.
        String imageToDraw = "images/starfield.jpg";
        StdDraw.setScale(-1 * radius, radius);
        StdDraw.clear();
        StdDraw.picture(0,0, imageToDraw);
        StdDraw.show();

        //draw the planets.
        for (int i = 0; i < planets.length; i += 1) {
            Planet planet = planets[i];
            planet.draw();
        }

        //create the animation.
        StdDraw.enableDoubleBuffering();
        double time = 0;
        double[] xForces = new double[planets.length];
        double[] yForces = new double[planets.length];

        while(time < T) {

            StdDraw.clear();
            StdDraw.picture(0,0, imageToDraw);

            for (int i = 0; i < planets.length; i += 1) {
                Planet planet = planets[i];
                double xForce = planet.calcNetForceExertedByX(planets);
                double yForce = planet.calcNetForceExertedByY(planets);
                xForces[i] = xForce;
                yForces[i] = yForce;
            }

            for (int i = 0; i < planets.length; i += 1) {
                planets[i].update(dt, xForces[i], yForces[i]);
                planets[i].draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            time += dt;

        }

        //print the final state of the universe.
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }

    }
}

