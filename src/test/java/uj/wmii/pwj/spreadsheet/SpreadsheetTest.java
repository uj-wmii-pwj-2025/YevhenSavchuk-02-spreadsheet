package uj.wmii.pwj.spreadsheet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadsheetTest {

    private static final String[] listFiles = {"example", "numbers", "two-timer", "walker", "ops", "ref2expr"};

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("spreadsheetInput")
    void checkSpreadsheet(String testName, String[][] input, String[][] expected) {
        String[][] result = new Spreadsheet().calculate(input);
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> spreadsheetInput() {
        return Stream.of(
            Arrays.stream(listFiles)
                .map(SpreadsheetTest::readTestCase)
                .toArray(Arguments[]::new)
        );
    }

    static Arguments readTestCase(String caseName) {
        return Arguments.of(
                caseName,
                readSpreadsheet(caseName + ".txt"),
                readSpreadsheet(caseName + "-result.txt"));
    }

    static String[][] readSpreadsheet(String name) {
        InputStream input = SpreadsheetTest.class.getResourceAsStream(name);
        if (input == null)
            return new String[][] {{"Invalid test case: " + name}};
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        List<String> lines = br.lines().toList();
        String[][] result = new String[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            result[i] = buildRow(lines.get(i));
        }
        return result;
    }

    static String[] buildRow(String line) {
        List<String> result = new ArrayList<>();
        boolean inParenthesis = false;
        StringBuilder current = new StringBuilder();
        for (var c: line.toCharArray()) {
            if (c == '(') inParenthesis = true;
            else if (c == ')') inParenthesis = false;
            if (c == ',' && !inParenthesis) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

}
