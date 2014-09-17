package core;

import fun.Fun;
import fun.Fun1;
import fun.Predicate;
import ui_tests.TestData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                return new ColumnInfo(columns[0], columns[1], columns[2], columns[3], columns[4]);
            }
        });
        System.out.println(idsName);
        return idsName;
    }

    public static List<List<String>> dataRows(String[] linesSplited) {
        List<String> linesSplitedWithoutHashes = Fun.filter(Arrays.asList(linesSplited), new Predicate<String>() {
            @Override
            public boolean apply(String value) {
                return !value.contains("#");
            }
        });
        List<List<String>> dataTorows = Fun.map(linesSplitedWithoutHashes, new Fun1<String, List<String>>() {
            @Override
            public List<String> apply(String linesToSplit) {
                String[] rows = linesToSplit.split(";");
                return Arrays.asList(rows);
            }
        });
        return dataTorows;
    }
    public static List<Map<String, String>> forZip(List<Response.ColumnInfo> categoriesList, Response.PageInfo info) {
        final List<String> categoriesIds;
        //Id"s from "--schema" file
        categoriesIds = Fun.map(categoriesList, new Fun1<Response.ColumnInfo, String>() {
            @Override
            public String apply(Response.ColumnInfo columnInfo) {
                return columnInfo.id;
            }
        });
        System.out.println(categoriesIds);
        // list of data from *csv file
        final List<List<String>> dataRows;
        dataRows = Response.dataRows(HttpTransport.retrieve(TestData.dataUrl(info)));
        final List<Map<String, String>> s = Fun.map(dataRows, new Fun1<List<String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply(List<String> strings) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < categoriesIds.size(); i++) {
                    map.put(categoriesIds.get(i), strings.get(i));
                }
                return map;
            }
        });
        return s;
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
