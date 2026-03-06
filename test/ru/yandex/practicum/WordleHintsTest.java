package ru.yandex.practicum;

import org.junit.jupiter.api.*;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleHintsTest {

    static PrintWriter log;

    private String computeHints(String answer, String guess) {
        String normAnswer = answer.replace("ё", "е");
        String normGuess  = guess.replace("ё", "е");

        String[] answerChars = normAnswer.split("");
        String[] guessChars  = normGuess.split("");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < answerChars.length; i++) {
            if (guessChars[i].equals(answerChars[i])) {
                result.append("+");
            } else if (normAnswer.contains(guessChars[i])) {
                result.append("^");
            } else {
                result.append("-");
            }
        }
        return result.toString();
    }

    @BeforeAll
    static void initLog() {
        log = new PrintWriter(System.out, true);
        log.println("=== WordleHintsTest начало ===");
    }

    @Test
    void testCorrectGuessGivesAllPlus() {
        String hints = computeHints("абзац", "абзац");

        assertEquals("+++++", hints);
        log.println("Точное угадывание: " + hints);
    }

    @Test
    void testNoCommonLettersGivesAllMinus() {
        // "флюид" и "абрек" не имеют общих букв
        String hints = computeHints("флюид", "абрек");

        assertEquals("-----", hints);
        log.println("Нет общих букв: " + hints);
    }

    @Test
    void testCorrectPositionGivesPlus() {
        String hints = computeHints("абзац", "аборт");

        assertEquals('+', hints.charAt(0));
        assertEquals('+', hints.charAt(1));
        log.println("Подсказка 'аборт' vs 'абзац': " + hints);
    }

    @Test
    void testWrongPositionGivesCaret() {
        String hints = computeHints("абзац", "зааац");

        assertEquals('^', hints.charAt(0));
        log.println("Неверная позиция буквы 'з': " + hints);
    }

    @Test
    void testAbsentLetterGivesMinus() {
        // 'д' нет в "абзац"
        String hints = computeHints("абзац", "аддда");

        assertEquals('-', hints.charAt(1));
        log.println("Отсутствующая буква 'д': " + hints);
    }


    @Test
    @DisplayName("Смешанный результат: +, ^ и - в одной подсказке")
    void testMixedHints() {
        // Ответ "абзац", угадываем "аздба":
        // а[0] = а → +
        // з[1] ≠ б, но з есть в "абзац" → ^
        // д[2] нет в "абзац" → -
        // б[3] ≠ а, но б есть в "абзац" → ^
        // а[4] ≠ ц, но а есть в "абзац" → ^
        String hints = computeHints("абзац", "аздба");

        assertEquals('+', hints.charAt(0));
        assertEquals('^', hints.charAt(1));
        assertEquals('-', hints.charAt(2));
        assertEquals('^', hints.charAt(3));
        assertEquals('^', hints.charAt(4));
        log.println("Смешанный результат 'аздба' vs 'абзац': " + hints);
    }

    @AfterAll
    static void closeLog() {
        log.println("=== WordleHintsTest конец ===");
        log.flush();
    }
}
