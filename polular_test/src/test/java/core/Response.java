package core;

import fun.Fun;
import fun.Fun1;
import fun.Predicate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by a on 01.09.14.
 */
public class Response {
    public static void responseBodyDataParsing(String[] linesSplited) {
        List<String> linesSplitedWithoutHashes = Fun.filter(Arrays.asList(linesSplited), new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return !value.contains("#");
            }
        });

        List<PageInfo> idName = Fun.map(linesSplitedWithoutHashes, new Fun1<String, PageInfo>() {
            @Override
            public PageInfo apply(String linesToSplit) {
                String[] columns = linesToSplit.split(";");
                return new PageInfo(columns[0], columns[1]);
            }
        });
        for (int i = 0; i < idName.size(); i++) {
            System.out.println(idName.get(i));
        }
        System.out.println("idName.get(1).id = " + Fun.filter(idName, new Predicate<PageInfo>() {
                    @Override
                    public boolean apply(PageInfo value) {
                        return value.id.equals("liczebniki");
                    }
                }).get(0)
        );
    }
    public static class PageInfo {
        public final String id;
        public final String title;

        public PageInfo(String id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public String toString() {
            return "PageInfo{id='" + id + '\'' + ", title='" + title + '\'' + '}';
        }
    }
}
