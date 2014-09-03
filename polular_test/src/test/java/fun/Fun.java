package fun;

import java.util.ArrayList;
import java.util.List;

public class Fun {
    public static <I,O> List<O> map(Iterable<I> input, Fun1<I, O> map) {
        List<O> idName = new ArrayList<O>();

        for (I i : input) {
            idName.add(map.apply(i));
        }

        return idName;
    }

    public static <T> List<T> filter(Iterable<T> input, Predicate<T> p) {
        List<T> out = new ArrayList<T>();

        for (T anInput : input) {
            if (p.apply(anInput)) {
                out.add(anInput);
            }
        }

        return out;
    }

    public static <T> T find(Iterable<T> input, Predicate<T> p) {
        T out = null;

        for (T anInput : input) {
            if (p.apply(anInput)) {
                out = anInput;
                break;
            }
        }

        return out;
    }
}
