package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionary;
import loader.WordleDictionary;
import loader.WordleDictionaryLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {


        try (PrintWriter pw = new PrintWriter(new FileWriter("log.txt", true))) {


            WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
            WordleDictionary wordleDictionary = wordleDictionaryLoader.loader("words_ru.txt", pw);
            WordleGame wordleGame = new WordleGame(wordleDictionary);
            game(wordleGame, pw);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void game(WordleGame wordleGame, PrintWriter pw) {
        final int FIRST_STEP = 6;
        try (Scanner scanner = new Scanner(System.in)) {
            LinkedHashMap<String, String> log = new LinkedHashMap<>();
            List<String> wordsFromUser = new ArrayList<>();
            boolean isWin = false;
            System.out.println("Загадано слово из 5 букв. У вас 6 попыток.");
            int steps = 6;
            int count = 0;
            String word = wordleGame.getRandomWord(wordleGame.getDictionary().getWords());
            char[] symbolsFromPC = word.toCharArray();
            System.out.println(word);
            StringBuilder symbols = new StringBuilder();
            while (steps != 0) {
                try {
                System.out.println("Введите слово из 5 букв: ");
                String wordFromUser = scanner.nextLine().trim().toLowerCase();
                if (wordFromUser.isEmpty() && !log.isEmpty()) {
                    wordleGame.filterDictionary(log, wordleGame.getDictionary(), pw);
                    continue;
                }
                if (!wordleGame.isInputCorrect(wordleGame.getDictionary(), wordFromUser, pw)) {
                    continue;
                }

                if (wordFromUser.contains("ё")) {
                    wordFromUser = wordFromUser.replace("ё", "е");
                }

                if (wordsFromUser.contains(wordFromUser) && !wordsFromUser.isEmpty()) {
                    System.out.println("Это слово уже было вписано!");
                    continue;
                }

                if (word.equals(wordFromUser) && steps == FIRST_STEP) {
                    System.out.println("Поздравляю! Вы угадали слово " + word + " за 1 попытку!");
                    isWin = true;
                    break;
                }

                wordsFromUser.add(wordFromUser);

                appendSymbolsFromUser(word, wordFromUser, symbolsFromPC, symbols);


                if (word.equals(wordFromUser)) {
                    System.out.println("Результат: " + symbols.toString());
                    System.out.println("Поздравляю! Вы угадали слово " + word + " за " + count + " попытки!");
                    symbols.delete(0, symbols.length());
                    isWin = true;
                    break;
                }
                log.put(wordFromUser, symbols.toString());
                steps--;
                count++;
                System.out.println("Результат: " + symbols.toString());
                System.out.println("Осталось попыток:  " + steps);
                symbols.delete(0, symbols.length());
            } catch (WordNotFoundInDictionary ex) {
                    pw.println("Введенное слово не существует в словаре");
                    System.out.println("Введенное слово не существует в словаре");
                    continue;
                }
            }
            if (!isWin) {
                System.out.println("К сожалению в проиграли :(\n загаданное слово: " + word);
            }
        }
    }

    public static void appendSymbolsFromUser(String word, String wordFromUser,
                                             char[] symbolsFromPC, StringBuilder symbols) {

        String[] symbolsFromUser = wordFromUser.split("");
        for (int i = 0; i < symbolsFromPC.length; i++) {
            if (symbolsFromUser[i].equals(symbolsFromPC[i])) {
                symbols.append("+");
            } else if (word.contains(symbolsFromUser[i])) {
                symbols.append("^");
            } else {
                symbols.append("-");
            }
        }
    }


}
