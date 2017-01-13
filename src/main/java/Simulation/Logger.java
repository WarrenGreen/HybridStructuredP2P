package Simulation;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by wsgreen on 1/13/17.
 */
public class Logger {
  private static final Logger logger = new Logger();
  private static PrintWriter writer;

  public Logger() {
    try{
      writer = new PrintWriter("log.out", "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static Logger getInstance() {
    return logger;
  }

  protected synchronized static void println(String str) {
    writer.println(str);
  }

  protected static void teardown() {
    writer.flush();
    writer.close();
  }
}
