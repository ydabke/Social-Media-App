import org.junit.*;

import static org.junit.Assert.*;

/**
 * Tests all methods in the Message class
 */
public class TestMessage {
    // Tests the Constructor of the Message class
    @Test(timeout = 1000)
    public void testConstructor() {
        // Makes a message with example inputs
        String input = "hellow this is my message";
        User sender = new User("name", "password", "bio", null, false,  0);
        User recipient = new User("name2", "password", "bio",null, false,  1);

        // Makes sure the constructor doesn't throw any errors, if it does then the test fails
        try {
            new Message(sender, recipient, input);
            assertEquals("You should never see this", true, true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // Tests that getMessage returns the correct text
    @Test(timeout = 1000)
    public void testGetMessage() {
        // Creates new message with example input
        String input = "hellow this is my message";
        User sender = new User("name", "password", "bio",null, false,  0);
        User recipient = new User("name2", "password", "bio",null, false,  1);

        Message message = new Message(sender, recipient, input);
        // Checks that the input is equal to the message's getMessage() method
        assertEquals("Unexpected value for message", input, message.getMessage());
    }

    // Checks if getRecipient() returns the correct user
    @Test(timeout = 1000)
    public void testGetRecipient() {
        // Input example data into the Message object
        String input = "hellow this is my message";
        User sender = new User("name", "password", "bio",null, false,  0);
        User recipient = new User("name2", "password", "bio",null, false,  1);

        Message message = new Message(sender, recipient, input);

        // Makes sure recipient is equal to the output of getRecipient()
        assertEquals("Unexpected value for message", recipient, message.getRecipient());
    }

    // Checks if getSender() returns the correct user
    @Test(timeout = 1000)
    public void testGetSender() {
        // Input example data into the Message object
        String input = "hellow this is my message";
        User sender = new User("name", "password", "bio",null, false,  0);
        User recipient = new User("name2", "password", "bio",null, false,  1);

        Message message = new Message(sender, recipient, input);

        // Makes sure sender is equal to the output of getSender()
        assertEquals("Unexpected value for message", sender, message.getSender());
    }
}