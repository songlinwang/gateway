import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wsl
 * @date 2019/4/26
 */
public class TestJDK8 {

    @FunctionalInterface
    interface Converter<F, T> {
        T convert(F from);
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("a", "b", "c");
        Collections.sort(names, (String a, String b) -> {
            return a.compareTo(b);
        });
        Collections.sort(names, (a, b) -> {
            return b.compareTo(a);
        });

        // filter
        names.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);

        // Match
        boolean anyStartWith = names.stream().anyMatch(s -> s.startsWith("a"));
        System.out.println(anyStartWith);

        Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
        Integer converted = converter.convert("1234");
        System.out.println(converted);

        Converter<String, Integer> converter1 = Integer::valueOf;
        Integer converteda = converter1.convert("1234");
        System.out.println(converteda);
    }
}
