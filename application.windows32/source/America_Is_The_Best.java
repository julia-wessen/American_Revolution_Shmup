import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class America_Is_The_Best extends PApplet {








//IGME 202 with Erin Asteroids Project 2
//Julia Wessen 
//---------------------------------------------------------------

//setting up sound
Minim minim;
AudioPlayer player;
AudioPlayer player2;
AudioPlayer player3;

//variables
PImage shippic, asteroid, bullet, backgroundpic, intro, backgroundpic2, asteroidsm, heart;
int asteroidamount, lives, asteroidspresent;
float angle;
Asteroid[] asteroidbelt;
Bullet[] bulletblt;
Ship ship;
boolean forward, left, right, immune, start, spawnedstart;
int score, asteroidnumber;

public void setup()
{
  //buidling the scene
  
  imageMode(CENTER);

  //music
  minim = new Minim(this);
  player2 = minim.loadFile("magnet.mp3");
  player = minim.loadFile("throughtmyowneyes.mp3");
  player3 = minim.loadFile("lost.mp3");
  player.loop();

  //setting up images and objects
  shippic = loadImage("America.png");
  asteroid = loadImage("England.png");
  bullet = loadImage("Hamburger.png");
  backgroundpic = loadImage("AmericanRevolution.png");
  intro =loadImage("Intro.png");
  heart = loadImage("heart.png");
  backgroundpic2 = loadImage("TrueRevolution.png");
  asteroidsm = loadImage("tea.png");
  asteroidnumber = 5;
  lives = 3;
  ship = new Ship(shippic, new PVector(width/2, height/2), 60, 60, bullet);
  asteroidbelt = new Asteroid[0];
  forward = false;
  left = false;
  right = false;
  immune = false;
  score = 0;
  spawnedstart= true;
  start = true;
}

public void draw() 
{
  //while player is alive
  if (lives >= 0)
  {
    //methods to be called and run
    ship.contain();
    ship.update();
    ship.applyfriction();
    ship.bulletdissapear();
    runship();
    if (start == false)
    {
      spawnasteroids();
    }

    //display and working with sound
    if (score >= 1000 && start == false)
    {
      background(backgroundpic2);
      if (player.isPlaying())
      { 
        ship.bulletimagereset(heart);
        player.pause();
        player.rewind();
        player2.play();
      }
      if (player3.isPlaying())
      { 
        ship.bulletimagereset(heart);
        player3.pause();
        player3.rewind();
        player2.play();
      }
    } else if (start)
    {
      background(intro);
    } else if (score < 1000 && start == false)
    {
      background(backgroundpic);
      if (player2.isPlaying())
      {
        ship.bulletimagereset(bullet);
        player2.pause();
        player2.rewind();
        player.play();
      }
      if (player3.isPlaying())
      { 
        ship.bulletimagereset(bullet);
        player3.pause();
        player3.rewind();
        player.play();
      }
    }
    textSize(30);
    text("SCORE: " + score, width/2-60, 30);
    textSize(20);
    text("LIVES: " + lives, width/2-30, 60);
    for (int i = 0; i < asteroidbelt.length; i++)
    {
      if (asteroidbelt[i] != null)
      {
        asteroidspresent++;
      } else
      {
      }
    }
    text("Enemies: " + asteroidspresent, 0, 20);

    //loops that call certain things for each object in the array
    //bullets
    for (Bullet bul : ship.returnbullets())
    {
      if (bul != null)
      {
        bul.contain();
        bul.display();
        bul.update();
      }
    }

    //asteroids
    for (int a = 0; a < asteroidbelt.length; a++)
    {
      //if theres an asteroid inthe array
      if (asteroidbelt[a] != null)
      {
        asteroidbelt[a].update();
        asteroidbelt[a].contain();
        asteroidbelt[a].display();

        //if there is an asteroid-ship collision
        if (asteroidbelt[a].collisioncheckship(ship.returnposition().x, ship.returnposition().y) && immune == false) 
        {
          score = 0;
          lives--;
          immune = true;
          asteroidbelt = new Asteroid[asteroidspresent];
          for (int y = 0; y < asteroidbelt.length; y++)
          {
            asteroidbelt[y] = new Asteroid(asteroid, new PVector(random(width), random(height)), 80, 80, random(360));
            PVector direction = PVector.sub(ship.returnposition(), asteroidbelt[y].returnposition());
            asteroidbelt[y].newangle(atan2(direction.y, direction.x));
          }
        }

        //bullets colliding with asteroids that exist
        for (int i = 0; i < ship.returnbullets().length; i++)
        { 
          //if there is a bullet in the array
          if (ship.returnbullets()[i] != null && a < asteroidbelt.length)
          {
            if (asteroidbelt[a]!=null)
            {
              //if there is indeed a collision between asteroid and bullet
              if (asteroidbelt[a].collisioncheckbullet(ship.returnbullets()[i].returnposition().x, ship.returnbullets()[i].returnposition().y))
              {

                //adding to score and taking out a bullet and an asteroid from their arrays
                score = score + 40;
                ship.returnbullets()[i] = null;
                PVector astpos = asteroidbelt[a].position;
                if (asteroidbelt[a].returnimag() != asteroidsm)
                {
                  //making the asteroid array longer and adding one more asteroid
                  Asteroid[] newbelt = new Asteroid[asteroidbelt.length+1];
                  for (int x = 0; x < asteroidbelt.length; x ++)
                  {
                    newbelt[x] = asteroidbelt[x];
                  }
                  asteroidbelt = newbelt;
                  asteroidbelt[a] = null;
                  int count = 0;
                  for (int y = 0; y < asteroidbelt.length; y++)
                  {
                    if (asteroidbelt[y] == null && count <= 1) 
                    {
                      count++;
                      asteroidbelt[y] = new Asteroid(asteroidsm, new PVector(astpos.x, astpos.y), 50, 50, random(180));
                    }
                  }
                  break;
                } else
                {
                  asteroidbelt[a] = null;
                }
              }
            }
          }
        }
      }
    }
    asteroidspresent = 0;


    //controlling andsetting up immunity factor
    ship.display();
    if (immune) 
    { 
      ship.immunity();
    }
  }


  //game over
  else
  {
    //turning the music into something not fun
    if (player.isPlaying())
    {
      player.pause();
      player.rewind();
      player3.play();
    }
    if (player2.isPlaying())
    {
      player2.pause();
      player2.rewind();
      player3.play();
    }
    background(0);
    textSize(30);
    text("Game Over", width/2-80, height/2);
    textSize(20);
    text("Press 'y' to play again. Press 'n' to exit", width/2 - 150, height/2 + 30);
  }
}


//keyboard commands for the ship
public void keyPressed()
{
  if (key == 'w')
  {
    forward = true;
  }
  if (key == 'a')
  {
    left = true;
  }
  if (key == 'd')
  {
    right = true;
  }
  if (key == 'f')
  {
    ship.fire();
  }
  if (key == 'l')
  {
    start = false;
  }
  if (key == 'y' && lives < 0)
  {
    lives = 3;
    score = 0;
    asteroidamount = 6;
    asteroidbelt = new Asteroid[asteroidamount];
    for (int y = 0; y < asteroidbelt.length; y++)
    {
      asteroidbelt[y] = new Asteroid(asteroid, new PVector(random(width), random(height)), 50, 50, random(360));
      PVector direction = PVector.sub(ship.returnposition(), asteroidbelt[y].returnposition());
      asteroidbelt[y].newangle(atan2(direction.y, direction.x));
    }
  }
  if (key == 'n'&& lives < 0)
  {
    exit();
  }
}

//this method makes sure there is a smoooth transition between the
//keys being held down and the movement. Without it, there is a 
//delay between the first key press and the sequence of others when
//a key is held down, which affects movement. 
public void keyReleased()
{
  if (key == 'w')
  {
    forward = false;
  }
  if (key == 'a')
  {
    left = false;
  }
  if (key == 'd')
  {
    right = false;
  }
}

//this method makes the ship move depending on if a boolean is 
//active, where the boolean depends on a key being held down
//until it is released.
public void runship()
{
  if (forward)
  {
    ship.Move(1);
  }
  if (right)
  {
    ship.Move(3);
  }
  if (left)
  {
    ship.Move(2);
  }
}

//spawns asteroids when there are none
public void spawnasteroids()
{
  int a = 0;
  //if all spots inthe asteroid belt are null
  for (int i= 0; i < asteroidbelt.length; i++)
  {

    if (asteroidbelt[i] == null)
    {
      a++;
    }
  }

  //if they areall null, then spawn new ones
  if (a == asteroidbelt.length)
  {  
    immune = true;
    asteroidnumber++;
    asteroidbelt = new Asteroid[asteroidnumber];
    for (int i = 0; i < asteroidbelt.length; i++)
    {
      asteroidbelt[i] = new Asteroid(asteroid, new PVector(random(width), random(height)), 80, 80, random(360));
      PVector direction = PVector.sub(ship.returnposition(), asteroidbelt[i].returnposition());
      asteroidbelt[i].newangle(atan2(direction.y, direction.x));
    }
  } else
  {
  }
}
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
  public void display()
  {
    super.display();
  }

  //update position
  public void update()
  {
    PVector vec = PVector.fromAngle(radians(super.shiprotateamount));
    position.add(vec);
  }

  //contain within boundaries
  public void contian()
  {
    super.contain();
  }

  //collision check for asteroid vs. bullet
  public boolean collisioncheckbullet(float x, float y)
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
  public boolean collisioncheckship(float x, float y)
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
  public PImage returnimag()
  {
   return super.imag; 
  }
  
  //return position
  public PVector returnposition()
  {
   return super.position; 
  }
  
  //pass in a new rotate value
  public void newangle(float angle)
  {
    super.shiprotateamount = degrees(angle);
  }
}
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
  public void display()
  {
    super.display();
  }
  
  //updating bullet position and time value till it expires
  public void update()
  {
    super.velocity.add(vec);
    super.velocity.mult(2);
    super.update();
    timeval++;
  }
  
  //contains the objects within the boundary
  public void contain()
  {
    super.contain();
  }
  
  //returns time value in order for the bullet to 'expire'
  //after a certain amount of time
  public float timevalue()
  {
    return timeval;
  }
  
  //return the bullet's x 
  public PVector returnposition()
  {
   return super.position; 
  }
  

}
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
  public void contain(){
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
  public void update()
  {
    direction = PVector.fromAngle(radians(shiprotateamount*smoothing));
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    position.add(velocity);
    acceleration.mult(0);
  }
  
  //friction resistance for a cool drifting effect
  public void friction()
  {
    PVector friction = velocity.copy();
    friction.mult(-1);
    friction.normalize();
    friction.mult(0.1f);
    acceleration.add(friction);
  }

  //displays an object
  public void display()
  {
    pushMatrix();
    translate(position.x,position.y);
    rotate(radians(shiprotateamount*smoothing));
    image(imag, 0, 0);
    popMatrix();
  }
}
class Ship extends Object
{
  //Ship class fires bullets and can collide with asteroids for a game 
  //over. 

