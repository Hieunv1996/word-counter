package data.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class TextPreprocess {
    private String inputFile;
    private String outputFile;

    public TextPreprocess(String inputFile) {
        this.inputFile = inputFile;
        this.outputFile = this.inputFile + ".out";
        this.preprocess();
    }

    public TextPreprocess(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.preprocess();
    }

    public static String preprocessSentence(String sentence) {
        sentence = sentence.toLowerCase();
        sentence = sentence.replaceAll("[\\.,\\?!~@#\\$%\\^&\\*\\(\\)\\{\\}\\[\\]\\+-:;\"/\\\\\\|`]+", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("' ", "")
                .replaceAll(" '", "")
                .trim();

        return sentence;
    }

    private void preprocess() {
        BufferedReader reader;
        BufferedWriter writer;

        try {
            reader = new BufferedReader(new FileReader(this.inputFile));
            writer = new BufferedWriter(new FileWriter(this.outputFile));

            String line;
            while ((line = reader.readLine()) != null) {
                line = preprocessSentence(line);
                writer.write(line + "\n");
            }
        } catch (Exception e) {

        }
    }
}
