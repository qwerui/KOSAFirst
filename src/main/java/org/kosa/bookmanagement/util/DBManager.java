/*
 * 작업자 : 장원석
 */

package org.kosa.bookmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    // db user password 로컬에 맞춰 변경
    private final String url = "jdbc:oracle:thin:@localhost:1521:XE";

    private final String user = "bookmanagement";
    private final String pass = "bookmanagement";

    private final String driverName = "oracle.jdbc.driver.OracleDriver";

    private static DBManager instance = new DBManager();

    private DBManager() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}