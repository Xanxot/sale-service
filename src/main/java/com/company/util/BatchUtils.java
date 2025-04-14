package com.company.util;

import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;

@UtilityClass
public class BatchUtils {
    public static <T> void insertBatch(
            List<T> items,
            int batchSize,
            PreparedStatement stmt,
            SQLConsumer<T> binder,
            String logLabel,
            Logger log
    ) throws SQLException {
        int count = 0;
        for (T item : items) {
            binder.accept(item, stmt);
            stmt.addBatch();
            count++;

            if (count % batchSize == 0) {
                stmt.executeBatch();
                log.info("Insert {} row/s {}", count, logLabel);
            }
        }

        stmt.executeBatch();
        log.info("Finally insert {} row/s {}", count, logLabel);
    }
}
