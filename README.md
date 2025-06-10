# CS 180 Team Project

## Classes
### User
- String getUsername();
  - Return this User object's username.

- String getPassword();
  - Return this User object's password.

- String getBio();
  - Return this User object's bio.

- int getUniqueID();
  - Return this User object's unique ID.

- void setUsername(String username);
  - Sets this User object's username to the input one.

- void setPassword(String password);
  - Sets this User object's password to the input one.

- void setBio(String bio);
  - Sets this User object's bio to the input one.

- File getProfilePhoto();
  - Return this User object's photo.

- void addBlocked(String username);
  - Attempts to add the input username to this user's blocked arraylist.

- void addFriend(String username);
  - Attempts to add the input username to this user's friends arraylist.

- boolean removeBlocked(String username);
  - Attempts to remove a user from the blocked list
- boolean removeFriend(String username);
  - Attempts to remove a user from the friends list

- void setProfilePhoto(File profilePhoto);
  - Sets this User object's photo to the input one.

- boolean isBlocked(User username);
  - Return whether the User object associated with the input username is blocked by this user or not.

- boolean isFriend(String username);
  - Return whether the User object associated with the input username is friended by this user or not.

- String getBlockedString();
  - Returns this User object's blocked arraylist as a string for storing purposes.

- String getFriendsString();
  - Returns this User object's friends arraylist as a string for storing purposes.

- String toString();
  - Returns all of this User object's data as a string for storing purposes.



### Chat
- void writeMessage(User sender, String message);
  - Writes a message from a user to a chat file.
 
- void printChat();
  - Prints the chat file to show the User the chat history.
 
- User getUser1();
  - Returns the user with the lower user ID.
 
- User getUser2();
  - Returns the user with the higher user ID.
 
- ArrayList<Message> getChat();
  - Returns an ArrayList with all of the messages in the chat between User1 and User2.

  
### Database
- static void readFile();
  - Opens a Buffered Reader stream for each of the files in the user directory and reads each file. Uses all the recorded data to create a User object for each user stored and adds them to the users arraylist.

- static void writeFiles();
  - Creates/overrites the text file for each User that stores their data. The file name will be the user's uniqueID + .txt.

- ArrayList<User> getUsers();
  - Returns the arraylist of users.

- boolean addUser(User newUser);
  - Tries to add the input user to the arraylist of users. If successful return true; If not return false.

- static int getUserId(String username)
  - Returns the user id of the user with the input username
- static String getUsername(int UserId)
  - Returns the username of the user with the input user id
- static User getUser(int id)
  - Returns the user object of the user with the input user id
- static User getUserByUsername (String username)
  - Returns the user object of the user with the input username
- ArrayList<User> searchForUser(String searchName)
  - Searches for matches using an input search term (partial matches are included)

- int login(String username, String password);
  - Tries to log in using the input credentials. Returns the user's id if successful and returns 0 if failure



### Message
Represents one message between two users, an ArrayList of Messages is passed into the Chat class representing a chat between the users.

- String getMessage()
  - Returns the String form of the message
  - If either user has the other blocked, getMessage() returns an empty string instead.

- User getSender()
  - Returns the User object that's the sender of the message

- User getRecipient()
  - Returns the User object that's the recipient of the message

### Client
The client side of the program. Opens a GUI when run that allows the user to send commands to the server.
- First Screen
  - User inputs username and password
- Login
  - If User selects log in, the server tries to log them in
- Signup
  - If the User selects signup, the user is prompted to enter a bio and select a profile photo
  - The server then tries to create a new user with those parameters


After logging in the client GUI shows the user more options
- View Next Page
  - Clicking this allows user to go through the chat list, friend list and blocked list
    - Your Chats
      - Shows list of other users that you have chats with
    - Your Friends
      - Shows list of friends
    - Your Blocked
      - Shows list of blocked users
- Status: Public/Private
  - Allows user to toggle between public and private depending on preference
- Quit
  - Exits program
