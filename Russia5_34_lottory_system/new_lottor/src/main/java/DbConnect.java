
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConnect {
    public static final String DB_URL = "jdbc:sqlite:lotto.db";

    public static final String DISK_DB_URL = "jdbc:sqlite:";


    private String dbUrl = DB_URL;


    public void createGenResultsTable() {

        try (final Connection connection = DriverManager.getConnection(dbUrl)) {

            try( final Statement stmt = connection.createStatement() ){
                stmt.executeUpdate( "CREATE TABLE betNumbers(day INTEGER, N1 INTEGER, N2 INTEGER, N3 INTEGER )" );
                System.out.println( "Success creating generated numbers table!" );
            }catch( SQLException e ){
                System.err.println( e.getMessage() );
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createResultsTable() {

        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            try( final Statement stmt = connection.createStatement() ){
                stmt.executeUpdate( "CREATE TABLE internetResults(id INTEGER PRIMARY KEY AUTOINCREMENT, N1 INTEGER, N2 INTEGER, N3 INTEGER , N4 INTEGER, N5 INTEGER )" );
                System.out.println( "Success creating results table!" );
            }catch( SQLException e ){
                System.err.println( e.getMessage() );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteBetTable() {
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            try( final Statement stmt = connection.createStatement() ){
                stmt.executeUpdate( "DROP TABLE IF EXISTS betNumbers" );
                System.out.println( "Success deleting betNumbers table!" );
            }catch( SQLException e ){
                System.err.println( e.getMessage() );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteInternetTable() {
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            try( final Statement stmt = connection.createStatement() ){
                stmt.executeUpdate( "DROP TABLE IF EXISTS internetResults" );
                System.out.println( "Success deleting internetResults table!" );
            }catch( SQLException e ){
                System.err.println( e.getMessage() );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void insertResultsData(int day,  List<List<Integer>> resultOfGenerator) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            System.out.println("Connected to database ");
            if(resultOfGenerator.size() > 0) {

                for(int h=0; h<resultOfGenerator.size(); h++){
                    insertBetData(connection, day, resultOfGenerator.get(h).get(0), resultOfGenerator.get(h).get(1),
                            resultOfGenerator.get(h).get(2));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public void insertInternetData(List<List<Integer>> resultOfGenerator) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            System.out.println("Connected to database ");
            if(resultOfGenerator.size() > 0) {

                for(int h=0; h<resultOfGenerator.size(); h++){
                    insertInternetResultsData(connection, resultOfGenerator.get(h).get(0), resultOfGenerator.get(h).get(1),
                            resultOfGenerator.get(h).get(2), resultOfGenerator.get(h).get(3), resultOfGenerator.get(h).get(4));
                }
                System.out.println("Row added!");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertBetData(final Connection connection,int day, Integer n1, Integer n2, Integer n3) //take args from terminal as well for insertion
            throws SQLException
    {

        try( final PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO betNumbers(day, N1, N2, N3) VALUES (?, ?, ?, ?)"
        )){
            stmt.setInt( 1,day);
            stmt.setInt( 2, n1); //size of objects
            stmt.setInt( 3, n2); // make sure position is not greater than the world
            stmt.setInt( 4, n3);

            final boolean gotAResultSet = stmt.execute();
            if( gotAResultSet ){
                throw new RuntimeException( "Unexpectedly got a SQL resultSet." );
            }
        }


        System.out.println("Row added!");

    }


    private void insertInternetResultsData(final Connection connection, Integer n1, Integer n2, Integer n3, Integer n4, Integer n5) //take args from terminal as well for insertion
            throws SQLException
    {

        try( final PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO internetResults(N1, N2, N3, N4, N5) VALUES (?, ?, ?, ?, ?)"
        )){
            stmt.setInt( 1, n1); //size of objects
            stmt.setInt( 2, n2); // make sure position is not greater than the world
            stmt.setInt( 3, n3);
            stmt.setInt( 4, n4); // make sure position is not greater than the world
            stmt.setInt( 5, n5);

            final boolean gotAResultSet = stmt.execute();
            if( gotAResultSet ){
                throw new RuntimeException( "Unexpectedly got a SQL resultSet." );
            }
        }

    }

    public  List<List<Integer>>  displayNumbers(int day1) {
        List<List<Integer>> newList = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            System.out.println("Connected to database");

            try (final PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM betNumbers WHERE day = ?"
            )) {
                stmt.setInt(1, day1);
                ResultSet results = stmt.executeQuery();

                while (results.next()) {
                    List<Integer> firstList = new ArrayList<>();

                    Integer n1 = results.getInt("N1");
                    Integer n2 = results.getInt("N2");
                    Integer n3 = results.getInt("N3");

                    firstList.add(n1);
                    firstList.add(n2);
                    firstList.add(n3);
                    newList.add(firstList);

                }

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return  newList;
    }

    public  List<List<Integer>>  displayInternetResults() {
        List<List<Integer>> newList = new ArrayList<>();
        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
            System.out.println("Connected to database");

            try (final PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM internetResults"
            )) {
                ResultSet results = stmt.executeQuery();

                while (results.next()) {
                    List<Integer> firstList = new ArrayList<>();

                    Integer n1 = results.getInt("N1");
                    Integer n2 = results.getInt("N2");
                    Integer n3 = results.getInt("N3");
                    Integer n4 = results.getInt("N4");
                    Integer n5 = results.getInt("N5");

                    firstList.add(n1);
                    firstList.add(n2);
                    firstList.add(n3);
                    firstList.add(n4);
                    firstList.add(n5);
                    newList.add(firstList);

                }

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return  newList;
    }

    public void deleteRowForBet(Connection connection,int day ) throws SQLException {
        try (final PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM betNumbers WHERE day = ?"
        )) {
            stmt.setInt(1, day);
            stmt.execute();
        }

    }

}



