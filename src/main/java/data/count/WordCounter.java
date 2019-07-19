package data.count;


import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class WordCounter {
    private String corpusPath;
    private String outputPath;
    private int ngram;

    List<Node> nodes = new ArrayList<>();
    Map<String, Integer> indexs = new HashMap<>();

    public WordCounter(String corpusPath, String outputPath, int ngram, int min_count) {
        this.corpusPath = corpusPath;
        this.outputPath = outputPath;
        this.ngram = ngram;
        File file = new File(this.outputPath);
        if (file.exists()) file.delete();

        this.count();
        this.export(min_count);
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
                lineCount++;
                if (lineCount % 1000 == 0) {
                    System.out.print("Current line " + lineCount + "\r");
                }
                this.countOnSentence(line);
            }
        } catch (Exception e) {

        }
    }

    private void export(int min_count) {
        System.out.println();
        Gson gson = new Gson();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputPath));
            for (Node node : this.nodes) {
                if (node.count >= min_count){
                    node.next = this.sortByValue(node.next);
                    node.prev = this.sortByValue(node.prev);
                    writer.write(gson.toJson(node) + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o2)).getValue()).compareTo(((Map.Entry<K, V>) (o1)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
