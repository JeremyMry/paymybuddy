package com.paymybuddy.app.data.config;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConfig {

    private Logger logger;

    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        String user = null;
        String password = null;
        String url = null;
        String driver = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("./src/main/resources/credentials.properties"));
            try {
                // Load a file.properties containing username/password and url of the db
                properties.load(fileInputStream);
                user = properties.getProperty("username");
                password = properties.getProperty("password");
                url = properties.getProperty("url");
                driver = properties.getProperty("driver");
                Class.forName(driver);
            } finally {
                fileInputStream.close();
            }
            // return DriverManager.getConnection(url, user, pass);
        } catch (FileNotFoundException e) {
            logger.error("Path for the credentials.properties file is incorrect");
        }
        logger.info("DB connection / SUCCESS");
        return DriverManager.getConnection(url + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", user, password);
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
