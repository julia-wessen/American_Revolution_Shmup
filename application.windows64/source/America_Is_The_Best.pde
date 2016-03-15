import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

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

void setup()
{
  //buidling the scene
  size(800, 600, P2D);
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

void draw() 
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
void keyPressed()
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
void keyReleased()
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
void runship()
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
void spawnasteroids()
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