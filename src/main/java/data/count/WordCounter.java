package data.count;


import com.google.gson.Gson;
import data.preprocess.TextPreprocess;

import java.io.*;
import java.util.*;

public class WordCounter {
    private String corpusPath;
    private String outputPath;
    private int ngram;

    List<Node> nodes = new ArrayList<>();
    Map<String, Integer> indexs = new HashMap<>();

    public WordCounter(String corpusPath, String outputPath, int ngram, int minCount, int minCountChild) {
        this.corpusPath = corpusPath;
        this.outputPath = outputPath;
        this.ngram = ngram;
        File file = new File(this.outputPath);
        if (file.exists()) file.delete();

        this.count();
        this.export(minCount, minCountChild);
    }

    private void countOnSentence(String sentence) {
        String[] words = sentence.split("\\s+");
        int index;
        int l = words.length;
        if (l < ngram + 1) return;
        for (int i = 0; i < l - ngram + 1; i++) {
            // make key
            String key = "";
            for (int j = i; j < ngram + i; j++) key += words[j] + " ";
            key = key.trim();
            if (!indexs.containsKey(key)) {
                indexs.put(key, nodes.size());
                nodes.add(new Node(key));
            }
            index = indexs.get(key);
            // next
            if (i < l - ngram) {
                nodes.get(index).next.put(words[ngram + i], nodes.get(index).next.getOrDefault(words[ngram + i], 0) + 1);
            }
            // prev
            if (i > 0) {
                nodes.get(index).prev.put(words[i - 1], nodes.get(index).prev.getOrDefault(words[i - 1], 0) + 1);
            }
            // count
            nodes.get(index).count++;
        }
        nodes.get(indexs.get(words[0])).start++;
        nodes.get(indexs.get(words[l - 1])).end++;
    }

    private void count() {
        BufferedReader reader;
        int lineCount = 0;
        try {
            reader = new BufferedReader(new FileReader(this.corpusPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches(".*\\d.*") || line.trim().equals("")) continue;
                lineCount++;
                if (lineCount % 1000 == 0) {
                    System.out.print("Current line " + lineCount + "\r");
                }
                line = TextPreprocess.preprocessSentence(line);
                this.countOnSentence(line);
            }
        } catch (Exception e) {

        }
    }

    private void export(int minCount, int minCountChild) {
        System.out.println();
        Gson gson = new Gson();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputPath));
            for (Node node : this.nodes) {
                if (node.count >= minCount) {
                    node.next = this.sortByValue(node.next, minCountChild);
                    node.prev = this.sortByValue(node.prev, minCountChild);
                    writer.write(gson.toJson(node) + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> map, int minCountChild) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((Comparable<Integer>) ((Map.Entry<String, Integer>) (o2)).getValue()).compareTo(((Map.Entry<String, Integer>) (o1)).getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            if (entry.getValue() >= minCountChild) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
