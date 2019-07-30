package data.correct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.count.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class AutoCorrector {
    private Map<String, Node> nodes = new HashMap<>();

    public AutoCorrector(String knowledgePath) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(knowledgePath));
            String line;
            Gson gson = new Gson();
            while ((line = reader.readLine()) != null) {
                Node node = gson.fromJson(line, Node.class);
                this.nodes.put(node.word, node);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String correct(String sentence) {
        String[] words = sentence.split("\\s+");
        return sentence;
    }

    public List<Float[]> getScore(String[] words) {
        List<Float[]> scores = new ArrayList<>();
        int numWord = words.length;
        for (int i = 0; i < numWord; i++) {
            Float[] score = new Float[numWord];
            score[i] = 1f;
            for (int j = i; j > 0; --j) {
                int maxScore = this.nodes.get(words[j]).prev.entrySet().stream().findFirst().get().getValue();
                score[j - 1] = this.nodes.get(words[j]).prev.getOrDefault(words[j - 1], 0) / (float)maxScore;
            }
            for (int j = i; j < numWord - 1; ++j) {
                int maxScore = this.nodes.get(words[j]).next.entrySet().stream().findFirst().get().getValue();
                score[j + 1] = this.nodes.get(words[j]).next.getOrDefault(words[j + 1], 0) / (float)maxScore;
            }
            scores.add(score);
        }
        return scores;
    }

    public static void main(String[] args) {
        AutoCorrector corrector = new AutoCorrector("result/kiki.1gram");
        List<Float[]> scores = corrector.getScore("bây giờ là mấy giờ".split("\\s+"));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(scores));
    }

}
