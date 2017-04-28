import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Zipla extends MIDlet implements CommandListener {

  private Command cikisKomutu = new Command("Cikis", Command.EXIT, 99);
  private Command hareketKomutu = new Command("Basla", Command.SCREEN, 1);
  private Command duraklatKomutu = new Command("Duraklat", Command.SCREEN, 1);
  private Command yeniKomutu = new Command("Tekrar Oyna", Command.SCREEN, 1);

  private ZiplaCanvas oyunCanvas;
  private OyunThread oyunThread;

  public Zipla() {
  }

  void setNewCommand() {
    oyunCanvas.removeCommand(duraklatKomutu);
    oyunCanvas.removeCommand(hareketKomutu);
    oyunCanvas.addCommand(yeniKomutu);
  }

  private void setGoCommand() {
    oyunCanvas.removeCommand(duraklatKomutu);
    oyunCanvas.removeCommand(yeniKomutu);
    oyunCanvas.addCommand(hareketKomutu);
  }

  private void setPauseCommand() {
    oyunCanvas.removeCommand(yeniKomutu);
    oyunCanvas.removeCommand(hareketKomutu);
    oyunCanvas.addCommand(duraklatKomutu);
  }

  public void startApp() throws MIDletStateChangeException {
    try {
      if(oyunCanvas == null) {
        oyunCanvas = new ZiplaCanvas(this);
        oyunCanvas.addCommand(cikisKomutu);
        oyunCanvas.addCommand(duraklatKomutu);
        oyunCanvas.setCommandListener(this);
      }
      if(oyunThread == null) {
        oyunThread = new OyunThread(oyunCanvas);
        oyunCanvas.start();
        oyunThread.start();
      } else {
        oyunCanvas.removeCommand(hareketKomutu);
        oyunCanvas.addCommand(duraklatKomutu);
        oyunCanvas.flushKeys();
        oyunThread.oyunuDevamEttir();
      }
    } catch(Exception e) {
        e.printStackTrace();
      hataMesaji(e);
    }
  }
  
  public void destroyApp(boolean unconditional) 
      throws MIDletStateChangeException {
    if(oyunThread != null) {
      oyunThread.durIstegi();
    }
    oyunThread = null;
    oyunCanvas = null;
    System.gc();
  }

  public void pauseApp() {
    if(oyunCanvas != null) {
      setGoCommand();
    }
    if(oyunThread != null) {
      oyunThread.oyunuDuraklat();
    }
  }

  public void commandAction(Command c, Displayable s) {
    if(c == hareketKomutu) {
      oyunCanvas.removeCommand(hareketKomutu);
      oyunCanvas.addCommand(duraklatKomutu);
      oyunCanvas.flushKeys();
      oyunThread.oyunuDevamEttir();
    } else if(c == duraklatKomutu) {
      oyunCanvas.removeCommand(duraklatKomutu);
      oyunCanvas.addCommand(hareketKomutu);
      oyunThread.oyunuDuraklat();
    } else if(c == yeniKomutu) {
      oyunCanvas.removeCommand(yeniKomutu);
      oyunCanvas.addCommand(duraklatKomutu);
      oyunCanvas.reset();
      oyunThread.oyunuDevamEttir();
    } else if((c == cikisKomutu) || (c == Alert.DISMISS_COMMAND)) {
      try {
        destroyApp(false);
        notifyDestroyed();
      } catch (MIDletStateChangeException ex) {
      }
    } 
  }
  
  void hataMesaji(Exception e) {
    if(e.getMessage() == null) {
      hataMesaji(e.getClass().getName());
    } else {
      hataMesaji(e.getClass().getName() + ":" + e.getMessage());
    }
  }

  void hataMesaji(String msg) {
    Alert hataAlarmi = new Alert("error", msg, null, AlertType.ERROR);
    hataAlarmi.setCommandListener(this);
    hataAlarmi.setTimeout(Alert.FOREVER);
    Display.getDisplay(this).setCurrent(hataAlarmi);
  }
}
