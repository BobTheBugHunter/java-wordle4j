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

    public WordleDictionary loader(String fileName) {
        List<String> words = new ArrayList<>();
        String line;
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            while (bf.ready()) {
                line = bf.readLine();
                if (line.length() == 5) {
                    if (line.contains("ё")) {
                        words.add(line.toLowerCase().replaceAll("ё","e"));
                    } else {
                        words.add(line.toLowerCase());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error opening file");
        }
        return new WordleDictionary(words);
    }
}
