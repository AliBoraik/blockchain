package service;

import Models.BlockModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbWorker {
    private Connection connection;

    public DbWorker() {
        DataBase.getInstance();
        connection = DataBase.getConnection();
        addTables();
    }

    private void addTables() {
        try {

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS blocks(" +
                    "prevhash VARCHAR(64)," +
                    "signature VARCHAR(256)," +
                    "data TEXT," +
                    "name TEXT," +
                    "ts VARCHAR," +
                    "publickey TEXT" +
                    ");");

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Keys(" +
                    "privateKey TEXT," +
                    "publicKey TEXT" +
                    ");");

        } catch (SQLException ignored) {
        }
    }

    public void addBlockModel(BlockModel blockModel) {
        try {
            connection
                    .createStatement()
                    .execute("insert  into  blocks" +
                            "(prevhash,signature,data,name,ts,publickey) values " +
                            "('" + blockModel.getPrevhash() + "','" + blockModel.getSignature() + "'," +
                            "'" + blockModel.getData().getData() + "','" + blockModel.getData().getName() + "'," +
                            "'" + blockModel.getTs() + "','" + blockModel.getPublickey() + "') ON CONFLICT DO NOTHING ");
        } catch (SQLException ignored) {
        }
    }

    public String[] getKeys() {
        String[] keys = new String[2];
        try {
            ResultSet rs = connection.createStatement()
                    .executeQuery("SELECT k.privateKey, k.publicKey FROM keys k");

            while (rs.next()) {
                keys[0] = rs.getString(1);
                keys[1] = rs.getString(2);

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public void addKeys(String privateKey, String publicKey) {
        try {
            connection
                    .createStatement()
                    .execute("insert into keys" +
                            "(privatekey,publickey) values " +
                            "('" + privateKey + "','" + publicKey + "')");

        } catch (SQLException ignored) {
        }
    }

    public void clean() {
        try {
            connection
                    .createStatement()
                    .execute("DELETE FROM blocks; ");
        } catch (SQLException ignored) {
        }
    }


    public BlockModel[] getBlocks() {
        return null;
    }
}
