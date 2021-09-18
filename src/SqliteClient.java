import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SqliteClient {
    private Connection connection = null;

    public SqliteClient(String database) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + database);
    }

    public void close() throws SQLException {
        if(connection != null){
            connection.close();
        }
    }

    //it's templete function for quering data
    public void queryResult(String query) throws SQLException {
        //Query get Data
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        for(int i = 1; i <= columns; i++){
            System.out.print(rsmd.getColumnName(i) + " ");
        }
        System.out.println();
        while (rs.next()){
            for(int i = 1; i<=columns;i++){
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }
    }

    public void queryUpdate(String query) throws SQLException {
        // INSERT, UPDATE, or DELETE
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate(query);
    }

    /********************************************************************/

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase().replaceAll("đ", "d");
    }

    //Location.db
    public void normalizeProvinceData() throws SQLException {
        String query = "select province from location";

        List<String> province_nor = new ArrayList<>();

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        System.out.println();
        while (rs.next()){
            for(int i = 1; i<=columns;i++){
                System.out.print(rs.getString(i) + " ");
                province_nor.add(deAccent(rs.getString(i)));
            }
            System.out.println();
        }


        int i = 1;
        for (String province_nor_element: province_nor) {
            query = "update location set province_nor = " + "\"" + province_nor_element + "\"" + " where _rowid_ = " + i++;
            System.out.println(query);
            //statement.executeUpdate(query);
        }
    }
}
