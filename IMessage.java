/**
 * Interface for the Message class between two Users
 */
public interface IMessage {
    // Returns the text of the message, "" if either user is blocked
    String getMessage();

    // Returns the User that sent the message
    User getSender();

    // Returns the User that received the message
    User getRecipient();
}