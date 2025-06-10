
import java.util.ArrayList;

/**
 * Interface for the Database handling all Users
 */
public interface IUserDatabase {
    // Commented methods are not listed in the interface
    // since they are static and therefore cannot be overridden

    // Read all the User/Chat files and create new Users describing them
    // void readFiles();

    // Write all User objects to files
    // void writeFiles();

    // Returns the ArrayList of all Users
    // ArrayList<User> getUsers();

    // Adds a user to the User array list
    boolean addUser(User newUser);

    // Returns a given username/userID/User based on username/userID
    // int getUserId(String username);
    // User getUser(int id)
    // User getUserByUsername (String username)

    // Searches for a User using the input search term
    ArrayList<User> searchForUser(String searchName);

    // Attempts to log in with the input username and password
    int login(String username, String password);
}
