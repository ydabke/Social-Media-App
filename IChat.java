import java.util.ArrayList;
/**
 * Interface for the Chat class
 */
public interface IChat {
    // Method to write the current chat message to the chat file
    void writeMessage(User sender, String message);

    // Method to print the contents of the file
    String returnChat();

    // Method to save the chat to the file
    void saveChat();

    // Gets the user with the smaller userID
    User getUser1();

    // Gets the user with the larger userID
    User getUser2();

    // Returns all messages in the chat
    ArrayList<Message> getChat();

    // Returns gatekeeper to make mutation thread safe
    Object getGatekeeper();
}
