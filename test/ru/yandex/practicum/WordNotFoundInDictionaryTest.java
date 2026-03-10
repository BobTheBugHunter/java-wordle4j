package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionary;
import loader.WordleDictionary;
import loader.WordleDictionaryLoader;
import org.junit.jupiter.api.*;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordNotFoundInDictionaryTest {

    static PrintWriter log;

    @BeforeAll
    static void initLog() {
        log = new PrintWriter(System.out, true);
        log.println("=== WordNotFoundInDictionaryTest начало ===");
    }

    @Test
    void testExceptionKeepsMessage() {
        WordNotFoundInDictionary ex = new WordNotFoundInDictionary("слово не найдено");

        assertEquals("слово не найдено", ex.getMessage());
        log.println("Сообщение сохранено: " + ex.getMessage());
    }

    @Test
    void testExceptionIsSubclassOfException() {
        WordNotFoundInDictionary ex = new WordNotFoundInDictionary("ошибка");

        assertTrue(ex instanceof Exception);
    }

    @Test
    void testIsInputCorrectThrowsForUnknownWord() throws Exception {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        WordleDictionary dictionary = loader.loader("words_ru.txt", log);
        WordleGame game = new WordleGame(dictionary);

        assertThrows(WordNotFoundInDictionary.class, () -> {
            game.isInputCorrect(dictionary, "ааааа", log);
        });
        log.println("Исключение выброшено для несловарного слова — ОК");
    }

    @AfterAll
    static void closeLog() {
        log.println("=== WordNotFoundInDictionaryTest конец ===");
        log.flush();
    }
}
