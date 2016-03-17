class Bullet extends Object
{  
  //bullets will only collide with Asteroids and will be shot based on the location
  //and direction of the ship class as a way to destory Asteroids. 
  
  //variables
  PVector vec;
  float timeval; //see ship class for timeval max
  
  //bullet constructor
  Bullet(PImage img, PVector pos, int w, int h, float rotateamount)
  {
    super(img, new PVector(pos.x,pos.y), w, h);
    timeval = 0;
    float rot = rotateamount;
    super.shiprotateamount = rot;
    vec = PVector.fromAngle(radians(super.shiprotateamount*super.smoothing));
    super.maxspeed = 7;
  }

  //bullet display method that displays bullets
  void display()
  {
    super.display();
  }
  
  //updating bullet position and time value till it expires
  void update()
  {
    super.velocity.add(vec);
    super.velocity.mult(2);
    super.update();
    timeval++;
  }
  
  //contains the objects within the boundary
  void contain()
  {
    super.contain();
  }
  
  //returns time value in order for the bullet to 'expire'
  //after a certain amount of time
  float timevalue()
  {
    return timeval;
  }
  
  //return the bullet's x 
  PVector returnposition()
  {
   return super.position; 
  }
  

}