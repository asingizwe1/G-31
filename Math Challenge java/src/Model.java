import java.sql.*;

public class Model {
    public static Connection createConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mathcontest";
        String username="root";
        String password = "";

        return DriverManager.getConnection(url,username,password);
    }
    //Checks if a given school registration number exists in the School table.
    //checking if a given school registration number is valid
    public static boolean checkRegNo(pupil_detail pupil){

        try(Connection con =Model.createConnection()){
            String sql ="SELECT 1 FROM schoolrep where School_reg_no = ?";

            PreparedStatement st =con.prepareStatement(sql);
            st.setString(1,pupil.getSchoolRegNo());
            ResultSet rs = st.executeQuery();//: Executes the query and retrieves the result.
            // Checks if the result set is empty. If it is, the school registration number does not exist.
            if(!rs.isBeforeFirst()){
                return false;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //checking if a given student is already registered basing on their name and date of birth
    public static boolean checkStudentRegistration(pupil_detail pupil){
        String sql = "SELECT 1 FROM pupil where name = ? and date_of_birth = ?";
        PreparedStatement st= null;
        ResultSet rs = null;
        boolean isRegistered = false;
        try(Connection con = Model.createConnection()){
            st = con.prepareStatement(sql);
            st.setString(1,pupil.getName());//: Sets the name parameter.
            st.setString(2,pupil.getDate_of_birth());
            rs = st.executeQuery();// Executes the query and retrieves the result.
            isRegistered= rs.isBeforeFirst();//Checks if the result set is not empty, indicating the student is already registered.
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return isRegistered;
    }


    //check if a username is taken
    public static boolean checkUsername(pupil_detail pupil){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM pupil where username = ?";
            PreparedStatement st = con.prepareStatement(sql);// Prepares the SQL statement.
            st.setString(1,pupil.getUsername());//Sets the username parameter.
            ResultSet rs = st.executeQuery();//Executes the insert operation.
            if(!rs.isBeforeFirst()){//Checks if the result set is empty, indicating the username is not taken.
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //add a pupil to the database
    public static void updatePupil(pupil_detail pupil){
        String sql = "insert into pupil(name,username,email,password,date_of_birth,school_reg_no) values(?,?,?,?,?,?)";
//: Inserts a rejected pupil into the rejected table.
        try(Connection con = Model.createConnection();) {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, pupil.getName());
            st.setString(2, pupil.getUsername());
            st.setString(3, pupil.getEmail());
            st.setString(4, pupil.getPassword());
            st.setString(5, pupil.getDate_of_birth());
            st.setString(6, pupil.getSchoolRegNo());
            st.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    //update rejected table if pupil is rejected
    public static void updateRejected(pupil_detail pupil){
        String sql = "insert into rejected(name,username,email,password,date_of_birth,school_reg_no) values(?,?,?,?,?,?)";

        try(Connection con = Model.createConnection();) {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, pupil.getName());
            st.setString(2, pupil.getUsername());
            st.setString(3, pupil.getEmail());
            st.setString(4, pupil.getPassword());
            st.setString(5, pupil.getDate_of_birth());
            st.setString(6, pupil.getSchoolRegNo());
            st.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
//
/* Method to retrieve salt for a username
public static String retrieveSaltByUsername(String username) {
    String salt = null;

    try (Connection con = createConnection()) {
        String sql = "SELECT password FROM users WHERE username = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            salt = rs.getString("salt");
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    return salt;
}

    */

    //check if a supplied username and password match for a given pupil
    public static boolean checkPupilLogin(String username, String password){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM pupil where username = ? and password = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,username);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            if(!rs.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //check if a supplied username and password match for a given school representative
    public static boolean checkSRLogin(String email, String password){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM schoolrep where email = ? and password = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,email);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            if(!rs.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

}
