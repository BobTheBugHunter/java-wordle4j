package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionary;
import loader.WordleDictionary;
import loader.WordleDictionaryLoader;
import org.junit.jupiter.api.*;

import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    static PrintWriter log;
    static WordleDictionary dictionary;

    WordleGame game;

    @BeforeAll
    static void loadDictionary() {
        log = new PrintWriter(System.out, true);
        log.println("=== WordleGameTest начало ===");

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        dictionary = loader.loader("words_ru.txt");
        log.println("Словарь загружен, слов: " + dictionary.getWords().size());
    }

    @BeforeEach
    void createGame() {
        game = new WordleGame(dictionary);
    }


    @Test
    void testRandomWordIsFromList() {
        List<String> words = Arrays.asList("абзац", "аббат", "аборт");
        String result = game.getRandomWord(words);

        assertTrue(words.contains(result), "Вернулось слово не из списка: " + result);
        log.println("getRandomWord вернул: " + result);
    }

    @Test
    void testRandomWordSingleElement() {
        List<String> words = Arrays.asList("абзац");
        String result = game.getRandomWord(words);

        assertEquals("абзац", result);
    }

    @Test
    void testValidWordReturnsTrue() throws WordNotFoundInDictionary {
        String word = dictionary.getWords().get(0);
        boolean result = game.isInputCorrect(dictionary, word);

        assertTrue(result);
        log.println("Слово принято: " + word);
    }

    @Test
    void testUnknownWordThrowsException() {
        assertThrows(WordNotFoundInDictionary.class, () -> {
            game.isInputCorrect(dictionary, "ааааа");
        });
    }

    @Test
    void testDigitsReturnFalse() throws WordNotFoundInDictionary {
        boolean result = game.isInputCorrect(dictionary, "12345");

        assertFalse(result);
    }

    @Test
    void testLatinLettersReturnFalse() throws WordNotFoundInDictionary {
        boolean result = game.isInputCorrect(dictionary, "hello");

        assertFalse(result);
    }

    @Test
    void testEmptyStringReturnsFalse() throws WordNotFoundInDictionary {
        boolean result = game.isInputCorrect(dictionary, "");

        assertFalse(result);
    }

    @Test
    void testStringWithSpacesReturnsFalse() throws WordNotFoundInDictionary {
        boolean result = game.isInputCorrect(dictionary, "а б в г д");

        assertFalse(result);
    }


    @Test
    void testFilterWithEmptyLog() {
        LinkedHashMap<String, String> log = new LinkedHashMap<>();

        assertDoesNotThrow(() -> game.filterDictionary(log, dictionary));
    }

    @Test
    void testFilterWithNullValue() {
        LinkedHashMap<String, String> log = new LinkedHashMap<>();
        log.put("абзац", null);

        assertDoesNotThrow(() -> game.filterDictionary(log, dictionary));
    }

    @Test
    void testFilterWithRealGuesses() {
        LinkedHashMap<String, String> log = new LinkedHashMap<>();
        log.put("аббат", "-+---");
        log.put("абрис", "++--^");

        List<String> smallWords = Arrays.asList("абзац", "аббат", "аборт", "абрек", "абрис");
        WordleDictionary smallDict = new WordleDictionary(smallWords);

        assertDoesNotThrow(() -> game.filterDictionary(log, smallDict));
        this.log.println("filterDictionary с 2 ходами — ОК");
    }

    @AfterAll
    static void closeLog() {
        log.println("=== WordleGameTest конец ===");
        log.flush();
    }
}
