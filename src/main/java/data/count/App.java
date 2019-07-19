package data.count;

public class App {
    public static void main(String[] args) {
//        String corpusPath = "/home/lap60313/data/kiki_test/test_chi_Khanh.txt.out";
//
//        String outputPath = "result/test_kiki.txt";
//
//        new WordCounter(corpusPath, outputPath, 0);
        new WordCounter(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    }
}
