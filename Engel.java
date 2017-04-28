import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Engel extends Sprite {

  static final int WIDTH = 16;

  private Random rastgele = new Random();

  private boolean atladi;

  private boolean myLeft;

  private int myY;

  public Engel(boolean left) throws Exception {
    super(Image.createImage("/resimler/tumbleweed.png"), WIDTH, WIDTH);
    myY = ZiplamaYoneticisi.DISP_HEIGHT - WIDTH - 2;
    myLeft = left;
    if(!myLeft) {
      setTransform(TRANS_MIRROR);
    }
    atladi = false;
    setVisible(false);
  }

  void reset() {
    setVisible(false);
    atladi = false;
  }
  int advance(Adam cowboy, int tickCount, boolean left,
	      int currentLeftBound, int currentRightBound) {
    int retVal = 0;
    if((getRefPixelX() + WIDTH <= currentLeftBound) || (getRefPixelX() - WIDTH >= currentRightBound)) {
      setVisible(false);
    } 
    if(!isVisible()) {
      int rand = getRandomInt(100);
      if(rand == 3) {
	atladi = false;
	setVisible(true);
	if(myLeft) {
	  setRefPixelPosition(currentRightBound, myY);
	  move(-1, 0);
	} else {
	  setRefPixelPosition(currentLeftBound, myY);
	  move(1, 0);
	}
      }
    } else {
      if(tickCount % 2 == 0) { 
	nextFrame();
      }
      if(myLeft) {
	move(-3, 0);
	if((! atladi) && 
	   (getRefPixelX() < cowboy.getRefPixelX())) {
	  atladi = true;
	  retVal = cowboy.puaniArttir();
	}
      } else {
	move(3, 0);
	if((! atladi) && 
	   (getRefPixelX() > cowboy.getRefPixelX() + Adam.EN)) {
	  atladi = true;
	  retVal = cowboy.puaniArttir();
	}
      }
    }
    return(retVal);
  }

  public int getRandomInt(int upper) {
    int retVal = rastgele.nextInt() % upper;
    if(retVal < 0) {
      retVal += upper;
    }
    return(retVal);
  }
}
