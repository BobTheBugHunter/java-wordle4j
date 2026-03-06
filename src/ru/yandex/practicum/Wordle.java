package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionary;
import loader.WordleDictionary;
import loader.WordleDictionaryLoader;

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

        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        WordleDictionary wordleDictionary = wordleDictionaryLoader.loader("words_ru.txt");
        WordleGame wordleGame = new WordleGame(wordleDictionary);
        game(wordleGame);

    }

    public static void game(WordleGame wordleGame) {
        try (Scanner scanner = new Scanner(System.in)) {
            LinkedHashMap<String, String> log = new LinkedHashMap<>();
            List<String> wordsFromUser = new ArrayList<>();
            boolean isWin = false;
            System.out.println("Загадано слово из 5 букв. У вас 6 попыток.");
            int steps = 6;
            int count = 0;
            int plusCount = 0;
            String word = wordleGame.getRandomWord(wordleGame.getDictionary().getWords());
            String[] symbolsFromPC = word.split("");
            System.out.println(word);
            StringBuilder symbols = new StringBuilder();
            while (steps != 0) {
                System.out.println("Введите слово из 5 букв: ");
                String wordFromUser = scanner.nextLine().trim().toLowerCase();
                if (wordFromUser.isEmpty() && plusCount != 0) {
                    wordleGame.filterDictionary(log, wordleGame.getDictionary());
                    continue;
                }
                if (!wordleGame.isInputCorrect(wordleGame.getDictionary(), wordFromUser)) {
                    continue;
                }

                if (wordsFromUser.contains(wordFromUser) && !wordsFromUser.isEmpty()) {
                    System.out.println("Это слово уже было вписано!");
                    continue;
                }

                if (word.equals(wordFromUser)) {
                    System.out.println("Поздравляю! Вы угадали слово " + word + " за 1 попытку!");
                    isWin = true;
                    break;
                }

                wordsFromUser.add(wordFromUser);

                if (wordFromUser.contains("ё")) {
                    wordFromUser = wordFromUser.replace("ё", "е");
                }

                String[] symbolsFromUser = wordFromUser.split("");
                for (int i = 0; i < symbolsFromPC.length; i++) {
                    if (symbolsFromUser[i].equals(symbolsFromPC[i])) {
                        symbols.append("+");
                        plusCount++;
                    } else if (word.contains(symbolsFromUser[i])) {
                        symbols.append("^");
                        plusCount++;
                    } else {
                        symbols.append("-");
                    }
                }
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

            }
            if (!isWin) {
                System.out.println("К сожалению в проиграли :(\n загаданное слово: " + word);
            }
        } catch (WordNotFoundInDictionary e) {
            System.out.println("Введенное слово не существует в словаре");
        }
    }


}
