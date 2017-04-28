import javax.microedition.lcdui.*;

public class ZiplaCanvas extends javax.microedition.lcdui.game.GameCanvas {

  static final int GROUND_HEIGHT = 32;

  static final int CORNER_X = 0;
  static final int CORNER_Y = 0;
  static int DISP_WIDTH;
  static int DISP_HEIGHT;

  static int FONT_HEIGHT;
  static Font FONT;
  static int SCORE_WIDTH;

  static int TIME_WIDTH;

  public static final int SIYAH = 0;
  public static final int BEYAZ = 0xffffff;

  private Display goruntu;

  private Zipla zipla;

  private ZiplamaYoneticisi yonetici;

  private boolean oyunBittiMi;

  private int puan = 0;

  private int myInitialGameTicks = 950;
  private int myOldGameTicks = myInitialGameTicks;
  private int myGameTicks = myOldGameTicks;
  private static String zaman = "1:00";
  private String toplamZaman = zaman;

  void setGameOver() {
    oyunBittiMi = true;
    zipla.pauseApp();
  }

  public ZiplaCanvas(Zipla midlet) throws Exception {
    super(false);
    goruntu = Display.getDisplay(midlet);
    zipla = midlet;
    DISP_WIDTH = getWidth();
    DISP_HEIGHT = getHeight();
    Display disp = Display.getDisplay(zipla);
    if(disp.numColors() < 256) {
      throw(new Exception("game requires 256 shades"));
    }
    if((DISP_WIDTH > 250) || (DISP_HEIGHT > 320)) {
      throw(new Exception("Screen too large"));
    }
    FONT = getGraphics().getFont();
    FONT_HEIGHT = FONT.getHeight();
    SCORE_WIDTH = FONT.stringWidth("Score: 000");
    TIME_WIDTH = FONT.stringWidth("Time: " + zaman);
    if(yonetici == null) {
      yonetici = new ZiplamaYoneticisi(CORNER_X, CORNER_Y + FONT_HEIGHT*2, 
	   DISP_WIDTH, DISP_HEIGHT - FONT_HEIGHT*2 - GROUND_HEIGHT);
    } 
  }

  void start() {
    oyunBittiMi = false;
    goruntu.setCurrent(this);
    repaint();
  }

  void reset() {
    yonetici.reset();
    puan = 0;
    oyunBittiMi = false;
    myGameTicks = myInitialGameTicks;
    myOldGameTicks = myInitialGameTicks;
    repaint();
  }

  void flushKeys() {
    getKeyStates();
  }

  protected void hideNotify() {
  }
  protected void showNotify() {
  }

  public void paint(Graphics g) {
    g.setColor(BEYAZ);
    g.fillRect(CORNER_X, CORNER_Y, DISP_WIDTH, DISP_HEIGHT);
    g.setColor(0, 255, 0);
    g.fillRect(CORNER_X, CORNER_Y + DISP_HEIGHT - GROUND_HEIGHT, 
	       DISP_WIDTH, DISP_HEIGHT);
    try {
      yonetici.paint(g);
    } catch(Exception e) {
      zipla.hataMesaji(e);
    }
    g.setColor(SIYAH);
    g.setFont(FONT);
    g.drawString("Score: " + puan, (DISP_WIDTH - SCORE_WIDTH)/2, 
		 DISP_HEIGHT + 5 - GROUND_HEIGHT, g.TOP|g.LEFT);
    g.drawString("Time: " + formatTime(), (DISP_WIDTH - TIME_WIDTH)/2, 
		 CORNER_Y + FONT_HEIGHT, g.TOP|g.LEFT);
    if(oyunBittiMi) {
      zipla.setNewCommand();
      g.setColor(BEYAZ);
      g.fillRect(CORNER_X, CORNER_Y, DISP_WIDTH, FONT_HEIGHT*2 + 1);
      int goWidth = FONT.stringWidth("Oyun Bitti");
      g.setColor(SIYAH);
      g.setFont(FONT);
      g.drawString("Oyun Bitti", (DISP_WIDTH - goWidth)/2, 
      		   CORNER_Y + FONT_HEIGHT, g.TOP|g.LEFT);
    }
  }
  
  public String formatTime() {
    if((myGameTicks / 16) + 1 != myOldGameTicks) {
      toplamZaman = "";
      myOldGameTicks = (myGameTicks / 16) + 1;
      int smallPart = myOldGameTicks % 60;
      int bigPart = myOldGameTicks / 60;
      toplamZaman += bigPart + ":";
      if(smallPart / 10 < 1) {
	toplamZaman += "0";
      }
      toplamZaman += smallPart;
    }
    return(toplamZaman);
  }

  void hareketEttir() {
    myGameTicks--;
    puan += yonetici.advance(myGameTicks);
    if(myGameTicks == 0) {
      setGameOver();
    }
    try {
      paint(getGraphics());
      flushGraphics();
    } catch(Exception e) {
      zipla.hataMesaji(e);
    }
  }

  public void tusKontrol() { 
    if(! oyunBittiMi) {
      int keyState = getKeyStates();
      if((keyState & LEFT_PRESSED) != 0) {
	yonetici.setLeft(true);
      } 
      if((keyState & RIGHT_PRESSED) != 0) {
	yonetici.setLeft(false);
      }
      if((keyState & UP_PRESSED) != 0) {
	yonetici.zipla();
      } 
    }
  }
}
