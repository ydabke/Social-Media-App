/**
 * Represents one message between 2 users, part of a larger Chat.
 */
public class Message implements IMessage {
    private User sender;
    private User recipient;
    private String message;

    // Constructor, sets object fields
    public Message(User sender, User recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    // Returns the text of the message, or an empty string if either user is blocked
    public String getMessage() {
        if (sender.isBlocked(recipient) || recipient.isBlocked(sender)) {
            return "";
        }
        return message;
    }

    // Returns the sender of the message
    public User getSender() {
        return sender;
    }

    // Returns the recipient of the message
    public User getRecipient() {
        return recipient;
    }
}