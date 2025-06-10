import org.junit.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Tests all methods in the User class
 */
public class TestUser {

    // Test constructor with username, password, bio, and uniqueID
    @Test
    public void testConstructorWithID() throws IOException {
        User user = new User("Alice", "password123", "I enjoy hiking and painting.",null, false,  101);
        assertEquals("Alice", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("I enjoy hiking and painting.", user.getBio());
        assertEquals(101, user.getUniqueID());
    }

    // Test constructor with username, password, bio, and profilePhoto
    @Test
    public void testConstructorWithProfilePhoto() {
        File photo = new File("path/to/photo.jpg");
        User user = new User("Bob", "securepass", "Tech enthusiast and coffee lover.", null, false, 0);
        assertEquals("Bob", user.getUsername());
        assertEquals("securepass", user.getPassword());
        assertEquals("Tech enthusiast and coffee lover.", user.getBio());
        assertEquals(photo, user.getProfilePhoto());
    }

    // Test constructor with username, password, bio, blocked list, friends list, and uniqueID
    @Test
    public void testConstructorWithFriendsAndBlocked() {
        ArrayList<String> blocked = new ArrayList<>();
        blocked.add("Charlie");

        ArrayList<String> friends = new ArrayList<>();
        friends.add("Diana");

        User user = new User("Eve", "passw0rd!", "Bookworm and nature lover.", blocked, friends,null, false,  102);
        assertEquals("Eve", user.getUsername());
        assertEquals("passw0rd!", user.getPassword());
        assertEquals("Bookworm and nature lover.", user.getBio());
        assertEquals(102, user.getUniqueID());
        assertTrue(user.isBlocked(new User("Charlie", "", "", null, false, 0)));
        assertTrue(user.isFriend("Diana"));
    }

    // Test constructor with all parameters
    @Test
    public void testConstructorWithAllParameters() {
        ArrayList<String> blocked = new ArrayList<>();
        blocked.add("Charlie");

        ArrayList<String> friends = new ArrayList<>();
        friends.add("Diana");

        File photo = new File("path/to/photo.jpg");
        User user =
                new User("Frank", "1234abcd", "Love traveling and meeting new people.", blocked, friends,null, false, 103);
        assertEquals("Frank", user.getUsername());
        assertEquals("1234abcd", user.getPassword());
        assertEquals("Love traveling and meeting new people.", user.getBio());
        assertEquals(103, user.getUniqueID());
        assertEquals(photo, user.getProfilePhoto());
        assertTrue(user.isBlocked(new User("Charlie", "", "", null, false, 0)));
        assertTrue(user.isFriend("Diana"));
    }

    // Test addBlocked and isBlocked methods
    @Test
    public void testAddBlockedAndIsBlocked() {
        User user = new User("Alice", "password123", "I enjoy hiking and painting.",null, false,  101);
        user.addBlocked("Charlie");
        assertTrue(user.isBlocked(new User("Charlie", "", "",null, false,  0)));
    }

    // Test addFriend and isFriend methods
    @Test
    public void testAddFriendAndIsFriend() {
        User user = new User("Bob", "securepass", "Tech enthusiast and coffee lover.", null, false, 102);
        user.addFriend("Diana");
        assertTrue(user.isFriend("Diana"));
    }

    // Test getBlockedString
    @Test
    public void testGetBlockedString() {
        User user = new User("Eve", "passw0rd!", "Bookworm and nature lover.",null, false,  103);
        user.addBlocked("Charlie");
        Database.getUsers().add(new User("Mallory", "password123", "I enjoy hiking and painting.", null, false,  101));
        user.addBlocked("Mallory");
        assertEquals("Charlie,Mallory,", user.getBlockedString());
    }

    // Test getFriendsString
    @Test
    public void testGetFriendsString() {
        User user = new User("Frank", "1234abcd", "Love traveling and meeting new people.", null, false, 104);
        user.addFriend("Diana");
        Database.getUsers().add(new User("Grace", "password123", "I enjoy hiking and painting.",null, false,  101));
        user.addFriend("Grace");
        assertEquals("Diana,Grace,", user.getFriendsString());
        user.removeFriend("Diana");
        assertEquals("Grace,", user.getFriendsString());
    }

    // Test toString method
    @Test
    public void testToString() {
        User user = new User("Alice", "password123", "I enjoy hiking and painting.", null, false, 101);
        String expectedToString = "Alice\npassword123\nI enjoy hiking and painting.\nnone\nnone";
        assertEquals(expectedToString, user.toString());

        Database.getUsers().add(new User("Charlie", "password123", "I enjoy hiking and painting.", null, false, 101));
        Database.getUsers().add(new User("Diana", "password123", "I enjoy hiking and painting.",null, false,  101));
        user.addBlocked("Charlie");
        user.addFriend("Diana");
        expectedToString = "Alice\npassword123\nI enjoy hiking and painting.\nCharlie,\nDiana,";
        assertEquals(expectedToString, user.toString());
    }
}