  //Bullet variables. You can only have 6 bullets at a time on the screen
  //and will be on the screen for a short amount of time until they expire
  //unless this is changed. 
  PImage bulletimage;
  int bulletsavailable, bullettimeMAX;
  Bullet[] bulletbelt;
  int i;

  //constructor
  Ship(PImage img, PVector pos, int w, int h, PImage bullt )
  {
    super(img, pos, w, h);
    bulletimage = bullt;
    bulletbelt = new Bullet[3];
    bullettimeMAX = 60;
  }

  //display method
  public void display() 
  {
    super.display();
  }

  //move method based on a int parameter
  public void Move(int dinum)
  {
    switch(dinum)
    {
      //move forward
    case 1: 
      super.velocity.add(super.direction);
      break;

      //rotate left
    case 2: 
      super.shiprotateamount--;
      break;

      //rotate right
    case 3: 
      super.shiprotateamount++;
      break;
    }
  }

  //apply friction to the ship
  public void applyfriction()
  {
    super.friction();
  }

  //fire method that uses the bullets the ship has
  public void fire()
  {
    for (int i = 0; i < bulletbelt.length; i++)
    {
      if (bulletbelt[i] != null)
      {
        //move onto the next one if there is a bullet existing in this space
        //of of the array
      } else
      {
        bulletbelt[i] = new Bullet(bulletimage, super.position, 30, 30, super.shiprotateamount);
        break;
      }
    }
  }

  //contains the ship within the boundaries
  public void contain()
  {
    super.contain();
  }

  //returns an array of bullets
  public Bullet[] returnbullets()
  {
    return bulletbelt;
  }

  //makes a bullet dissapear once the time increment reaches 
  //a certain amount and leaves an empty spot in the array. 
  public void bulletdissapear()
  {
    for (int i = 0; i < bulletbelt.length; i++)
    {
      if (bulletbelt[i] != null) {
        if (bulletbelt[i].timevalue() >= bullettimeMAX)
        {
          bulletbelt[i] = null;
        }
      }
    }
  }

  //immunity method. When you die and your score is reset, you get
  //a short amount of time where nothing can collide with you. 
  public void immunity()
  {
    i++;
    if (i<100)
    {
      tint(150);
    } else
    {
      tint(255);
      i = 0;
      immune = false;
    }
  }
  
  //return ship rotate amount
  public float returnrotate()
  {
    return super.shiprotateamount;
  }
  
  //returns position
  public PVector returnposition()
  {
   return super.position; 
  }
  
  //sets bullet image
  public void bulletimagereset(PImage pic)
  {
    bulletimage = pic;
  }
}
  public void settings() {  size(800, 600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "America_Is_The_Best" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
