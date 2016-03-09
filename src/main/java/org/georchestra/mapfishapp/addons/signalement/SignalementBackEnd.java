package org.georchestra.mapfishapp.addons.signalement;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignalementBackEnd {

    private String id;
    private String table;
    private String jdbcUrl;
    private BasicDataSource basicDataSource;

    public SignalementBackEnd(String id, String table, String jdbcUrl) {
        this.id = id;
        this.table = table;
        this.jdbcUrl = jdbcUrl;

        this.basicDataSource = new BasicDataSource();
        this.basicDataSource.setDriverClassName("org.postgresql.Driver");
        this.basicDataSource.setTestOnBorrow(true);
        this.basicDataSource.setPoolPreparedStatements(true);
        this.basicDataSource.setMaxOpenPreparedStatements(-1);
        this.basicDataSource.setDefaultReadOnly(false);
        this.basicDataSource.setDefaultAutoCommit(true);
        this.basicDataSource.setUrl(jdbcUrl);
    }

    /**
     * Store signalement in database configured in this backend.
     * @param s signalement to store in this backend
     */
    public void store(Signalement s) throws SQLException {

        Connection connection = null;
        PreparedStatement st = null;
       // try {
            connection = this.basicDataSource.getConnection();
            st = connection.prepareStatement("INSERT INTO " + this.table + "(email, comment, map_context, login, latitude, longitude) VALUES (?,?,?,?,?,?)");
            st.setString(1, s.getEmail());
            st.setString(2, s.getComment());
            st.setString(3, s.getMapContext());
            st.setString(4, s.getLogin());
            st.setDouble(5, s.getLatitude());
            st.setDouble(6, s.getLongitude());
            st.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (st != null) try { st.close(); } catch (SQLException e) {LOG.error(e);}
//            if (connection != null) try { connection.close(); } catch (SQLException e) {LOG.error(e);}
//        }
    }

    @Override
    public String toString() {
        return "SignalementBackEnd{" +
                "id='" + id + '\'' +
                ", table='" + table + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                '}';
    }

    public JSONObject toJson() {
        JSONObject res = new JSONObject();
        res.put("id", id);
        res.put("description", this.toString());
        return res;
    }
}
