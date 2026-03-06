package ru.yandex.practicum;

import loader.WordleDictionary;
import loader.WordleDictionaryLoader;
import org.junit.jupiter.api.*;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    static PrintWriter log;
    WordleDictionaryLoader loader;

    @BeforeAll
    static void initLog() {
        log = new PrintWriter(System.out, true);
        log.println("=== WordleDictionaryLoaderTest начало ===");
    }

    @BeforeEach
    void createLoader() {
        loader = new WordleDictionaryLoader();
    }

    @Test
    void testLoadRealFileReturnsWords() {
        WordleDictionary result = loader.loader("words_ru.txt");

        assertNotNull(result);
        assertFalse(result.getWords().isEmpty());
        log.println("Загружено слов: " + result.getWords().size());
    }

    @Test
    void testAllWordsHaveLengthFive() {
        WordleDictionary result = loader.loader("words_ru.txt");

        for (String word : result.getWords()) {
            assertEquals(5, word.length(), "Слово не 5 букв: " + word);
        }
    }

    @Test
    void testMissingFileReturnsEmptyDictionary() {
        WordleDictionary result = loader.loader("несуществующий_файл.txt");

        assertNotNull(result);
        assertTrue(result.getWords().isEmpty());
        log.println("Несуществующий файл — пустой словарь, ОК");
    }

    @Test
    void testNoYoLetterInDictionary() {
        WordleDictionary result = loader.loader("words_ru.txt");

        for (String word : result.getWords()) {
            assertFalse(word.contains("ё"), "Нашлось слово с ё: " + word);
        }
    }

    @AfterAll
    static void closeLog() {
        log.println("=== WordleDictionaryLoaderTest конец ===");
        log.flush();
    }
}
