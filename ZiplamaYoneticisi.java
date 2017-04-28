import javax.microedition.lcdui.*;
public class ZiplamaYoneticisi extends javax.microedition.lcdui.game.LayerManager {
  static int CANVAS_X;
  static int CANVAS_Y;
  static int DISP_WIDTH;
  static int DISP_HEIGHT;

  private Adam adam;
  private Engel[] myLeftTumbleweeds;
  private Engel[] myRightTumbleweeds;
  private Cimen cimen;

  private boolean myLeft;
  private int myCurrentLeftX;
  void setLeft(boolean left) {
    myLeft = left;
  }

  public ZiplamaYoneticisi(int x, int y, int width, int height)
      throws Exception {
    CANVAS_X = x;
    CANVAS_Y = y;
    DISP_WIDTH = width;
    DISP_HEIGHT = height;
    myCurrentLeftX = Cimen.CYCLE*Cimen.TILE_WIDTH;
    setViewWindow(0, 0, DISP_WIDTH, DISP_HEIGHT);
    if(adam == null) {
      adam = new Adam(myCurrentLeftX + DISP_WIDTH/2, DISP_HEIGHT - Adam.BOY - 2);
      append(adam);
    }
    if(myLeftTumbleweeds == null) {
      myLeftTumbleweeds = new Engel[2];
      for(int i = 0; i < myLeftTumbleweeds.length; i++) {
	myLeftTumbleweeds[i] = new Engel(true);
	append(myLeftTumbleweeds[i]);
      }
    }
    if(myRightTumbleweeds == null) {
      myRightTumbleweeds = new Engel[2];
      for(int i = 0; i < myRightTumbleweeds.length; i++) {
	myRightTumbleweeds[i] = new Engel(false);
	append(myRightTumbleweeds[i]);
      }
    }
    if(cimen == null) {
      cimen = new Cimen();
      append(cimen);
    }
  }

  void reset() {
    if(cimen != null) {
      cimen.reset();
    }
    if(adam != null) {
      adam.reset();
    }
    if(myLeftTumbleweeds != null) {
      for(int i = 0; i < myLeftTumbleweeds.length; i++) {
	myLeftTumbleweeds[i].reset();
      }
    }
    if(myRightTumbleweeds != null) {
      for(int i = 0; i < myRightTumbleweeds.length; i++) {
	myRightTumbleweeds[i].reset();
      }
    }
    myLeft = false;
    myCurrentLeftX = Cimen.CYCLE*Cimen.TILE_WIDTH;
  }

  public void paint(Graphics g) {
    setViewWindow(myCurrentLeftX, 0, DISP_WIDTH, DISP_HEIGHT);
    paint(g, CANVAS_X, CANVAS_Y);
  }

  private void wrap() {
    if(myCurrentLeftX % (Cimen.TILE_WIDTH*Cimen.CYCLE) == 0) {
      if(myLeft) {
	adam.move(Cimen.TILE_WIDTH*Cimen.CYCLE, 0);
	myCurrentLeftX += (Cimen.TILE_WIDTH*Cimen.CYCLE);
	for(int i = 0; i < myLeftTumbleweeds.length; i++) {
	  myLeftTumbleweeds[i].move(Cimen.TILE_WIDTH*Cimen.CYCLE, 0);
	}
	for(int i = 0; i < myRightTumbleweeds.length; i++) {
	  myRightTumbleweeds[i].move(Cimen.TILE_WIDTH*Cimen.CYCLE, 0);
	}
      } else {
	adam.move(-(Cimen.TILE_WIDTH*Cimen.CYCLE), 0);
	myCurrentLeftX -= (Cimen.TILE_WIDTH*Cimen.CYCLE);
	for(int i = 0; i < myLeftTumbleweeds.length; i++) {
	  myLeftTumbleweeds[i].move(-Cimen.TILE_WIDTH*Cimen.CYCLE, 0);
	}
	for(int i = 0; i < myRightTumbleweeds.length; i++) {
	  myRightTumbleweeds[i].move(-Cimen.TILE_WIDTH*Cimen.CYCLE, 0);
	}
      }
    }
  }

  int advance(int gameTicks) {
    int retVal = 0;
    if(myLeft) {
      myCurrentLeftX--;
    } else {
      myCurrentLeftX++;
    }
    cimen.hareketEttir(gameTicks);
    adam.hareketEttir(gameTicks, myLeft);
    for(int i = 0; i < myLeftTumbleweeds.length; i++) {
      retVal += myLeftTumbleweeds[i].advance(adam, gameTicks, 
		    myLeft, myCurrentLeftX, myCurrentLeftX + DISP_WIDTH);
      retVal -= adam.checkCollision(myLeftTumbleweeds[i]);
    }
    for(int i = 0; i < myLeftTumbleweeds.length; i++) {
      retVal += myRightTumbleweeds[i].advance(adam, gameTicks, 
           myLeft, myCurrentLeftX, myCurrentLeftX + DISP_WIDTH);
      retVal -= adam.checkCollision(myRightTumbleweeds[i]);
    }
    wrap();
    return(retVal);
  }

  void zipla() {
    adam.zipla();
  }
}
