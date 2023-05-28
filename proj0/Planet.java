import java.lang.Math;

public class Planet {
    public double xxPos;   //current x position
    public double yyPos;   //current y position
    public double xxVel;   //current velocity in x direction
    public double yyVel;   //current velocity in y direction
    public double mass;    //mass of the planet
    public String imgFileName; //The name of the file that corresponds to the image that depicts the planet

    private static final double G = 6.67e-11;

    public Planet (double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    //istantiate a copy of the planet provided
    public Planet (Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    //calculate the distance between this planet and another planet. 
    public double calcDistance(Planet aPlanet) {
        double x_distance = aPlanet.xxPos - xxPos;
        double y_distance = aPlanet.yyPos - yyPos;
        return Math.sqrt(x_distance * x_distance + y_distance * y_distance);
    }

    //calculate the force between this planet and another planet.
    public double calcForceExertedBy(Planet aPlanet) {
        double force = (G * mass * aPlanet.mass)/(calcDistance(aPlanet) * calcDistance(aPlanet));
        return force; 
    }

    //calcualte the force between this planet and another planet in x direction
    public double calcForceExertedByX(Planet aPlanet) {
        double force = calcForceExertedBy(aPlanet);
        double forceX = force * (aPlanet.xxPos - xxPos)/calcDistance(aPlanet);
        return forceX;
    }

     //calcualte the force between this planet and another planet in y direction
     public double calcForceExertedByY(Planet aPlanet) {
        double force = calcForceExertedBy(aPlanet);
        double forceY = force * (aPlanet.yyPos - yyPos)/calcDistance(aPlanet);
        return forceY;
    }

    //calcualte the net force between this planet and another planets in x direction
    public double calcNetForceExertedByX(Planet[] aPlanets) {
        double NetForceX = 0;

        for(int i = 0; i < aPlanets.length; i += 1) {
            if ( !this.equals(aPlanets[i]) ) {
                NetForceX += calcForceExertedByX(aPlanets[i]);
            }
        }

        return NetForceX;
    }

    //calcualte the net force between this planet and another planets in y direction
    public double calcNetForceExertedByY(Planet[] aPlanets) {
        double NetForceY = 0;

        for(int i = 0; i < aPlanets.length; i += 1) {
            if ( !this.equals(aPlanets[i]) ) {
                NetForceY += calcForceExertedByY(aPlanets[i]);
            }
        }

        return NetForceY;
    }

    //update the current position and velocity of the planet 
    public void update(double dt, double fX, double fY) {
        double ax = fX/mass;
        double ay = fY/mass;

        //update the velocity (x and y)
        xxVel += (ax * dt);
        yyVel += (ay * dt);

        //update the position (x and y)
        xxPos += (xxVel * dt);
        yyPos += (yyVel * dt);
    }

    //use the StdDraw API to draw the planet.
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}