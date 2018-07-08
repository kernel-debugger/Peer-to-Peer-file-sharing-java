package gui;

public class Validateip {
  public Validateip() {}
  
  public static String ip(String ip) throws NumberFormatException { try { if ((Integer.parseInt(ip.split("\\.")[0]) < 256) && (Integer.parseInt(ip.split("\\.")[1]) < 256) && (Integer.parseInt(ip.split("\\.")[2]) < 256) && (Integer.parseInt(ip.split("\\.")[3]) < 256)) {
        return ip;
      }
      throw new NumberFormatException();
    }
    catch (NumberFormatException e) {
      throw new NumberFormatException();
    }
  }
}
