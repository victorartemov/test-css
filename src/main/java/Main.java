import java.io.*;
import java.util.*;

/**
 * Created by Виктор on 10.04.2017.
 */
public class Main {

    private static String firstFile = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\first.css";
    private static String secondFile = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\second.css";

    private static String firstFileFormatted = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\first_formatted.css";
    private static String secondFileFormatted = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\second_formatted.css";

    private static String firstUnique = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\first_unique.css";
    private static String secondUnique = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\second_unique.css";
    private static String common = "C:\\Users\\Виктор\\Desktop\\CSS Parsing\\src\\main\\resources\\common.css";

    public static void main(String[] args) {
        formatFiles();
//        showStylechunksFromFile(firstFileFormatted);
//        showStylechunksFromFile(secondFileFormatted);
        showDifferentAndCommonStyleChunks(firstFileFormatted, secondFileFormatted);
    }

    public static void makeSpacesBetweenStyleChunks(String inputFile, String outputFile) {
        try (FileReader fileReader = new FileReader(inputFile);
             FileWriter fileWriter = new FileWriter(outputFile)) {
            int c;
            StringBuffer stringBuffer = new StringBuffer();

            int unclosedBrackets = 0;

            while ((c = fileReader.read()) != -1) {

                if ((char) c == '{') {
                    unclosedBrackets++;
                }

                if ((char) c == '}') {
                    unclosedBrackets--;
                    if (unclosedBrackets == 0) {
                        fileWriter.append((char) c);
                        fileWriter.append('\n');
                    } else {
                        fileWriter.append((char) c);
                    }
                } else {
                    fileWriter.append((char) c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void formatFiles() {
        makeSpacesBetweenStyleChunks(firstFile, firstFileFormatted);
        makeSpacesBetweenStyleChunks(secondFile, secondFileFormatted);
    }

    public static List<String> getStyleChunks(String filePath) {
        List<String> styleChunks = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String s;
            StringBuffer stringBuffer = new StringBuffer();
            while ((s = bufferedReader.readLine()) != null) {
                if (!s.isEmpty()) {
                    stringBuffer.append(s);
                } else {
                    styleChunks.add(stringBuffer.toString());
                    stringBuffer.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return styleChunks;
    }

    public static void showStylechunksFromFile(String filePath) {
        List<String> styleChunks = getStyleChunks(filePath);
        for (int i = 0; i < styleChunks.size(); i++) {
            System.out.println("Chunk №" + (i + 1) + " = " + styleChunks.get(i));
        }
        System.out.println();
    }

    public static void showDifferentAndCommonStyleChunks(String firstFile, String secondFile) {
        List<String> firstFileStyleChunks = getStyleChunks(firstFile);
        List<String> secondFileStyleChunks = getStyleChunks(secondFile);

        Set<String> commonStyleChunks = new HashSet<>();
        Set<String> uniqeStyleChunksFromFirstFile = new HashSet<>();
        Set<String> uniqeStyleChunksFromSecondFile = new HashSet<>();

        boolean found = false;

        //iterate through first file chunks
        //add common chunks only in this cycle
        for (String firstFileChunk : firstFileStyleChunks) {
            found = false;
            for (String secondFileChunk : secondFileStyleChunks) {
                if (secondFileChunk.equals(firstFileChunk)) {
                    commonStyleChunks.add(firstFileChunk);
                    found = true;
                }
            }
            if (found == false) {
                uniqeStyleChunksFromFirstFile.add(firstFileChunk);
            }
        }

        //iterate through second file chunks
        for (String secondFileChunk : secondFileStyleChunks) {
            found = false;
            for (String firstFileChunk : firstFileStyleChunks) {
                if (firstFileChunk.equals(secondFileChunk)) {
                    found = true;
                }
            }
            if (found == false) {
                uniqeStyleChunksFromSecondFile.add(secondFileChunk);
            }
        }

        try (PrintWriter out = new PrintWriter(firstUnique)) {
            System.out.println("Unique chunks for first file:");
            for (String chunk : uniqeStyleChunksFromFirstFile) {
                System.out.println(chunk);
                out.write(chunk);
                out.write('\n');
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(secondUnique)) {
            System.out.println("Unique chunks for second file:");
            for (String chunk : uniqeStyleChunksFromSecondFile) {
                System.out.println(chunk);
                out.write(chunk);
                out.write('\n');
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(common)) {
            System.out.println("Common chunks for both files:");
            for (String chunk : commonStyleChunks) {
                System.out.println(chunk);
                out.write(chunk);
                out.write('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
