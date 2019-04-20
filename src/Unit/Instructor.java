package Unit;

import Util.DBUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Instructor implements User{

    private SimpleIntegerProperty instructor_id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private SimpleStringProperty full_name;
    private SimpleStringProperty email;
    private SimpleStringProperty telephone;
    private boolean isAdmin;

    public Instructor(int instructor_id, String username,
                      String password, String firstname, String lastname,
                      String email, String telephone, boolean isAdmin) {
        this.instructor_id = new SimpleIntegerProperty(instructor_id);
        this.password = password;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
        this.isAdmin = isAdmin;
        this.full_name = new SimpleStringProperty(firstname + " " + lastname);
    }

    public static Instructor getInstructorById(int instructor_id){
        Connection conn = DBUtil.connect();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT  * FROM instructors WHERE ID = ?");
            stmt.setInt(1,instructor_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new Instructor(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getBoolean(8));
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getInstructor_id() {
        return instructor_id.get();
    }

    public SimpleIntegerProperty instructor_idProperty() {
        return instructor_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFull_name() {
        return full_name.get();
    }

    public SimpleStringProperty full_nameProperty() {
        return full_name;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public SimpleStringProperty telephoneProperty() {
        return telephone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id.set(instructor_id);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFull_name(String full_name) {
        this.full_name.set(full_name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
