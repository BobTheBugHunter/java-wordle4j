package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionary;
import loader.WordleDictionary;

import java.io.PrintWriter;
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

    public boolean isInputCorrect(WordleDictionary dictionary, String input, PrintWriter pw) throws WordNotFoundInDictionary {
        if (!input.matches("[а-яА-ЯёЁ]+")) {
            pw.println("Допускаются только символы русского алфавита!");
            System.out.println("Допускаются только символы русского алфавита!");
            return false;
        } else if (!dictionary.getWords().contains(input)) {
            throw new WordNotFoundInDictionary("Введенное слово не существует в словаре");
        } else {
            return true;
        }
    }

    public void filterDictionary(LinkedHashMap<String, String> log, WordleDictionary dictionary1, PrintWriter pw) {
        List<String> valueWords = new ArrayList<>(log.values());
        List<String> keyWords = new ArrayList<>(log.keySet());
        List<String> words = new ArrayList<>(dictionary1.getWords());

        Set<String> allMinus = new HashSet<>();
        Set<String> allPlus = new HashSet<>();
        Set<String> allIDK = new HashSet<>();
        HashMap<Integer, String> guessWord = new HashMap<>();

        String[] valueString;
        String[] keyString;
        for (int i = 0; i < valueWords.size(); i++) {
            if (valueWords.get(i) == null) continue;
            valueString = valueWords.get(i).split("");
            keyString = keyWords.get(i).split("");
            for (int j = 0; j < valueString.length; j++) {
                if (valueString[j].equals("+")) {
                    allPlus.add(keyString[j]);
                    guessWord.put(j, keyString[j]);
                } else if (valueString[j].equals("-")) {
                    if (!allPlus.contains(keyString[j])) {
                        allMinus.add(keyString[j]);
                    }
                } else if (valueString[j].equals("^")) {
                    allIDK.add(keyString[j]);
                }
            }
        }

        words.removeIf(word -> allMinus.stream().anyMatch(word::contains));
        words.removeIf(word -> allIDK.stream().anyMatch(letter -> !word.contains(letter)));
        words.removeIf(word -> {
            String[] letters = word.split("");
            return guessWord.entrySet().stream()
                    .anyMatch(e -> !letters[e.getKey()].equals(e.getValue()));
        });

        if (words.isEmpty()) {
            pw.println("Нет подходящих слов для подсказки");
            System.out.println("Нет подходящих слов для подсказки");
        } else {
            Random rand = new Random();
            System.out.println("Возможное слово: " + words.get(rand.nextInt(words.size())));
        }
    }
}