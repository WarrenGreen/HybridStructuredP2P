package Simulation;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wsgreen on 1/12/17.
 */
public class Node implements Runnable {
  private Thread searcherThread;
  private Thread rankerThread;
  private Ranker ranker;

  protected final ConcurrentLinkedQueue<Edge> found;
  protected AtomicBoolean running;

  protected final int size_col, size_row;
  protected final int col, row;
  private final String name;

  private final static int DEFAULT_NUM_EDGES = 3;
  private final static Logger LOG = Logger.getInstance();

  public Node(int n, int m, int col, int row) {
    this(n, m, col, row, DEFAULT_NUM_EDGES);
  }

  public Node(int n, int m, int col, int row, int numEdges) {
    this.col = n; this.row = m;
    size_col = col; size_row = row;
    name = String.format("(%d, %d)", this.col, this.row);

    found = new ConcurrentLinkedQueue<Edge>();
    running = new AtomicBoolean();
    running.set(false);

    searcherThread = new Thread(new Searcher());
    ranker = new Ranker(numEdges);
    rankerThread = new Thread(ranker);
  }

  public void run() {
    running.set(true);
    searcherThread.start();
    rankerThread.start();

    try {
      Thread.sleep(1000);
      running.set(false);

      searcherThread.join();
      rankerThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    synchronized (System.out) {
      System.out.println(toString());
      System.out.flush();
    }

    LOG.teardown();
  }

  public String toString() {
    String out = String.format("(%d, %d) {", col, row);
    Iterator<Edge> iterator = ranker.getRanked().iterator();
    while(iterator.hasNext()) {
      Edge e = iterator.next();
      out += String.format("(%d, %d)", e.col, e.row);

      if(iterator.hasNext()) {
        out += ", ";
      }
    }
    out += "}";

    return out;
  }

  private class Searcher implements Runnable {
    private PrimitiveIterator.OfInt colStream, rowStream;


    public Searcher() {
      Random random = new Random();
      colStream = random.ints(1, size_col + 1).iterator();
      rowStream = random.ints(1, size_row + 1).iterator();
    }

    public void run() {
      while (running.get()) {
        int n = colStream.next();
        int m = rowStream.next();

        if (!(n == col && m == row)) {
          found.add(new Edge(n, m));
        }
      }
    }
  }

  private class Ranker implements Runnable {
    private final int numRanked;
    private final PriorityQueue<Edge> ranked;


    public Ranker(int size) {
      numRanked = size;
      ranked = new PriorityQueue<Edge>(new comparator());
    }

    public Collection<Edge> getRanked() {
      return Collections.unmodifiableCollection(ranked);
    }

    public void run() {
      Set<String> rankedSet = new HashSet<String>();
      while (running.get()) {
        if (!found.isEmpty()) {
          Edge e = found.poll();
          if(rankedSet.contains(e.toString())) {
            continue;
          }

          ranked.add(e);
          LOG.println(String.format("A,%s,%s", name, e.toString()));
          rankedSet.add(e.toString());

          if (ranked.size() > numRanked) {
            String del = ranked.poll().toString();
            rankedSet.remove(del);
            LOG.println(String.format("D,%s,%s", name, del));
          }
        }
      }
    }
  }

  class comparator implements Comparator<Edge> {

    public int compare(Edge o1, Edge o2) {
      int dist1 = o1.dist(col, row);
      int dist2 = o2.dist(col, row);

      if (dist1 < dist2)
        return 1;
      else if (dist1 > dist2)
        return -1;
      else
        return 0;
    }
  }
}
