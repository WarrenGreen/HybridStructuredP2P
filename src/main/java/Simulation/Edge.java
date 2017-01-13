package Simulation;

/**
 * Created by wsgreen on 1/13/17.
 */
public class Edge implements Comparable {
  final int col, row;

  public Edge(int col, int row) {
    this.col = col;
    this.row = row;
  }

  private int manhattanDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }

  public int compareTo(Object o) {
    Edge other = (Edge) o;
    return manhattanDistance(col, row, other.col, other.row);
  }

  public int dist(int x, int y) {
    return manhattanDistance(col, row, x, y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", col, row);
  }
}