- Search User
  - Prompted to enter user to either add as a friend, block, or chat with
- Add friend
  - User inputs the username of the person they want to add as friend. 
- Remove friend
  - User inputs the username of the person they want to remove as friend.
- Block
  - User inputs the username of the person they want to add as blocked. 
- Unblock
  - User inputs the username of the person they want to remove as blocked. '
- Chat
  - User inputs username of person they want to chat with and user can chat and view chat history with other user
- Profile Photo and Username displayed in top left of GUI

### Server
The server side of the program. Starts the server when run that waits for clients to connect, then establishes connections with them, and then carries out any commands sent from the client and returns desired outputs
- Login
  - Takes in input username and password from client and tries to log in using those parameters. Returns success/failure message
- Signup
  - Takes in input required to create a new user and tries to create a new user with that input. Returns success/failure message
- Exit
  - Closes connections with the client that sent the exit command
- Chat
  - Creates a chat between two users and facilitates the chatting through input from client GUI for those users
- Add friend
  - Attempts to add the input username to the client's user's friends list. Returns success/failure message
- Remove friend
  - Attempts to remove the input username from the client's user's friends list. Returns success/failure message
- View friends list
  - Returns all of the user's friends. Returns nothing if no friends
- Add blocked
  - Attempts to add the input username to the client's user's blocked list. Returns success/failure message
- Remove blocked
  - Attempts to remove the input username from the client's user's blocked list. Returns success/failure message
- View blocked list
  - Returns all of the user's blocked. Returns nothing if no blocked

### Running IO
- Run the Server.java file and as many Client.java (for each user) as needed
- Host: localhost
- Port: 180

## Test Cases
### Test User
- Tested all constructors with assert equal statements to make sure the parameters input into the constructor match the output of the getters of the created objects
- Tested all setters and getters with assert equal statements to make sure setters set correctly and getters got correctly
- Tested addFriend, addBlocked, isFriend, and isBlocked with assert true statements to make sure objects were added and checked correctly
- Tested getFriendsAsString and getBlockedAsString with assert equal statements to make sure they returned expected outputs
- Tested toString with an assert equal statement to make sure it returned the expected output

### Test Chat
- Tested the constructor by creating users and seeing if the chat file between two users is able to be created
- Tested the testWriteMessage by creating two users and writing messages and checking if the file was able to save the newly created message
- Tested testPrintChat by creating a chat between 2 users and checking if the chat history can be printed from it corresponding chat file
- Tested testGetUser1 by checking if it is able to get the first user of the chat file
- Tested testGetUser2 by checking if it is able to get the second user of the chat file
- Tested testGetChat by checking if it was able to get the array list of messages between the two corresponding users

### Test Database
- Tested addUser by attempting to add a new User and checking if the User was added
- Tested addUser by attempting to add an existing User and checking if the User was not added
- Tested getUsers by adding two Users and checking if the arraylist returned by getUsers contained those Users
- Tested getUserId by adding a User with the unique ID 77 and checking if the ID returned by getUserId matched that ID
- Tested getUser by adding a User and checking if that user matched the user returned by getUser

### Test Message
- Tested constructor by creating a new message between two Users with the proper parameters and checking if any errors were thrown
- Tested getMessage by creating a new message between two Users with a specific message and checking if the message matched the output of getMessage
- Tested getRecipient by creating a new message between two Users with a specific recipient and checking if the recipient matched the output of getRecipient
- Tested getSender by creating a new message between two Users with a specific sender and checking if the sender matched the output of getSender

### Test Message
- Tested constructor by creating a new message between two Users with the proper parameters and checking if any errors were thrown
- Tested getMessage by creating a new message between two Users with a specific message and checking if the message matched the output of getMessage
- Tested getRecipient by creating a new message between two Users with a specific recipient and checking if the recipient matched the output of getRecipient
- Tested getSender by creating a new message between two Users with a specific sender and checking if the sender matched the output of getSender

### Test Client

### Test Server
