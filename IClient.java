import java.io.IOException;

import javax.swing.ImageIcon;

/**
 * Interface for the Chat class
 */
interface IClient {
    /* There is nothing in this interface, except for helper
     * methods since Client only has a public void run() 
     * method from Runnable, and a main method.
     */
    public void mainMenu(String thisUsername, int activeUserID) throws IOException;

    public void setUserSearched(String username);

    public ImageIcon base64ToImageIcon(String base64String);
}
