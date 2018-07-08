package LanScanner;

import gui.LanScannerGui;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class Generator
{
  String stringip;
  String stringsubnet;
  int[] ip = new int[4];
  int[] subnet = new int[4];
  int[] filtermask = new int[4];
  int[] base = new int[4];
  int a = 0; int b = 0; int c = 0; int d = 0;
  int pfixlen;
  public float counter = 0.0F;
  public float totalips;
  LanScannerGui scannerGui; int NO_THREADS = 20;
  ThreadedExecutor[] ty = new ThreadedExecutor[NO_THREADS];
  int port = 1337;
  int tout = 1000;
  
  public Generator(LanScannerGui stl) throws SocketException, UnknownHostException {
    scannerGui = stl;
    a = 0;b = 0;c = 0;d = 0;
    stringip = Inet4Address.getLocalHost().getHostAddress();
    pfixlen = ((InterfaceAddress)NetworkInterface.getByInetAddress(Inet4Address.getLocalHost()).getInterfaceAddresses().get(0)).getNetworkPrefixLength();
    stringsubnet = prefix2subnet((short)pfixlen);
    scannerGui.setDefaultParams(stringsubnet);
  }
  
  public synchronized String next() {
    float ft = ++counter / totalips;
    ft *= 100.0F;
    scannerGui.updateProgress((int)ft);
    if (a++ >= filtermask[3])
      if (++b < filtermask[2]) { a = 0;
      } else if (++c < filtermask[1]) { a = 0;b = 0;
      } else if (++d < filtermask[0]) { a = 0;b = 0;c = 0;
      } else { return null;
      }
    return base[0] + d + "." + (base[1] + c) + "." + (base[2] + b) + "." + (base[3] + a);
  }
  
  public void exec(String usubnet, int port, int tout) {
    int x = -1;
    do {
      ip[x] = Integer.parseInt(stringip.split("\\.")[x]);
      subnet[x] = Integer.parseInt(usubnet.split("\\.")[x]);x++;
    } while (x < 4);
    


    filtermask[0] = (256 - subnet[0]);
    filtermask[1] = (256 - subnet[1]);
    filtermask[2] = (256 - subnet[2]);
    filtermask[3] = (256 - subnet[3]);
    int i = 4;
    do {
      base[i] = (ip[i] - ip[i] % filtermask[i]);i--;
    } while (i != -1);
    

    totalips = (filtermask[0] * filtermask[1] * filtermask[2] * filtermask[3]);
    
    for (int t = 0; t < ty.length; t++) {
      ty[t] = new ThreadedExecutor(this, port, tout);
      ty[t].start();
    }
  }
  
  private static String prefix2subnet(short prflen) { if (prflen == 16)
      return "255.255.246.0";
    int shft = -1 << 32 - prflen;
    int oct1 = (byte)((shft & 0xFF000000) >> 24) & 0xFF;
    int oct2 = (byte)((shft & 0xFF0000) >> 16) & 0xFF;
    int oct3 = (byte)((shft & 0xFF00) >> 8) & 0xFF;
    int oct4 = (byte)(shft & 0xFF) & 0xFF;
    return oct1 + "." + oct2 + "." + oct3 + "." + oct4;
  }
  
  public void halt() { counter = 0.0F;a = 0;b = 0;c = 0;d = 0;
    for (ThreadedExecutor da : ty) {
      da.interrupt();
    }
  }
}
