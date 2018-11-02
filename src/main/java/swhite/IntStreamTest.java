package swhite;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntStreamTest {
    public static void main(String args[]) {
        Map<Integer,Integer> m1 = IntStream.range(0,500).boxed().collect(Collectors.toMap(Function.identity(), i -> 0));
        Map<Integer, Boolean> m2 = IntStream.range(0, 3).boxed().collect(Collectors.toMap(Function.identity(), b -> Boolean.TRUE));
    }
}
