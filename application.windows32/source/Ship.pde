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
  void display() 
  {
    super.display();
  }

  //move method based on a int parameter
  void Move(int dinum)
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
  void applyfriction()
  {
    super.friction();
  }

  //fire method that uses the bullets the ship has
  void fire()
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
  void contain()
  {
    super.contain();
  }

  //returns an array of bullets
  Bullet[] returnbullets()
  {
    return bulletbelt;
  }

  //makes a bullet dissapear once the time increment reaches 
  //a certain amount and leaves an empty spot in the array. 
  void bulletdissapear()
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
  void immunity()
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
  float returnrotate()
  {
    return super.shiprotateamount;
  }
  
  //returns position
  PVector returnposition()
  {
   return super.position; 
  }
  
  //sets bullet image
  void bulletimagereset(PImage pic)
  {
    bulletimage = pic;
  }
}