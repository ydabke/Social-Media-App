import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Database class to handle User data stored in files in the users folder
 */
public class Database implements IUserDatabase {
    //Creating a user array list that contains all the user objects
    public static ArrayList<User> users = new ArrayList<>();
    // Chat double array list to hold all chats
    public static ArrayList<ArrayList<Chat>> chats = new ArrayList<ArrayList<Chat>>();
    // Gatekeepers to avoid race conditions
    public static final Object chatsGatekeeper = new Object();
    public static final Object usersGatekeeper = new Object();

    public static synchronized void readFiles() { // Read all the User/Chat files and create new Users describing them
        String line;
        ArrayList<String> fileContent;
        BufferedReader br;
        new File("users/").mkdir();
        try {
            //Getting the correct directory
            File dir = new File("users/");
            //Getting the list of files in the directory
            File[] files = dir.listFiles();
            users = new ArrayList<>();
            if (files != null) {
                for (File userFile : dir.listFiles()) {
                    fileContent = new ArrayList<>();
                    br = new BufferedReader(new FileReader(userFile));
                    int uniqueId = Integer.parseInt(userFile.getName().substring(0, userFile.getName().indexOf(".")));
                    line = br.readLine();

                    //Getting all the information for each user and added it to the fileContent array list
                    while ((line != null)) {
                        fileContent.add(line);
                        line = br.readLine();
                    }
                    //Getting the username, password, and bio
                    String username = fileContent.get(0);
                    String password = fileContent.get(1);
                    String bio = fileContent.get(2);
                    ArrayList<String> blocked = new ArrayList<>();
                    ArrayList<String> friends = new ArrayList<>();
                    String photo = null;
                    boolean publicMode = Boolean.parseBoolean(fileContent.get(6));
                    //Checking if friends and blocks is not null then it splits the array lists to get the friends and blocked
                    if (!fileContent.get(3).equals("none")) {
                        blocked = new ArrayList<>(Arrays.asList(fileContent.get(3).split(",")));
                    }
                    if (!fileContent.get(4).equals("none")) {
                        friends = new ArrayList<>(Arrays.asList(fileContent.get(4).split(",")));
                    }
                    if (!fileContent.get(5).equals("null")) {
                        photo = fileContent.get(5);
                    }

                    //Adding all the information to the User object
                    users.add(new User(username, password, bio, blocked, friends, photo, publicMode, uniqueId));
                    br.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File("chat/").mkdir(); // Create folder if doesn't exist
        try {
            //Getting the correct directory
            File dir = new File("chat/");
            //Getting the list of files in the directory
            File[] files = dir.listFiles();
            for (int i = 0; i < users.size(); i++) {
                chats.add(new ArrayList<>());
                while (chats.get(i).size() < users.size()) {
                    chats.get(i).add(null);
                }
            }
            if (files != null) {
                for (File chatFile : dir.listFiles()) { // For each file in folder
                    ArrayList<Message> chatContent = new ArrayList<>();
                    br = new BufferedReader(new FileReader(chatFile));
                    int user1 = Integer.parseInt(chatFile.getName().substring(0, 1));
                    int user2 = Integer.parseInt(chatFile.getName().substring(2, 3));
                    line = br.readLine();

                    //Getting all the information for each chat and added it to the chatContent array list
                    while ((line != null)) {
                        String message = line.split(":")[1];
                        int sender = Integer.parseInt(line.split(":")[0]);
                        int recipient;
                        if (sender == user1) {
                            recipient = user2;
                        } else {
                            recipient = user1;
                        }
                        chatContent.add(new Message(Database.getUser(sender), Database.getUser(recipient),
                                message));
                        line = br.readLine();
                    }
                    chats.get(user1 - 1).set(user2 - 1, new Chat(user1, user2, chatContent));
                    br.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void writeFiles() { // Write all User objects to files
        for (User user : users) {
            //Creating a new file based on the uniqueID of the user
            new File("users/").mkdir(); // Create folder if doesn't exist
            try (PrintWriter pw = new PrintWriter(new FileWriter(new File("users/" + user.getUniqueID() + ".txt")))) {
                //Prints the user information to the file
                pw.print(user.toString());
                pw.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }


    public synchronized boolean addUser(User newUser) {
        //If the user is not in the users array list then it gets added otherwise it returns false which means that the user is already created
        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                return false;
            }
        }
        users.add(newUser);
        chats.add(new ArrayList<>());
        while (chats.get(chats.size() - 1).size() < users.size()) {
            chats.get(chats.size() - 1).add(null);
        }
        return true;
    }

    // Returns the userID corresponding with the provided username
    public static int getUserId(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getUniqueID();
            }
        }
        return 0;
    }

    // Returns the username corresponding with the provided user ID
    public static String getUsername(int UserId) {
        for (User user : users) {
            if (user.getUniqueID() == UserId) {
                return user.getUsername();
            }
        }
        return "";
    }

    // Returns the User with the provided user ID
    public static User getUser(int id) {
        for (User user : users) {
            if (user.getUniqueID() == id) {
                return user;
            }
        }
        return null;
    }

    // Returns the user with the provided username
    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Returns true if the Username and password are correct, false if not
    public int login(String username, String password) {
        for (User tempUser : users) {
            if (tempUser.getUsername().equals(username) && tempUser.getPassword().equals(password)) {
                return tempUser.getUniqueID();
            }
        }
        return 0;
    }

    public ArrayList<User> searchForUser(
            String searchName) { // Searches for matches using an input search term (partial matches are included)
        ArrayList<User> returnValue = new ArrayList<>();
        for (User tempUser : users) {
            if (tempUser.getUsername().contains(searchName)) {
                returnValue.add(tempUser);
            }
        }
        return returnValue;
    }
}



