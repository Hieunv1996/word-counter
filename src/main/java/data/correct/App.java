package data.correct;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        EndingCorrector endingCorrector = new EndingCorrector("result/kiki_1gram.count");

        String input = "result/wer_per_utt.txt";
        String output = input + ".out";

        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new FileReader(input));
            writer = new BufferedWriter(new FileWriter(output));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
                line = reader.readLine();
                writer.write(line + "\n");


                List<String> parts = Arrays.asList(line.split("\\s+"));
                /* Process here */
                if (line.contains("***")) {
                    writer.write(line.replaceAll("\\s+hyp\\s+", " >>>  ") + "\n");
                } else {
                    writer.write(String.join(" ", parts.subList(0, 1)));
                    writer.write(" >>>  " + endingCorrector.removeNoise(line.replaceAll(".+hyp\\s+", ""), 2000) + "\n");
                }
                line = reader.readLine();
                writer.write(line.trim() + "\n");
                reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (Exception e) {

        }

    }
}
