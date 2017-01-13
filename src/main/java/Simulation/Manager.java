package Simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsgreen on 1/12/17.
 */
public class Manager {
  private List<Node> nodes;

  public Manager(int n, int m) {
    nodes = new ArrayList<Node>();
    for (int i=1; i<=n; i++) {
      for (int j=1; j<=m; j++) {
        Node node = new Node(i, j, n, m);
        nodes.add(node);
      }
    }
  }

  public void start() {
    for (Node node: nodes) {
      new Thread(node).start();
    }
  }

  public static void main(String[] args) {
    Manager m = new Manager(3, 3);
    m.start();
  }

}
