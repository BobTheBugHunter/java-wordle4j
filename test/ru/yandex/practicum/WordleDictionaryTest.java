package ru.yandex.practicum;

import Loader.WordleDictionary;
import org.junit.jupiter.api.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    static PrintWriter log;
    WordleDictionary dictionary;

    @BeforeAll
    static void initLog() {
        log = new PrintWriter(System.out, true);
        log.println("=== WordleDictionaryTest начало ===");
    }

    @BeforeEach
    void createDictionary() {
        List<String> words = Arrays.asList("абзац", "аббат", "аборт");
        dictionary = new WordleDictionary(words);
    }

    @Test
    void testGetWordsReturnsCorrectList() {
        List<String> words = dictionary.getWords();

        assertEquals(3, words.size());
        assertTrue(words.contains("абзац"));
        assertTrue(words.contains("аббат"));
        log.println("getWords работает корректно");
    }

    @Test
    void testSetWordsReplacesOldList() {
        List<String> newWords = Arrays.asList("слово", "тест");
        dictionary.setWords(newWords);

        assertEquals(2, dictionary.getWords().size());
        assertTrue(dictionary.getWords().contains("слово"));
        assertFalse(dictionary.getWords().contains("абзац"));
        log.println("setWords заменил список");
    }

    @Test
    void testEmptyListDoesNotThrow() {
        assertDoesNotThrow(() -> {
            WordleDictionary empty = new WordleDictionary(Arrays.asList());
            assertTrue(empty.getWords().isEmpty());
        });
    }

    @AfterAll
    static void closeLog() {
        log.println("=== WordleDictionaryLoaderTest конец ===");
        log.flush();
    }
}
