import java.util.ArrayList;


/**a
 * Class representing a User on the social media app
 */
public class User implements IUser {
    private String username;
    private String password;
    private String bio;
    private int uniqueID;
    private ArrayList<String> blocked;
    private ArrayList<String> friends;
    private String profilePhoto;
    private boolean publicMode;


    public User(String username, String password, String bio, ArrayList<String> blocked, ArrayList<String> friends, boolean publicMode, int uniqueID) {
        this(username, password, bio, publicMode);
        this.blocked = blocked;
        this.friends = friends;
        this.profilePhoto = null;
        this.uniqueID = uniqueID;
    }


    public User(String username, String password, String bio, ArrayList<String> blocked, ArrayList<String> friends, String profilePhoto, boolean publicMode, int uniqueID) {
        this(username, password, bio, blocked, friends, publicMode, uniqueID);
        this.profilePhoto = profilePhoto;
    }


    public User(String username, String password, String bio, String profilePhoto, boolean publicMode, int uniqueID) {
        this(username, password, bio, publicMode);
        this.blocked = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.profilePhoto = profilePhoto;
        this.uniqueID = uniqueID;
    }

    private User(String username, String password, String bio, boolean publicMode) {
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.publicMode = publicMode;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getBio() {
        return bio;
    }


    public int getUniqueID() {
        return uniqueID;
    }


    public String getProfilePhoto() {
        return profilePhoto;
    }

    public boolean getPublicMode() {
        return publicMode;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }


    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setPublicMode(boolean publicMode) {
        this.publicMode = publicMode;
    }

    public boolean addBlocked(String username) {
        if (Database.getUserByUsername(username) == null || username.equals(this.getUsername())) {
            return false;
        }
        for (String tempUsername : blocked) {
            if (tempUsername.equals(username)) {
                return false;
            }
        }
        this.removeFriend(username);
        blocked.add(username);
        return true;
    }


    public boolean addFriend(String username) {
        if (Database.getUserByUsername(username) == null || username.equals(this.getUsername())) {
            return false;
        }
        if(Database.getUserByUsername(username).isBlocked(this)){
            return false;
        }
        for (String tempUsername : blocked) {
            if (tempUsername.equals(username)) {
                return false;
            }
        }
        for (String tempUsername : friends) {
            if (tempUsername.equals(username)) {
                return false;
            }
        }
        friends.add(username);
        Database.getUserByUsername(username).addFriend(this.getUsername());
        return true;
    }



    public boolean removeBlocked(String username) {
        if (Database.getUserByUsername(username) == null) {
            return false;
        }
        for (int i = 0; i < blocked.size(); i++) {
            if (blocked.get(i).equals(username)) {
                blocked.remove(i);
                return true;
            }
        }
        return false;
    }


    public boolean removeFriend(String username) {
        if (Database.getUserByUsername(username) == null) {
            return false;
        }
        for (int i = 0; i < this.friends.size(); i++) {
            if (this.friends.get(i).equals(username)) {
                this.friends.remove(i);
                Database.getUserByUsername(username).removeFriend(this.getUsername());
                return true;
            }
        }
        return false;
    }




    public boolean isBlocked(User username) {
        for (String s : blocked) {
            if (s.equals(username.getUsername())) {
                return true;
            }
        }
        return false;
    }


    public boolean isFriend(String username) {
        for (String friend : friends) {
            if (friend.equals(username)) {
                return true;
            }
        }
        return false;
    }




    public String getBlockedString() {
        StringBuilder returnValue = new StringBuilder();
        for (String blockedUsername : blocked) {
            returnValue.append(blockedUsername).append(",");
        }
        return returnValue.toString();
    }


    public String getFriendsString() {
        StringBuilder returnValue = new StringBuilder();
        for (String friendUsername : friends) {
            returnValue.append(friendUsername).append(",");
        }
        return returnValue.toString();
    }


    @Override
    public String toString() {
        String tempBlocked = this.getBlockedString();
        String tempFriends = this.getFriendsString();
        if (this.blocked.isEmpty()) {
            tempBlocked = "none";
        }
        if (this.friends.isEmpty()) {
            tempFriends = "none";
        }

        String publicString = "true";
        if (!publicMode) {
            publicString = "false";
        }

        return String.format("%s\n" + "%s\n" + "%s\n" + "%s\n" + "%s\n" + "%s\n" + "%s\n",
                this.getUsername(), this.getPassword(), this.getBio(), tempBlocked, tempFriends, this.getProfilePhoto(), publicString);
    }
}


