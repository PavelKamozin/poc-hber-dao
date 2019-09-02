package softserve.hibernate.com.config;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public final static class Controller {
        public static final String DEFAULT_START_PAGE = "1";
        public static final String DEFAULT_PAGE_SIZE = "20";
    }

    public final static class SQL {
        public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
        public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
}
