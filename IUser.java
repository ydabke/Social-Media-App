/**
 * Interface for the User class
 */
public interface IUser {
    // Returns User's String username
    String getUsername();


    // Returns the User's Password
    String getPassword();


    // Returns the User's Bio statement
    String getBio();

    // Returns the User's status
    boolean getPublicMode();


    // Returns the User's integer unique User ID number
    int getUniqueID();


    // Setter for the username
    void setUsername(String username);


    // Setter for the password
    void setPassword(String password);


    // Setter for the bio
    void setBio(String bio);

    // Setter for the status
    void setPublicMode(boolean publicMode);


    // Getter for the user's profile photo
    String getProfilePhoto();


    // Attempts to block another user by username
    boolean addBlocked(String username);


    // Attempts to add a user as friends by username
    boolean addFriend(String username);


    // Attempts to remove a user from blocked list
    boolean removeBlocked(String username);


    // Attempts to remove a user from friends list
    boolean removeFriend(String username);


    // Set user profile photo
    void setProfilePhoto(String profilePhoto);


    // Check if another user is blocked
    boolean isBlocked(User username);


    // Check if another user is friended
    boolean isFriend(String username);


    // Returns all blocked users as a String
    String getBlockedString();


    // Returns all friended users as a String
    String getFriendsString();


    // ToString method for the User
    @Override
    String toString();
}
