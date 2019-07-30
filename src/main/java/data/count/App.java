package data.count;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class App {
    public static void main(String[] args) {

        if (args.length < 2) throw new ValueException("args length at least 2");

        int ngram = 1;
        try {
            ngram = Integer.parseInt(args[2]);
        } catch (Exception e) {

        }
        int minCount = 500;
        try {
            minCount = Integer.parseInt(args[3]);
        } catch (Exception e) {

        }

        int minChildCount = 20;
        try {
            minChildCount = Integer.parseInt(args[4]);
        } catch (Exception e) {

        }
        new WordCounter(args[0], args[1], ngram, minCount, minChildCount);
    }
}
