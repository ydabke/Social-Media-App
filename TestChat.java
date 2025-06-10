import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Tests all methods in the Chat class
 */
public class TestChat {

    // Test constructor with username, password, bio, and uniqueID
    @Test
    public void testConstructor() {
        User user1 = new User("user1", "123", "I am user 1", null, false, 1);
        User user2 = new User("user2", "456", "I am user 2", null, false, 2);

        File chatFile1 = new File("chat/" + user1.getUniqueID() + "_" + user2.getUniqueID() + ".txt");
        // Write a sample message to the file and reload
        try (FileWriter writer = new FileWriter(chatFile1)) {
            writer.write(user1.getUniqueID() + ":Hello\n");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        Chat newChat = new Chat(user1.getUniqueID(), user2.getUniqueID());
        assertEquals(1, newChat.getChat().size());
        assertEquals("Hello", newChat.getChat().get(0).getMessage());

        //Deleting files created by test casess
        File dir = new File("chat/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File userFile : dir.listFiles()) {
                userFile.delete();
            }
        }

        //Deleting files created by test cases
        File dir2 = new File("users/");
        File[] files2 = dir.listFiles();
        if (files2 != null) {
            for (File userFile : dir2.listFiles()) {
                userFile.delete();
            }
        }
    }

    // Test constructor with username, password, bio, and profilePhoto
    @Test
    public void testWriteMessage() {
        Database dbT = new Database();
        User userC = new User("userC", "123", "I am user 1", null, false,3);
        User userD = new User("userD", "456", "I am user 2", null, false,4);

        dbT.addUser(userC);
        dbT.addUser(userD);

        Chat chat2 = new Chat(userC.getUniqueID(), userD.getUniqueID());
        File chatFile2 = new File("chat/" + userC.getUniqueID() + "_" + userD.getUniqueID() + ".txt");
        chat2.writeMessage(userC, "Hello, User2!");
        assertEquals(1, chat2.getChat().size());

        Message message = chat2.getChat().get(0);
        assertEquals("Hello, User2!", message.getMessage());
        assertEquals(userC.getUsername(), message.getSender().getUsername());

        // Check that the message was appended to the file
        try (BufferedReader reader = new BufferedReader(new FileReader(chatFile2))) {
            String line = reader.readLine();
            assertTrue(line.contains("Hello, User2!"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        //Deleting files created by test cases
        File dir = new File("chat/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File userFile : dir.listFiles()) {
                userFile.delete();
            }
        }
    }

    // Test constructor with username, password, bio, blocked list, friends list, and uniqueID
    @Test
    public void testPrintChat() {
        Database database6 = new Database();
        User user3 = new User("user3", "123", "I am user 3", null, false, 3);
        User user4 = new User("user4", "456", "I am user 4", null, false,  4);

        database6.addUser(user3);
        database6.addUser(user4);
        Chat chat6 = new Chat(user3.getUniqueID(), user4.getUniqueID());

        chat6.writeMessage(user3, "First message");
        chat6.writeMessage(user4, "Second message");

        File chatFile2 = new File("chat/" + user3.getUniqueID() + "_" + user4.getUniqueID() + ".txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(chatFile2))) {
            String line = reader.readLine();
            assertEquals("3:First message", line);
            line = reader.readLine();
            assertEquals("4:Second message", line);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        //Deleting files created by test cases
        File dir = new File("chat/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File userFile : dir.listFiles()) {
                userFile.delete();
            }
        }
    }

    //Test to see if chat can get user 1
    @Test
    public void testGetUser1() {
        Database db3 = new Database();
        User userE = new User("user1", "123", "I am user 1",null, false,  1);
        User userF = new User("user2", "456", "I am user 2",null, false,  2);

        db3.addUser(userE);
        db3.addUser(userF);
        db3.writeFiles();

        Chat chat = new Chat(userE.getUniqueID(), userF.getUniqueID());
        assertEquals(userE, chat.getUser1());
    }

    //Test to see if chat can get user 2
    @Test
    public void testGetUser2() {
        Database db = new Database();
        User user1 = new User("user1", "123", "I am user 1",null, false,  1);
        User user2 = new User("user2", "456", "I am user 2",null, false,  2);

        db.addUser(user1);
        db.addUser(user2);

        Chat chat = new Chat(user1.getUniqueID(), user2.getUniqueID());
        assertEquals(user2.getUsername(), chat.getUser2().getUsername());
    }

    //Test to see if chat can get chat
    @Test
    public void testGetChat() {
        Database db = new Database();
        User user1 = new User("user7", "123", "I am user 1",null, false,  7);
        User user2 = new User("user8", "456", "I am user 2", null, false, 8);

        db.addUser(user1);
        db.addUser(user2);

        Chat chat = new Chat(user1.getUniqueID(), user2.getUniqueID());
        assertEquals(0, chat.getChat().size());  // Initially, the chat should be empty
    }
}
