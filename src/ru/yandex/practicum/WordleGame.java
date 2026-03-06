package ru.yandex.practicum;

import Exceptions.WordNotFoundInDictionary;
import Loader.WordleDictionary;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getRandomWord(List<String> words) {
        Random rand = new Random();
        int index = rand.nextInt(words.size());
        return words.get(index);
    }


    public boolean isInputCorrect(WordleDictionary dictionary, String input) throws WordNotFoundInDictionary {
        if (!input.matches("[а-яА-ЯёЁ]+")) {
            System.out.println("Допускаются только символы русского алфавита!");
            return false;
        } else if (!dictionary.getWords().contains(input)) {
            throw new WordNotFoundInDictionary("Введенное слово не существует в словаре");
        } else {
            return true;
        }
    }

    public void filterDictionary(LinkedHashMap<String, String> log, WordleDictionary dictionary1) {

        try {


            List<String> valueWords = new ArrayList<>(log.values());

            List<String> keyWords = new ArrayList<>(log.keySet());

            List<String> words = new ArrayList<>(dictionary1.getWords());

            List<String> allMinus = new ArrayList<>();
            List<String> allPlus = new ArrayList<>();
            List<String> allIDK = new ArrayList<>();
            HashMap<Integer, String> guessWord = new HashMap<>();

            String[] valueString;
            String[] keyString;
            for (int i = 0; i < valueWords.size(); i++) {
                valueString = valueWords.get(i).split("");
                keyString = keyWords.get(i).split("");
                for (int j = 0; j < valueString.length; j++) {
                    if (valueString[j].equals("+")) {
                        allPlus.add(keyString[j]);
                        guessWord.put(j, keyString[j]);
                    }
                }
                for (int j = 0; j < valueString.length; j++) {
                    if (valueString[j].equals("-")) {
                        if (!allPlus.contains(keyString[j])) {
                            allMinus.add(keyString[j]);
                        }
                    } else if (valueString[j].equals("^")) {
                        allIDK.add(keyString[j]);
                    }
                }

            }
            for (int j = words.size() - 1; j >= 0; j--) {
                String word = words.get(j);
                for (String minus : allMinus) {
                    if (word.contains(minus)) {
                        words.remove(j);
                        break;
                    }
                }
            }

            for (int j = words.size() - 1; j >= 0; j--) {
                String word = words.get(j);
                boolean hasAllIDK = true;
                for (String idk : allIDK) {
                    if (!word.contains(idk)) {
                        hasAllIDK = false;
                        break;
                    }
                }
                if (!hasAllIDK) {
                    words.remove(j);
                }
            }


            for (int i = words.size() - 1; i >= 0; i--) {
                String word = words.get(i);
                String[] position = word.split("");
                boolean matchesGuess = true;
                for (int j = 0; j < position.length; j++) {
                    String guessedLetter = guessWord.get(j);
                    if (guessedLetter != null && !position[j].equals(guessedLetter)) {
                        matchesGuess = false;
                        break;
                    }
                }
                if (!matchesGuess) {
                    words.remove(i);
                }
            }

            if (words.isEmpty()) {
                System.out.println("Нет подходящих слов для подсказки");
            } else {
                Random rand = new Random();
                int index = rand.nextInt(words.size());
                System.out.println("Возможное слово: " + words.get(index));
            }
        } catch (NullPointerException e) {
            System.out.println("Возникла ошибка с null!");
        }
    }
}