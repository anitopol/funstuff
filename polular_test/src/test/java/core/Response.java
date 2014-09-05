package core;

import fun.Fun;
import fun.Fun1;
import fun.Predicate;
import ui_tests.TestData;

import java.util.Arrays;
import java.util.List;

public class Response {
    public static List<PageInfo> index(String[] linesSplited) {
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
        return idName;
    }

    public static List<ColumnInfo> indexSchema(String[] columnsSplited) {
        List<String> columnsSplittedWithoutHashes = Fun.filter(Arrays.asList(columnsSplited), new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return !value.contains("#");
            }
        });

        List<ColumnInfo> idsName = Fun.map(columnsSplittedWithoutHashes, new Fun1<String, ColumnInfo>() {
            @Override
            public ColumnInfo apply(String linesToSplit) {
                String[] columns = linesToSplit.split(";");
                return new ColumnInfo(columns[0], columns[1], columns[2], columns[3],columns[4]);
            }
        });
        System.out.println(idsName);
    return idsName;
    }

    public static class PageInfo {
        public final String id;
        public final String title;

        public PageInfo(String id, String title) {
            this.id = id;
            this.title = title;

        }

        public static Predicate<PageInfo> nameEq(final String name) {
            return new Predicate<PageInfo>() {
                @Override
                public boolean apply(PageInfo value) {
                    return value.id.toLowerCase().contains(name);
                }
            };
        }

        @Override
        public String toString() {
            System.out.println(id);
            return "PageInfo{id='" + id + '\'' + ", title='" + title + '\'' + '}';

        }
    }

    public static class ColumnInfo {
        public final String id;
        public final String title;
        public final String role;
        public final String align;
        public final String width;

        public ColumnInfo(String id, String title, String role, String align, String width) {
            this.id = id;
            this.title = title;
            this.role = role;
            this.align = align;
            this.width = width;
        }
        /*@Override
        public String toString() {
            System.out.println(id);
            return "PageInfo{id='" + id + '\'' + ", title='" + title + '\'' + '}';

        }*/
    }


}
