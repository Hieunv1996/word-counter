package data.correct;

import com.google.gson.Gson;
import data.count.Node;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EndingCorrector {
    private String json_data;
    private Map<String, Node> nodes;

    public EndingCorrector(String json_data) {
        this.json_data = json_data;
        nodes = new HashMap<>();
        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(this.json_data));
            String line;
            while ((line = reader.readLine()) != null) {
                Node node = gson.fromJson(line, Node.class);
                nodes.put(node.word, node);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewScore(String sentence) {
        String[] words = sentence.split("\\s+");
        int l = words.length;
        System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "word", "count", "prev", "next", "__detect__"));
        int prev = 0, next = 0, count;
        for (int i = 0; i < l; i++) {
            if (!nodes.containsKey(words[i])) {
                return;
            }
            if (i == 0) {
                prev = -1;
                next = nodes.get(words[i]).next.getOrDefault(words[i + 1], 0);
            } else if (i == l - 1) {
                prev = nodes.get(words[i]).prev.getOrDefault(words[i - 1], 0);
                next = -1;
            } else {
                if (nodes.containsKey(words[i])) {
                    prev = nodes.get(words[i]).prev.getOrDefault(words[i - 1], 0);
                    next = nodes.get(words[i]).next.getOrDefault(words[i + 1], 0);
                } else {
                    prev = 0;
                    next = 0;
                }
            }
            count = nodes.get(words[i]).count;
            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", words[i], count, prev, next, (prev * next == 0 ? "YES" : "NO")));
        }
    }


    public String removeNoise(String sentence, int minCount) {
        String[] words = sentence.split("\\s+");
        if (words.length < 2) return sentence;
        int start = 0, end = words.length;
        for (int i = 1; i < words.length - 1; i++) {
            if (nodes.containsKey(words[i])) {
                if (nodes.get(words[i]).prev.getOrDefault(words[i - 1], 0) == 0 && start == -1 && nodes.get(words[i]).start >= minCount)
                    start = i;
                if (nodes.get(words[i]).next.getOrDefault(words[i + 1], 0) == 0 && end == words.length && nodes.get(words[i]).end >= minCount)
                    end = i + 1;
            }
        }
        String out = String.join("  ", Arrays.asList(words).subList(start, end));
        if (start != 0) out += "\ts" + nodes.get(words[start]).start;
        if (end != words.length) out += "\te" + nodes.get(words[end-1]).end;
        return out;
    }
}
