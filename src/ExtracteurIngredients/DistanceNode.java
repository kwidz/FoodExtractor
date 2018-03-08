package ExtracteurIngredients;

public class DistanceNode implements Comparable<DistanceNode> {
    TrieNode node;
    String word;
    int distance;
    boolean wordExists;

    DistanceNode(TrieNode node, int distance, String word, boolean wordExists) {
        this.node = node;
        this.distance = distance;
        this.word = word;
        this.wordExists = wordExists;
    }

    // break ties of distance with frequency
    public int compareTo(DistanceNode n) {
        if (distance == n.distance)
            return n.node.occurances - node.occurances;
        return distance - n.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        if (!(o instanceof DistanceNode))
            return false;

        DistanceNode n = (DistanceNode)o;
        return distance == n.distance && n.node.occurances == node.occurances;
    }

    @Override
    public int hashCode()
    {
        int hash = 31;
        hash += distance * 31;
        hash += node.occurances * 31;
        return hash;
    }

    @Override
    public String toString() {
        return word+":"+distance;
    }
}
