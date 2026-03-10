package loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final int MAX_LENGTH_OF_WORDS = 5;

    public WordleDictionary loader(String fileName, PrintWriter pw) {
        List<String> words = new ArrayList<>();
        String line;
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            while (bf.ready()) {
                line = bf.readLine();
                if (line.length() == MAX_LENGTH_OF_WORDS) {
                    if (line.contains("ё")) {
                        words.add(line.toLowerCase().replaceAll("ё","е"));
                    } else {
                        words.add(line.toLowerCase());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            pw.println("File not found");
            System.out.println("File not found");
        } catch (IOException e) {
            pw.println("Error opening file");
            System.out.println("Error opening file");
        }
        return new WordleDictionary(words);
    }
}
