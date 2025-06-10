import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Represents a direct message between two users. The Chats are stored in an ArrayList of Messages
 */
public class Chat implements IChat {
    private final User user1;
    private final User user2;
    private final File chatFile;
    private ArrayList<Message> chat;
    private final Object gatekeeper = new Object();

    // Constructor
    public Chat(int id1, int id2) {
        //Sets user1 to the lower userid, user2 to the larger one
        if (id1 < id2) {
            this.user1 = Database.getUser(id1);
            this.user2 = Database.getUser(id2);
        } else {
            this.user1 = Database.getUser(id2);
            this.user2 = Database.getUser(id1);
        }
        //Creates empty chat and sets chat file path
        this.chat = new ArrayList<>();
        new File("chat/").mkdir();
        this.chatFile = new File("chat/" + this.user1.getUniqueID() + "_" + this.user2.getUniqueID() + ".txt");

        // Create file since it doesn't exist
        try {
            chatFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    public Chat(int id1, int id2, ArrayList<Message> chat) {
        this(id1, id2);
        this.chat = chat;
    }

    // Method to write a message to the array list
    public void writeMessage(User sender, String message) {
        if (!(message.equalsIgnoreCase("exit"))) {
            User recipient;
            if (user1.getUniqueID() == sender.getUniqueID()) {
                recipient = user2;
            } else {
                recipient = user1;
            }
            chat.add(new Message(sender, recipient, message));
            for (Message m : chat) {
                System.out.println(m.getMessage());
            }
        }
    }

    // Method to print the contents of the file
    public String returnChat() {
        String fullChat = "";
        for (Message message : chat) {
            fullChat += (message.getSender().getUsername() + ": " + message.getMessage() + ",,,");
        }
        return fullChat;
    }

    // Method to save the chat to the file
    public void saveChat() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(chatFile))) {
            for (Message m : chat) {
                pw.write(m.getSender().getUniqueID() + ":" + m.getMessage() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public ArrayList<Message> getChat() {
        return chat;
    }

    public Object getGatekeeper() {
        return gatekeeper;
    }
}
