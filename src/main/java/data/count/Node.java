package data.count;

import java.util.Map;
import java.util.TreeMap;

public class Node {
    public String word;
    public Map<String, Integer> next;
    public Map<String, Integer> prev;
    public Integer start;
    public Integer end;
    public Integer count;

    public Node(String word) {
        this.word = word;
        this.next = new TreeMap<>();
        this.prev = new TreeMap<>();
        this.start = 0;
        this.end = 0;
        this.count = 0;
    }
}
