class Object
{
  //A parent class whose children are Bullet, Asteroid, and Ship
  
  //variables
  PImage imag;
  int objwid;
  int objhei;
  float shiprotateamount; 
  float smoothing;
  float maxspeed;
  PVector position, velocity, acceleration,direction;

  //constructor
  Object(PImage img, PVector pos, int w, int h)
  {
    imag = img;
    position = pos;
    objwid = w;
    objhei = h;
    shiprotateamount = 0;
    smoothing = 4;
    velocity = new PVector(0,0);
    acceleration = new PVector(0,0);
    maxspeed = 5;
    direction = new PVector(0,0);
  }
  
  //keeps objects within bounds of the window
  void contain(){
    if(position.x >= width)
    {
      position.x = 1;
    }
    if(position.y >= height)
    {
      position.y = 1;
    }
    if(position.y <= 0)
    {
     position.y = height-1; 
    }
    if(position.x <= 0)
    {
      position.x = width-1;
    }
  }
  
  //updates the position of the object
  void update()
  {
    direction = PVector.fromAngle(radians(shiprotateamount*smoothing));
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    position.add(velocity);
    acceleration.mult(0);
  }
  
  //friction resistance for a cool drifting effect
  void friction()
  {
    PVector friction = velocity.copy();
    friction.mult(-1);
    friction.normalize();
    friction.mult(0.1);
    acceleration.add(friction);
  }

  //displays an object
  void display()
  {
    pushMatrix();
    translate(position.x,position.y);
    rotate(radians(shiprotateamount*smoothing));
    image(imag, 0, 0);
    popMatrix();
  }
}