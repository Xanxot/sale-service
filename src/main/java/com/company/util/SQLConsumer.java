package com.company.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T t, PreparedStatement stmt) throws SQLException;
}
