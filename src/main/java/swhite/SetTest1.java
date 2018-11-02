package swhite;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class SetTest1 {
    public static void main(String args[]) {
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        set.add("D");
        set.add("E");
        set.add("F");
        set.add("G");
        set.add("H");

        for (int i = 0; i < 100; i++) {
            System.out.println(randomElement(set));
        }
    }

    private static String randomElement(Set<String> set) {
        Random rand = new Random();
        int index = rand.nextInt(set.size());
        System.out.println(index);
        Iterator<String> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }
}

