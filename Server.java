
import java.io.*;
import java.net.*;

public class Server implements Runnable, IServer {

    private static int port = 4242;
    private static Database db = new Database();
    static Socket socket;

    public static void main(String[] args) {
        Database.readFiles();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                while (true) {
                    socket = serverSocket.accept(); // Wait for client to connect to server
                    System.out.println("Client connected");
                    Thread serverThread = new Thread(new Server());
                    serverThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Create thread to handle that client
        System.out.println("Running...");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String input; // Initializing command variables
            String action = "";
            String[] information;

            while (!action.equals("0")) { // When action is 0 that client has exited
                System.out.println("Waiting for command...");
                Database.writeFiles(); // Make sure data is up to date
                Database.readFiles();
                input = in.readLine();
                System.out.println(input);
                action = input.substring(0,
                        1); // First character of command input is the type of command
                information = input.substring(1)
                        .split(","); // The rest of the command input is data to use in command
                switch (action) {
                    case "1": // Log in
                        out.println(db.login(information[0], information[1])); // Return success/failure
                        break;
                    case "2": // Sign up
                        out.println(db.addUser(new User(information[0], information[1], information[2], information.length > 3 ? information[3] : "null",
                                true, Database.getUsers().size() + 1))); // Return success/failure
                        Database.writeFiles(); // Store new user to file (if created)
                        break;
                    case "3": // Chat
                        Database.readFiles();
                        //Checks if the user is not friends and in private mode
                        int receiverID = Database.getUserId(information[1]);
                        int senderID = Database.getUserId(information[0]);
                        boolean notFriends = true;
                        if (!Database.getUser(receiverID).getPublicMode()) {
                            String fString = Database.getUserByUsername(information[1]).getFriendsString();
                            for (String username : fString.split(",")) {
                                if (username.equals(information[0])) {
                                    notFriends = false;
                                }
                            }
                        }
                        //Checks if either user blocked the other
                        boolean blocked = false;
                        String bString = Database.getUserByUsername(information[1]).getBlockedString();
                        for (String username : bString.split(",")) {
                            if (username.equals(information[0])) {
                                blocked = true;
                            }
                        }
                        bString = Database.getUserByUsername(information[0]).getBlockedString();
                        for (String username : bString.split(",")) {
                            if (username.equals(information[1])) {
                                blocked = true;
                            }
                        }
                        // Does not let the chat open if a user is blocked, or not friends and in private mode
                        if (receiverID == 0 || (!Database.getUser(receiverID).getPublicMode() && notFriends) || blocked) {
                            out.println("false");

                        } else {
                            // Opens chat
                            out.println("true");
                            int user1, user2;
                            if (senderID < receiverID) {
                                user1 = senderID;
                                user2 = receiverID;
                            } else {
                                user2 = senderID;
                                user1 = receiverID;
                            }
                            // Creates the chat object in the array list if it doesnt exist already
                            if (Database.chats.get(user1 - 1).get(user2 - 1) == null) {
                                synchronized (Database.chatsGatekeeper) {
                                    Chat chat = new Chat(user1, user2);
                                    Database.chats.get(user1 - 1)
                                            .set(user2 - 1, chat);
                                }
                            }
                            Thread spamLog = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        while (true) {
                                            out.println(Database.chats.get(user1 - 1).get(user2 - 1).returnChat());
                                            Thread.sleep(1000);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Stop sending log!");
                                    }
                                }
                            });
                            spamLog.start();
                            // Keeps writing user input to array list until client quits
                            String[] message = {"continue", " "};
                            System.out.println(message[0]);
                            while (!message[0].equals("quit")) {
                                message = in.readLine().split(",");
                                // synchronized with chat object to avoid race conditions
                                if (!message[0].equals("quit")) {
                                    synchronized (Database.chats.get(user1 - 1).get(user2 - 1)
                                            .getGatekeeper()) {
                                        Database.chats.get(user1 - 1).get(user2 - 1)
                                                .writeMessage(Database.getUser(senderID), message[1]);
                                        System.out.println("Message sent");
                                        Database.chats.get(user1 - 1).get(user2 - 1).saveChat();
                                    }
                                }
                            }
                            // Sends quit message to client
                            spamLog.interrupt();
                            out.println("end");
                            System.out.println("EXITED CHAT");
                        }
                        break;
                    case "4": // Add friend; Same concept as cases 5,7,8
                        if (information.length == 2) { // Return failure if empty input
                            out.println(false);
                        } else {
                            out.println(Database.getUser(Integer.parseInt(information[1]))
                                    .addFriend(information[2]));
                        } // Return success/failure
                        break;
                    case "5": // Remove friend
                        if (information.length == 2) {
                            out.println(false);
                        } else {
                            out.println(Database.getUser(Integer.parseInt(information[1]))
                                    .removeFriend(information[2]));
                        }
                        break;
                    case "6": // View friends list; Same concept as case 9
                        out.println(
                                Database.getUser(Integer.parseInt(information[1])).getFriendsString());
                        // Return list of friends (returns nothing if no friends)
                        break;
                    case "7": // Add blocked
                        if (information.length == 2) {
                            out.println(false);
                        } else {
                            out.println(Database.getUser(Integer.parseInt(information[1]))
                                    .addBlocked(information[2]));
                        }
                        break;
                    case "8": // Remove blocked
                        if (information.length == 2) {
                            out.println(false);
                        } else {
                            out.println(Database.getUser(Integer.parseInt(information[1]))
                                    .removeBlocked(information[2]));
                        }
                        break;
                    case "9": // View blocked list
                        out.println(
                                Database.getUser(Integer.parseInt(information[1])).getBlockedString());
                        break;
                    case "a": // View chat list
                        Database.readFiles();
                        String chatUsers = "";
                        for (int i = 0; i < Database.chats.get(0).size(); i++) {
                            for (int j = 0; j < Database.chats.get(0).size(); j++) {
                                if (Database.chats.get(i).get(j) != null) {
                                    if (i + 1 == Integer.parseInt(information[1])) {
                                        chatUsers += Database.getUser(j + 1).getUsername();
                                        chatUsers += ",";
                                    } else if (j + 1 == Integer.parseInt(information[1])) {
                                        chatUsers += Database.getUser(i + 1).getUsername();
                                        chatUsers += ",";
                                    }
                                }
                            }
                        }
                        if (chatUsers.length() > 0) {
                            chatUsers = chatUsers.substring(0, chatUsers.length() - 1);
                        }
                        out.println(chatUsers);
                        break;
                    case "b": // view base64Photo String
                        out.println(
                                Database.getUserByUsername(information[1]).getProfilePhoto());
                        break;

                    case "c": //Check if a user exists
                        boolean userExists = false;
                        if (information.length < 2) {
                            userExists = false;
                        } else {
                            for (User user : Database.users) {
                                if (information[1].equals(user.getUsername())) {
                                    userExists = true;
                                }
                            }
                        }
                        out.println(String.valueOf(userExists));
                        break;
                    case "d": // Check public/private status; Same concept as case 9
                        out.println(
                                Database.getUserByUsername(information[0]).getPublicMode());
                        break;
                    case "e": // Exit
                        System.out.println("Client exiting");
                        action = "0";
                        break;
                    case "f": // Change public/private status
                        Database.getUserByUsername(information[0]).setPublicMode(Boolean.parseBoolean(information[1]));
                        break;
                    case "g": // Gets bio information
                        out.println(Database.getUserByUsername(information[0]).getBio());
                    default: // Unknown action handler (should never be called)
                        System.out.println("Unknown action, ignoring");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
