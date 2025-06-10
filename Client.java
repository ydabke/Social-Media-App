
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Base64;
import javax.swing.*;

public class Client implements Runnable, IClient {

    JFrame frame;
    Container content;
    String userSearched;
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        SwingUtilities.invokeLater(new Client());
    }

    @Override
    // Runs the login screen, calls mainMenu() when logged in
    public void run() {
        String host = "localhost";
        int port = 4242;
        try { //connects client to the server
            socket = new Socket(host, port);
            JOptionPane.showMessageDialog(null, "Connected to the server.", "Success", JOptionPane.INFORMATION_MESSAGE);
            //Define input and output
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (socket.isConnected()) {
                //Create login JFrame
                JFrame loginFrame = new JFrame("Super Messager");
                Container loginContent = loginFrame.getContentPane();
                loginContent.setLayout(new BorderLayout());
                loginContent.setBackground(new Color(83, 255, 126));
                loginFrame.setSize(600, 300);
                loginFrame.setResizable(true);
                loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                loginFrame.setVisible(true);

                //Defining elements in the login screen
                JPanel loginScreen = new JPanel();
                loginScreen.setLayout(new BoxLayout(loginScreen, BoxLayout.Y_AXIS));
                loginScreen.setBackground(new Color(145, 145, 234));

                // App Title
                JLabel appName = new JLabel("Super Messager");
                appName.setFont(new Font("SansSerif", Font.BOLD, 36));
                appName.setForeground(new Color(221, 167, 28));
                appName.setMaximumSize(new Dimension(300, 200));
                appName.setAlignmentX(JLabel.CENTER_ALIGNMENT);

                // JPanel for receiving user input for username
                JPanel usernameEntry = new JPanel();
                JLabel usernameLabel = new JLabel("Username: ");
                JTextField usernameField = new JTextField(24);
                usernameEntry.add(usernameLabel);
                usernameEntry.add(usernameField);
                usernameEntry.setMaximumSize(new Dimension(400, 100));
                usernameEntry.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                usernameEntry.setBackground(new Color(58, 167, 221));

                // JPanel for receiving user input for password
                JPanel passwordEntry = new JPanel();
                JLabel passwordLabel = new JLabel("Password: ");
                JPasswordField passwordField = new JPasswordField(24);
                passwordEntry.add(passwordLabel);
                passwordEntry.add(passwordField);
                passwordEntry.setMaximumSize(new Dimension(400, 100));
                passwordEntry.setBackground(new Color(58, 167, 221));

                // Two buttons to either log in or sign up, login calls mainMenu(), signup opens signup screen
                JPanel loginButtons = new JPanel();
                JButton loginButton = new JButton("Log In");
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = new String(passwordField.getPassword());
                        if (username.equals("")) {
                            JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Invalid username", JOptionPane.ERROR_MESSAGE);
                            usernameField.setText("");
                            passwordField.setText("");
                        } else if (password.equals("")) {
                            JOptionPane.showMessageDialog(null, "Password cannot be empty!", "Invalid password", JOptionPane.ERROR_MESSAGE);
                            usernameField.setText("");
                            passwordField.setText("");
                        } else {
                            out.println("1" + username + "," + password);
                            try {
                                int validLogin = Integer.parseInt(in.readLine());
                                if (validLogin > 0) {
                                    JOptionPane.showMessageDialog(null, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    loginFrame.dispose();
                                    mainMenu(username, validLogin);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Incorrect login details!", "Invalid login", JOptionPane.ERROR_MESSAGE);
                                    usernameField.setText("");
                                    passwordField.setText("");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

                // Add the buttons to the JPanel
                JButton signupButton = new JButton("Sign Up");
                loginButtons.add(loginButton);
                loginButtons.add(signupButton);
                loginButtons.setMaximumSize(new Dimension(400, 100));
                loginButtons.setBackground(new Color(58, 167, 221));

                //Adding each component JPanel to the login screen
                loginScreen.add(appName);
                loginScreen.add(usernameEntry);
                loginScreen.add(passwordEntry);
                loginScreen.add(loginButtons);
                loginContent.add(loginScreen);

                //Creating the signup screen to take bio and pfp from user and send it to server
                ArrayList<String> signupFields = new ArrayList<>();
                JPanel signupScreen = new JPanel();
                signupScreen.setLayout(new BoxLayout(signupScreen, BoxLayout.Y_AXIS));
                signupButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = new String(passwordField.getPassword());
                        if (username.equals("")) {
                            JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Invalid username", JOptionPane.ERROR_MESSAGE);
                        } else if (password.equals("")) {
                            JOptionPane.showMessageDialog(null, "Password cannot be empty!", "Invalid password", JOptionPane.ERROR_MESSAGE);
                        } else {
                            try {
                                signupFields.clear();
                                signupFields.add(username);
                                signupFields.add(password);
                                loginContent.removeAll();
                                loginContent.add(signupScreen, BorderLayout.CENTER);
                                loginContent.repaint();
                                loginContent.revalidate();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

                // Back button to cancel signup
                JButton backtoLogin = new JButton("Back");
                backtoLogin.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loginContent.removeAll();
                        loginContent.add(loginScreen, BorderLayout.CENTER);
                        loginContent.repaint();
                        loginContent.revalidate();
                    }
                });

                //JTextArea to input bio
                JPanel bioPanel = new JPanel();
                JLabel bioLabel = new JLabel("Bio:");
                JTextArea bioArea = new JTextArea(4, 48);
                bioArea.setMaximumSize(new Dimension(600, 200));
                bioPanel.add(bioLabel);
                bioPanel.add(bioArea);

                //Checkbox for whether the account will have a profile picture
                JCheckBox pfpCheckBox = new JCheckBox("Profile Picture?");
                // Sign up button that sends provided info to server
                JButton realSignUpButton = new JButton("Sign Up");
                realSignUpButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
                //Adding all elements to signup screen
                signupScreen.add(backtoLogin);
                signupScreen.add(bioPanel);
                signupScreen.add(pfpCheckBox);
                signupScreen.add(realSignUpButton);
                backtoLogin.setAlignmentX(JButton.CENTER_ALIGNMENT);
                bioPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
                realSignUpButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean success = false;
                        //Remove dangerous chars from bio
                        bioArea.setText(bioArea.getText().replaceAll("\n", ";"));
                        bioArea.setText(bioArea.getText().replaceAll(",", "，"));
                        if (bioArea.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Bio cannot be empty!", "Invalid bio", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Adding bio and getting picture input
                            signupFields.add(bioArea.getText());
                            String photoBase64 = null;
                            boolean picture = pfpCheckBox.isSelected();
                            if (picture) {
                                JFileChooser fileChooser = new JFileChooser();
                                fileChooser.setDialogTitle("Choose a profile photo");
                                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
                                int fileResult = fileChooser.showOpenDialog(null);

                                if (fileResult == JFileChooser.APPROVE_OPTION) {
                                    File selectedFile = fileChooser.getSelectedFile();
                                    try (FileInputStream fileInputStream = new FileInputStream(selectedFile)) {
                                        byte[] fileBytes = new byte[(int) selectedFile.length()];
                                        fileInputStream.read(fileBytes);
                                        photoBase64 = Base64.getEncoder().encodeToString(fileBytes);  // Encoding the photo into base64
                                        signupFields.add(photoBase64);
                                        out.println("2" + signupFields.get(0) + "," + signupFields.get(1) + "," + signupFields.get(2) + "," + signupFields.get(3));
                                        success = Boolean.parseBoolean(in.readLine());
                                    } catch (IOException ex) {
                                        JOptionPane.showMessageDialog(null, "Error reading the image file.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                // Try to create the new user
                                out.println("2" + signupFields.get(0) + "," + signupFields.get(1) + "," + signupFields.get(2));
                                try {
                                    success = Boolean.parseBoolean(in.readLine());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        //Returns message on success or failure, then kick back to login screen
                        if (success) {
                            JOptionPane.showMessageDialog(null, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Could not create user.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        loginContent.removeAll();
                        loginContent.add(loginScreen, BorderLayout.CENTER);
                        loginFrame.repaint();
                        loginFrame.revalidate();
                    }
                });
            }
        } catch (IOException e) { //This should never happen because the port number is hardcoded
            JOptionPane.showMessageDialog(null, "Could not connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // JFrame that represents the main menu and chat windows
    public void mainMenu(String thisUsername, int activeUserID) throws IOException {
        // Defining main menu frame
        frame = new JFrame(thisUsername);
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(83, 255, 126));
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        //Main menu panel that shows the user's profile, friends & blocked
        JPanel mainMenu = new JPanel(new BorderLayout());
        
        //Chat panel shows the chat window when two users are chatting
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        //Chatter info shows the username, pfp and bio of user you're talking to
        JPanel chatterInfo = new JPanel(new BorderLayout());
        JPanel chatterUserPlusImage = new JPanel();
        ImageIcon chatterPfp = new ImageIcon();
        JLabel chatterImage = new JLabel(chatterPfp);
        JLabel chatterUser = new JLabel();
        // Creating each element for profile picture, name and bio, they will be filled in later
        chatterUser.setFont(new Font("Serif", Font.BOLD, 14));
        chatterUserPlusImage.add(chatterUser);
        chatterUserPlusImage.add(chatterImage);
        JPanel chatterBio = new JPanel();
        JLabel chatterBioPrompt = new JLabel("A little bit about me: ");
        chatterBioPrompt.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel chatterBioText = new JLabel();
        chatterBio.add(chatterBioPrompt);
        chatterBio.add(chatterBioText);
        // Adding each element to the chatterInfo panel
        chatterInfo.add(chatterUserPlusImage, BorderLayout.NORTH);
        chatterInfo.add(chatterBio, BorderLayout.CENTER);
        chatPanel.add(chatterInfo, BorderLayout.EAST);

        //Titlebar to go back to main menu, and to show who you're talking to
        JPanel chatTitle = new JPanel();
        chatTitle.setLayout(new BorderLayout());
        JButton chatExit = new JButton("Back");
        JLabel chatterName = new JLabel();
        chatTitle.add(chatExit, BorderLayout.WEST);
        chatTitle.add(chatterName, BorderLayout.CENTER);
        chatPanel.add(chatTitle, BorderLayout.NORTH);

        //JTextArea to show the chat history
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        //Thread takes in chat history from the server every second, and updates chatArea
        Runnable chatUpdateRunnable = new Runnable() {
            public void run() {
                try {
                    String chatLog = in.readLine().replaceAll(",,,", "\n");
                    while (!chatLog.equals("end")) { // Keeps taking messages from the user
                        chatLog = in.readLine().replaceAll(",,,", "\n");
                        chatArea.setText(chatLog);
                        JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
                        scrollBar.setValue(scrollBar.getMaximum());
                        chatArea.repaint();
                        chatArea.revalidate();
                    }
                    System.out.println("ending");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //ArrayList used so that it can be set() and get() inside an ActionListener
        ArrayList<Thread> chatUpdate = new ArrayList<>();
        chatUpdate.add(new Thread(chatUpdateRunnable));
        //Stops thread when user quits out
        chatExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("quit");
                chatUpdate.get(0).interrupt();
                content.removeAll();
                content.add(mainMenu);
                content.repaint();
                content.revalidate();
            }
        });

        //Bottom panel to send text input from user
        JPanel sendPanel = new JPanel(new BorderLayout());
        JTextField messageBox = new JTextField(64);
        JButton sendButton = new JButton("Send");
        //Only sends non empty messages
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!messageBox.getText().equals("")) {
                    out.println("continue," + messageBox.getText());
                }
                messageBox.setText("");
            }
        });
        sendPanel.add(messageBox, BorderLayout.WEST);
        sendPanel.add(sendButton, BorderLayout.CENTER);
        chatPanel.add(sendPanel, BorderLayout.SOUTH);

        //Code for main menu

        //Private mode button, checks current status and updates it
        JButton setPrivate = new JButton("Status: Public");
        out.println("d" + thisUsername);
        if (!Boolean.parseBoolean(in.readLine())) {
            setPrivate.setText("Status: Private");
        }
        //Toggles the user's status when pressed
        setPrivate.addActionListener((ActionEvent e) -> {
            if (setPrivate.getActionCommand().equals("Status: Public")) {
                setPrivate.setText("Status: Private");
                out.println("f" + thisUsername + "," + "false");
            } else if (setPrivate.getActionCommand().equals("Status: Private")) {
                setPrivate.setText("Status: Public");
                out.println("f" + thisUsername + "," + "true");
            }
        });
        //Button to close the program when done, closing the server connection and frame
        JButton signOut = new JButton("Quit");
        signOut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to quit?",
                            "Exit",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        out.println("exit");
                        socket.close();
                        out.close();
                        in.close();
                        JOptionPane.showMessageDialog(null, "Thank you for using Super Messager!", "Exiting client", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Code to get profile picture from server
        out.println("b" + "," + thisUsername);

        String P = in.readLine();
        ImageIcon iconCurrentClient = base64ToImageIcon(P);

        // scale image
        Image image = iconCurrentClient.getImage(); // transform it
        Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        iconCurrentClient = new ImageIcon(newimg);

        // Username label for top bar
        JLabel userLabel = new JLabel(thisUsername);
        userLabel.setFont(new Font("Serif", Font.ROMAN_BASELINE, 24));

        //User panel at the top to show user info
        JPanel userPanel = new JPanel(new BorderLayout());
        JPanel userLeft = new JPanel();
        JPanel userRight = new JPanel();
        Color userPanelColor = new Color(41, 127, 63);
        //Put username and profile picture on the left, and the status button on the right
        userPanel.setBackground(userPanelColor);
        userLeft.setBackground(userPanelColor);
        userRight.setBackground(userPanelColor);
        userLeft.add(new JLabel(iconCurrentClient));
        userLeft.add(userLabel);
        JButton cardNextButton = new JButton("View Next Page");
        userRight.add(cardNextButton);
        userRight.add(setPrivate);
        userRight.add(signOut);
        userPanel.add(userLeft, BorderLayout.WEST);
        userPanel.add(userRight, BorderLayout.EAST);
        mainMenu.add(userPanel, BorderLayout.NORTH);

        //Card layout in the center to show 3 cards:
        //Friends, blocked and chats
        CardLayout card = new CardLayout();
        JPanel centerPanel = new JPanel(card);
        JPanel friends = new JPanel();
        JPanel blocked = new JPanel();
        JPanel chats = new JPanel();
        //Update each card whenever the button is pressed to progress
        ActionListener cardNext = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Copy of the update code that will appear later
                try {
                    out.println("6" + "," + activeUserID);
                    ArrayList<JPanel> friendList = new ArrayList<>();
                    int i = 0;
                    friends.removeAll();
                    JLabel tabLabelF = new JLabel("Your Friends");
                    tabLabelF.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    friends.add(tabLabelF);
                    String f = in.readLine();
                    if (f.length() > 1) {
                        for (String friend : f.split(",")) {
                            friendList.add(new JPanel());
                            friendList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                            friendList.get(i).add(new JLabel(friend));
                            friends.add(friendList.get(i));
                            i++;
                        }
                    }

                    out.println("9" + "," + activeUserID);
                    ArrayList<JPanel> blockedList = new ArrayList<>();
                    i = 0;
                    blocked.removeAll();
                    JLabel tabLabelB = new JLabel("Your Blocked");
                    tabLabelB.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    blocked.add(tabLabelB);
                    String b = in.readLine();
                    if (b.length() > 1) {
                        for (String blocks : b.split(",")) {
                            blockedList.add(new JPanel());
                            blockedList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                            blockedList.get(i).add(new JLabel(blocks));
                            blocked.add(blockedList.get(i));
                            i++;
                        }
                    }

                    out.println("a" + "," + activeUserID);
                    ArrayList<JButton> chatList = new ArrayList<>();
                    i = 0;
                    chats.removeAll();
                    JLabel tabLabelC = new JLabel("Your Chats");
                    tabLabelC.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                    chats.add(tabLabelC);
                    String c = in.readLine();
                    if (c.length() > 1) {
                        for (String chat : c.split(",")) {
                            chatList.add(new JButton(chat));
                            chatList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                            chatList.get(i).setAlignmentX(JButton.CENTER_ALIGNMENT);
                            chatList.get(i).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        chatterUser.setText(e.getActionCommand());
                                        out.println("b" + "," + e.getActionCommand());
                                        String P = in.readLine();
                                        // scale image
                                        Image image = base64ToImageIcon(P).getImage(); // transform it
                                        Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                                        chatterPfp.setImage(newimg);
                                        out.println("g" + e.getActionCommand());
                                        chatterBioText.setText(in.readLine());
                                        out.println("3" + thisUsername + "," + e.getActionCommand());
                                        boolean validReceiver = Boolean.parseBoolean(
                                                in.readLine()); // Checks if the receiver is a user in the database
                                        if (validReceiver) {
                                            content.removeAll();
                                            content.add(chatPanel);
                                            chatterName.setText("Chatting with " + e.getActionCommand());
                                            String chatLog = in.readLine().replaceAll(",,,", "\n");
                                            chatArea.setText(chatLog);
                                            JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
                                            scrollBar.setValue(scrollBar.getMaximum());
                                            content.repaint();
                                            content.revalidate();
                                            chatUpdate.set(0, new Thread(chatUpdateRunnable));
                                            chatUpdate.get(0).start();
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Can't chat with this user!", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (IOException e1) {
                                        out.println("exit");
                                    }
                                }
                            });
                            chats.add(chatList.get(i));
                            i++;
                        }
                    }

                    centerPanel.repaint();
                    centerPanel.revalidate();
                    card.next(centerPanel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        //Puts the ActionListener on the "Card Next" button
        cardNextButton.addActionListener(cardNext);

        //Scrollpane and panel for friend list
        JScrollPane friendScroll = new JScrollPane(friends);
        friends.setLayout(new BoxLayout(friends, BoxLayout.Y_AXIS));
        JLabel tabLabelF = new JLabel("Your Friends");
        tabLabelF.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        friends.add(tabLabelF);
        // Request Friend list data from server, then add it to BoxLayout panel
        out.println("6" + "," + activeUserID);
        ArrayList<JPanel> friendList = new ArrayList<>();
        int i = 0;
        String f = in.readLine();
        if (f.length() > 1) {
            for (String friend : f.split(",")) {
                friendList.add(new JPanel());
                friendList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                friendList.get(i).add(new JLabel(friend));
                friends.add(friendList.get(i));
                i++;
            }
        }

        //Scrollpane and panel for block list
        JScrollPane blockedScroll = new JScrollPane(blocked);
        blocked.setLayout(new BoxLayout(blocked, BoxLayout.Y_AXIS));
        JLabel tabLabelB = new JLabel("Your Blocked");
        tabLabelB.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        blocked.add(tabLabelB);
        // Request block list data from server, then add it to BoxLayout panel
        out.println("9" + "," + activeUserID);
        ArrayList<JPanel> blockedList = new ArrayList<>();
        i = 0;
        String b = in.readLine();
        if (b.length() > 1) {
            for (String blocks : b.split(",")) {
                blockedList.add(new JPanel());
                blockedList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                blockedList.get(i).add(new JLabel(blocks));
                blocked.add(blockedList.get(i));
                i++;
            }
        }

        // Scrollpane and panel for chat list
        JScrollPane chatScroll = new JScrollPane(chats);
        chats.setLayout(new BoxLayout(chats, BoxLayout.Y_AXIS));
        JLabel tabLabelC = new JLabel("Your Chats");
        tabLabelC.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        chats.add(tabLabelC);
        // Request chat list data from server, then add it to BoxLayout panel
        out.println("a" + "," + activeUserID);
        ArrayList<JButton> chatList = new ArrayList<>();
        i = 0;
        String c = in.readLine();
        if (c.length() > 1) {
            for (String chat : c.split(",")) {
                chatList.add(new JButton(chat));
                chatList.get(i).setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                chatList.get(i).setAlignmentX(JButton.CENTER_ALIGNMENT);
                //Action listener for each chat button to try to open the chat window
                chatList.get(i).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Gets info for user info panel and updates it
                            chatterUser.setText(e.getActionCommand());
                            out.println("b" + "," + e.getActionCommand());
                            String P = in.readLine();
                            // scale image
                            Image image = base64ToImageIcon(P).getImage(); // transform it
                            Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                            chatterPfp.setImage(newimg);
                            out.println("g" + e.getActionCommand());
                            chatterBioText.setText(in.readLine());
                            out.println("3" + thisUsername + "," + e.getActionCommand());
                            boolean validReceiver = Boolean.parseBoolean(
                                    in.readLine()); // Checks if the receiver is a user in the database
                            if (validReceiver) {
                                // Updates to the chat window if it's a valid chatter
                                content.removeAll();
                                content.add(chatPanel);
                                chatterName.setText("Chatting with " + e.getActionCommand());
                                String chatLog = in.readLine().replaceAll(",,,", "\n");
                                chatArea.setText(chatLog);
                                JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
                                scrollBar.setValue(scrollBar.getMaximum());
                                content.repaint();
                                content.revalidate();
                                chatUpdate.set(0, new Thread(chatUpdateRunnable));
                                chatUpdate.get(0).start();
                            } else {
                                JOptionPane.showMessageDialog(null, "Can't chat with this user!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException e1) {
                            out.println("exit");
                        }
                    }
                });
                chats.add(chatList.get(i));
                i++;
            }
        }
        // Add each card to the center layout
        centerPanel.add("friends", friendScroll);
        centerPanel.add("blocked", blockedScroll);
        centerPanel.add("chats", chatScroll);
        
        // Add center card to the main menu
        mainMenu.add(centerPanel, BorderLayout.CENTER);

        //Search panel, if it is a valid search it is written to userSearched
        userSearched = "";
        JTextField searchBar = new JTextField(32);
        JButton searchButton = new JButton("Search Users");
        JButton friendButton = new JButton("Add Friend");
        JButton blockButton = new JButton("Block");
        // Checks if a user is a friend, similar to setPrivate, and toggles friend status.
        // Removes blocked status if added as a friend.
        friendButton.addActionListener((ActionEvent e) -> {
            boolean worked = true;
            try {
                // Tries to add friend if not friended
                if (friendButton.getActionCommand().equals("Add Friend")) {
                    if (blockButton.getActionCommand().equals("Unblock")) {
                        out.println("8," + activeUserID + "," + userSearched);
                        worked = Boolean.parseBoolean(in.readLine());
                        if (worked) {
                            blockButton.setText("Block");
                        }
                    }
                    out.println("4," + activeUserID + "," + userSearched);
                    worked = worked && Boolean.parseBoolean(in.readLine());
                    if (worked) {
                        friendButton.setText("Remove Friend");
                    }
                // Unfriends if they are already friends
                } else if (friendButton.getActionCommand().equals("Remove Friend")) {
                    out.println("5," + activeUserID + "," + userSearched);
                    if (Boolean.parseBoolean(in.readLine())) {
                        friendButton.setText("Add Friend");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // Checks if a user is blocked, similar to setPrivate, and toggles blocked status.
        // Removes friend status if blocking.
        blockButton.addActionListener((ActionEvent e) -> {
            boolean worked = true;
            try {
                //Blocks user if not currently blocked
                if (blockButton.getActionCommand().equals("Block")) {
                    if (friendButton.getActionCommand().equals("Remove Friend")) {
                        out.println("5," + activeUserID + "," + userSearched);
                        worked = Boolean.parseBoolean(in.readLine());
                        if (worked) {
                            friendButton.setText("Add Friend");
                        }
                    }
                    out.println("7," + activeUserID + "," + userSearched);
                    worked = worked && Boolean.parseBoolean(in.readLine());
                    if (worked) {
                        blockButton.setText("Unblock");
                    }
                    //Unblocks user if already blocked
                } else if (blockButton.getActionCommand().equals("Unblock")) {
                    out.println("8," + activeUserID + "," + userSearched);
                    if (Boolean.parseBoolean(in.readLine())) {
                        blockButton.setText("Block");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // Same as the chat buttons in the center card, opens up the chat with searched user
        JButton chatButton = new JButton("Chat");
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chatterUser.setText(userSearched);
                    out.println("b" + "," + userSearched);
                    String P = in.readLine();
                    // scale image
                    Image image = base64ToImageIcon(P).getImage(); // transform it
                    Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                    chatterPfp.setImage(newimg);
                    out.println("g" + userSearched);
                    chatterBioText.setText(in.readLine());
                    out.println("3" + thisUsername + "," + userSearched);
                    boolean validReceiver = Boolean.parseBoolean(
                            in.readLine()); // Checks if the receiver is a user in the database
                    if (validReceiver) {
                        content.removeAll();
                        content.add(chatPanel);
                        chatterName.setText("Chatting with " + userSearched);
                        String chatLog = in.readLine().replaceAll(",,,", "\n");
                        chatArea.setText(chatLog);
                        JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
                        scrollBar.setValue(scrollBar.getMaximum());
                        content.repaint();
                        content.revalidate();
                        chatUpdate.set(0, new Thread(chatUpdateRunnable));
                        chatUpdate.get(0).start();
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't chat with this user!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e1) {
                    out.println("exit");
                }
            }
        });

        // Adds the search bar and buttons to the bottom panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel searchLeft = new JPanel();
        JPanel searchRight = new JPanel();
        Color searchPanelColor = new Color(127, 127, 166);
        searchPanel.setBackground(searchPanelColor);
        searchLeft.setBackground(searchPanelColor);
        searchRight.setBackground(searchPanelColor);
        searchLeft.add(searchBar);
        searchLeft.add(searchButton);
        searchRight.add(friendButton);
        searchRight.add(blockButton);
        searchRight.add(chatButton);
        searchPanel.add(searchLeft, BorderLayout.WEST);
        // Search panel hides the right side buttons if the user is invalid, and shows them if it is
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Checks with server
                searchBar.setText(searchBar.getText().replaceAll(",", ""));
                out.println("c," + searchBar.getText());
                try {
                    // If valid, shows buttons and sets their initial states
                    if (Boolean.parseBoolean(in.readLine())) {
                        setUserSearched(searchBar.getText());
                        searchPanel.add(searchRight, BorderLayout.EAST);
                        out.println("6," + activeUserID);
                        boolean friends = false;
                        String fList = in.readLine();
                        for (String f : fList.split(",")) {
                            if (f.equals(userSearched)) {
                                friendButton.setText("Remove Friend");
                                friends = true;
                            }
                        }
                        if (!friends) {
                            friendButton.setText("Add Friend");
                        }
                        boolean blocked = false;
                        out.println("9," + activeUserID);
                        String bList = in.readLine();
                        for (String b : bList.split(",")) {
                            if (b.equals(userSearched)) {
                                blockButton.setText("Unblock");
                                blocked = true;
                            }
                        }
                        if (!blocked) {
                            blockButton.setText("Block");
                        }
                        searchPanel.repaint();
                        searchPanel.revalidate();
                    } else {
                        // Sets bar to empty if not, and hides buttons
                        searchBar.setText("");
                        JOptionPane.showMessageDialog(null, "User doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
                        searchPanel.remove(searchRight);
                        searchPanel.repaint();
                        searchPanel.revalidate();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //Adds bottom panel to mainMenu, and loads mainMenu to the frame to start
        mainMenu.add(searchPanel, BorderLayout.SOUTH);
        content.add(mainMenu);

    }

    // Helper method to set the userSearched variable in the search bar
    public void setUserSearched(String username) {
        this.userSearched = username;
    }

    // Helper method to convert Base64
    public ImageIcon base64ToImageIcon(String base64String) {

        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        Image image = new ImageIcon(decodedBytes).getImage();

        return new ImageIcon(image);

    }
}
