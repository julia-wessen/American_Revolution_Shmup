class Asteroid extends Object
{
  //this class is for asteroids that collide with bullets or the ship and 
  //are enemies in this game you want to shoot and avoid.

  //constructor
  Asteroid(PImage astr, PVector positio, int w, int h, float direction )
  {
    super(astr, positio, w, h);
    super.shiprotateamount = direction;
  }

  //display method displays asteroids
  void display()
  {
    super.display();
  }

  //update position
  void update()
  {
    PVector vec = PVector.fromAngle(radians(super.shiprotateamount));
    position.add(vec);
  }

  //contain within boundaries
  void contian()
  {
    super.contain();
  }

  //collision check for asteroid vs. bullet
  boolean collisioncheckbullet(float x, float y)
  {
    float hypot = sqrt(sq(super.position.x-x) + sq(super.position.y-y));
    if (hypot <= 15 +30) //(radius of asteroid + bullet)
    {
      return true;
    } else
    {
      return false;
    }
  }

  //collision check with asteroid vs ship
  boolean collisioncheckship(float x, float y)
  {
    float hypot = sqrt(sq(super.position.x-x) + sq(super.position.y-y));
    if (hypot <= 40+30) //(radius of asteroid + ship)
    {
      return true;
    } else
    {
      return false;
    }
  }
  
  //return image
  PImage returnimag()
  {
   return super.imag; 
  }
  
  //return position
  PVector returnposition()
  {
   return super.position; 
  }
  
  //pass in a new rotate value
  void newangle(float angle)
  {
    super.shiprotateamount = degrees(angle);
  }
}