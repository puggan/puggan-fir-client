package Puggan.Fir.Windows;

import Puggan.Fir.Data.Game;
import Puggan.Fir.Data.Token;
import Puggan.Fir.Main;
import Puggan.Fir.Strings;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameList extends JFrame implements ActionListener, ListSelectionListener
{
    private Token token;
    private List<Puggan.Fir.Windows.Game> games;

    private JButton show_button;
    private JButton challenge_button;
    private Puggan.Fir.Components.GameList list;
    private Timer timer;

    public GameList(Token token)
    {
        this.token = token;
        games = new ArrayList<>();
        timer = new Timer(60000, this);

        setTitle(Strings.list_title() + " - " + Strings.app_name());
        JLabel windows_label = new JLabel(Strings.login_title());
        show_button = new JButton(Strings.show_game());
        show_button.addActionListener(this);
        challenge_button = new JButton(Strings.challenge());
        challenge_button.addActionListener(this);

        List<Game> available_games;
        try
        {
            available_games = Game.player_games(token.Player_ID);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
            available_games = new ArrayList<>();
        }
        list = new Puggan.Fir.Components.GameList(available_games, token.Player_ID);
        list.addListSelectionListener(this);

        setLayout(new GridBagLayout());
        add(windows_label, Main.BagPosition(0, 0, 1, 1));
        add(list, Main.BagPosition(0, 1, 1, 3));
        add(show_button, Main.BagPosition(0, 4, 1, 1));
        add(challenge_button, Main.BagPosition(0, 5, 1, 1));
        setSize(new Dimension(300, 600));
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timer.start();
    }

    public void open_game(Game game) throws IOException
    {
        Puggan.Fir.Windows.Game game_window = new Puggan.Fir.Windows.Game(token, game);
        games.add(game_window);
        game_window.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object source = actionEvent.getSource();
        try
        {
            if (source == challenge_button)
            {
                start_challange();
                return;
            }

            if (source == show_button)
            {
                show_game();
                return;
            }

            if(source == timer)
            {
                replace_list();
                return;
            }

            if(source instanceof Puggan.Fir.Windows.Game)
            {
                timer.stop();
                replace_list();
                timer.start();
                return;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void replace_list() throws IOException
    {
        list.replace_list(Game.player_games(token.Player_ID));
    }

    public void show_game() throws IOException
    {
        open_game(list.get_selected_game());
    }

    public void start_challange() throws IOException
    {
        String username = JOptionPane.showInputDialog(Strings.challenge_question());
        if (username.length() < 1)
        {
            return;
        }

        open_game(Game.challenge(token.Token, username));
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent)
    {

    }
}
