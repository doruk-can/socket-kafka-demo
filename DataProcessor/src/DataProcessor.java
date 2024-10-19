import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataProcessor {


    public static List<Integer> processData(String[][] data) {
        List<Integer> allIntegers = Arrays.stream(data)
                .filter(Objects::nonNull)
                .flatMap(innerArray -> Arrays.stream(innerArray))
                .filter(Objects::nonNull)
                .map(DataProcessor::parseInteger)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Integer> resultList = new ArrayList<>();

        for (int i = 0; i + 2 < allIntegers.size(); i += 3) {
            int sum = allIntegers.get(i) + allIntegers.get(i + 1) + allIntegers.get(i + 2);
            if (sum >= 90) {
                resultList.add(allIntegers.get(i));
                resultList.add(allIntegers.get(i + 1));
                resultList.add(allIntegers.get(i + 2));
            }
        }

        return resultList;
    }

    private static Integer parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String[][] data = new String[][]{
                {"0", "s1", null, "35", "90", "60"},
                {"ttt", null, null , "15"},
                {"75", "95", "0", "0", null, "ssss", "0", "-15"},
                {"25", "fgdfg", "", "105", "dsfdsf", "-5"}
        };

        List<Integer> result = processData(data);
        System.out.println("Result: " + result);
    }
}