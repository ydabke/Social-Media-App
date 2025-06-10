import org.junit.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests all methods in the Database class
 */
public class TestDatabase {

    //tests readFile
    @Test(timeout = 1000)
    public void testReadFiles() {
        // Create a temporary directory and files for testing
        File tempDir = new File("test_users");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        try {
            // Create mock user files
            File userFile1 = new File(tempDir, "1.txt");
            File userFile2 = new File(tempDir, "2.txt");

            // Write mock data into the user files
            try (PrintWriter writer = new PrintWriter(userFile1)) {
                writer.println("user1"); // username
                writer.println("password1"); // password
                writer.println("bio1"); // bio
                writer.println("none"); // blocked
                writer.println("none"); // friends
            }
            try (PrintWriter writer = new PrintWriter(userFile2)) {
                writer.println("user2");
                writer.println("password2");
                writer.println("bio2");
                writer.println("none");
                writer.println("none");
            }

            // Simulate the directory and chat files (if necessary)
            File chatDir = new File("test_chats");
            if (!chatDir.exists()) {
                chatDir.mkdir();
            }
            File chatFile = new File(chatDir, "1-2.txt");
            try (PrintWriter writer = new PrintWriter(chatFile)) {
                writer.println("1:Hello, user2!");
                writer.println("2:Hi, user1!");
            }

            // Clear previous data and prepare to call readFiles
            Database.users.clear();  // Clear any previous state
            Database.chats.clear();

            // Check that lists are empty before calling readFiles
            assertTrue("Users list should be empty before readFiles", Database.getUsers().isEmpty());
            assertTrue("Chats list should be empty before readFiles", Database.chats.isEmpty());

            // Call the method to read files
            Database.readFiles();

            // Validate that users and chats are correctly populated
            assertEquals("First user's username should be user1", "user1", Database.getUsers().get(0).getUsername());
            assertEquals("Second user's username should be user2", "user2", Database.getUsers().get(1).getUsername());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up test files after the test runs
            deleteDirectory(new File("test_users"));
            deleteDirectory(new File("test_chats"));
        }
    }

    // Helper method to delete files and directories recursively
    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                deleteDirectory(file);
            }
        }
        dir.delete();
    }


    @Test(timeout = 1000)
    public void testAddNewUser() {
        //Creating user1 and database
        User user1 = new User("name", "password", "bio", null, false, 0);
        Database database1 = new Database();

        try {
            //Trying to add user1 to database
            boolean output = database1.addUser(user1);
            //Print error if add was unsuccessful
            assertFalse("Did not add user1", output);
            //Print error if User in database is not equal to user1
            assertEquals("Added user doesn't match user1", user1.getUsername(), Database.getUser(0).getUsername());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(timeout = 1000)
    public void testAddExistingUser() {
        //Creating user1, user2 (same as user1), and database
        User user1 = new User("name", "password", "bio",null, false,  0);
        User user2 = new User("name", "password", "bio", null, false, 0);
        Database database2 = new Database();

        try {
            //Trying to add user1 to database
            database2.addUser(user1);
            //Trying to add user2 to database
            boolean output = database2.addUser(user2);
            //Print error if add was successful
            assertFalse("Added duplicate user (bad)", output);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(timeout = 1000)
    public void testGetUsers() {
        //Creating user1, user2, and database
        User user1 = new User("name", "password", "bio",null, false,  0);
        User user2 = new User("name2", "password2", "bio",null, false,  1);
        //Creating arraylist users with user1 and user2 in it
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        Database database3 = new Database();

        try {
            //Trying to add user1 to database
            database3.addUser(user1);
            //Trying to add user2 to database
            database3.addUser(user2);
            for (int i = 0; i < users.size(); i++) {
                //Print error if Users in users are not equal to Users in the arraylist returned by getUsers
                assertEquals("ArrayLists don't match", users.get(i).getUsername(),
                        Database.getUsers().get(i).getUsername());

            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(timeout = 1000)
    public void testGetUserId() {
        //Creating user1 and database
        User user1 = new User("namess", "password", "bio", null, false, 77);
        Database database4 = new Database();

        try {
            //Trying to add user1 to database
            database4.addUser(user1);

            //Print error if user1's unique ID is different from the unique ID of the user in database
            assertEquals("ID's don't match", user1.getUniqueID(), Database.getUserId(user1.getUsername()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(timeout = 1000)
    public void testGetUser() {
        //Creating user1 and database
        User user1 = new User("namess", "password", "bio",null, false,  77);
        Database database5 = new Database();

        try {
            //Trying to add user1 to database
            database5.addUser(user1);

            //Print error if user1's username is different from the username of the user returned by getUser
            assertEquals("Added User is not equal to the User returned by getUsername", user1.getUsername(),
                    Database.getUser(user1.getUniqueID()).getUsername());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }



    @Test
    public void testGetUserByUsername() {
        //Creating user1 and database
        User user1 = new User("nameUSERNAME", "password", "bio",null, false,  77);
        Database database6 = new Database();

        try {
            //Trying to add user1 to database
            database6.addUser(user1);

            //Print error if user1's username is different from the username of the user returned by getUser
            assertEquals("Added User is not equal to the User returned by getUserByUsername", user1,
                    Database.getUserByUsername(user1.getUsername()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
