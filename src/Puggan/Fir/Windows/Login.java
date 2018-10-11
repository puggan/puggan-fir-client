package Puggan.Fir.Windows;

import Puggan.Fir.Data.Token;
import Puggan.Fir.Main;
import Puggan.Fir.Strings;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Login extends JFrame implements ActionListener, KeyListener
{
    private JTextField username_field;
    private JButton login_button;
    private JButton register_button;

    public Login()
    {
        setTitle(Strings.login_title() + " - " + Strings.app_name());

        JLabel windows_label = new JLabel(Strings.login_title());
        username_field = new JTextField();
        login_button = new JButton("Login");
        register_button = new JButton("Register");

        username_field.addActionListener(this);
        username_field.addKeyListener(this);
        login_button.addActionListener(this);
        register_button.addActionListener(this);
        update_action();

        setLayout(new GridBagLayout());
        add(windows_label, Main.BagPosition(0, 0, 2, 1));
        add(username_field, Main.BagPosition(0, 1, 2, 1));
        add(login_button, Main.BagPosition(0, 2, 1, 1));
        add(register_button, Main.BagPosition(1, 2, 1, 1));
        setSize(new Dimension(300, 150));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void update_action()
    {
        String username = username_field.getText();
        boolean enabled = (username.length() > 0);
        login_button.setEnabled(enabled);
        register_button.setEnabled(enabled);
    }

    public void login_action() throws IOException
    {
        String username = username_field.getText();
        logged_in(Token.auth(username));
    }

    public void register_action() throws IOException
    {
        String username = username_field.getText();
        logged_in(Token.register(username));
    }

    public void logged_in(Token token)
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        dispose();
        Main.current_window = new GameList(token);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            if (actionEvent.getSource() == login_button)
            {
                login_action();
            }
            else if (actionEvent.getSource() == register_button)
            {
                register_action();
            }
            else
            {
                update_action();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() != KeyEvent.VK_ENTER)
        {
            update_action();
            return;
        }

        try
        {
            login_action();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
