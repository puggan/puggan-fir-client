package Puggan.Fir.Windows;

import Puggan.Fir.Components.Pawn;
import Puggan.Fir.Data.Token;
import Puggan.Fir.Main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame implements ActionListener
{
    private JLabel status_label;
    private Puggan.Fir.Data.Game game;
    private Token token;
    private List<Pawn> pawns;
    private Timer timer;
    private ActionListener listener;
    private List<Puggan.Fir.Data.Pawn> data_pawns;

    public Game(Token token, Puggan.Fir.Data.Game game) throws IOException
    {
        this.token = token;
        this.game = game;
        timer = new Timer(15000, this);

        String title = game.Player1 + " vs " + game.Player2;
        setTitle(title);
        JLabel windows_label = new JLabel(title);
        status_label = new JLabel(game.Status);

        setSize(new Dimension(500, 600));
        setLayout(new GridBagLayout());
        add(windows_label, Main.BagPosition(0, 0, 7, 1));
        add(status_label, Main.BagPosition(0, 1, 7, 1));

        pawns = new ArrayList<>();
        for (int y_pos = 5; y_pos >= 0; y_pos--)
        {
            for (int x_pos = 0; x_pos < 7; x_pos++)
            {
                Pawn pawn = new Pawn(x_pos, y_pos);
                pawns.add(pawn);
                add(pawn, Main.BagPosition(x_pos, 7 - y_pos, 1, 1));
            }
        }

        update_pawns();

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void update_pawns() throws IOException
    {
        int[] col_high = {0, 0, 0, 0, 0, 0, 0};
        int size = -1;
        if (data_pawns != null)
        {
            size = data_pawns.size();
        }
        data_pawns = Puggan.Fir.Data.Pawn.load_game(game);
        if (size == data_pawns.size())
        {
            game_timer();
            return;
        }

        for (Puggan.Fir.Data.Pawn pawn : data_pawns)
        {
            int position = pawn.X - 7 * pawn.Y + 35;
            Pawn current = pawns.get(position);
            current.link_pawn(pawn);

            if (col_high[pawn.X] <= pawn.Y)
            {
                col_high[pawn.X] = pawn.Y + 1;
            }
        }

        Puggan.Fir.Data.Game old_game = game;
        try
        {
            game = Puggan.Fir.Data.Game.load(game.Game_ID);
            status_label.setText(game.Status);
        }
        catch (IOException e)
        {
            game = old_game;
            data_pawns = null;
            game_timer();
            throw e;
        }

        boolean playable = game.playable(token.Player_ID);
        for (int x = 0; x < 7; x++)
        {
            int y = col_high[x];
            if (y < 6)
            {
                int position = x - 7 * y + 35;
                Pawn current = pawns.get(position);
                current.set_playable(playable);
                if (playable)
                {
                    current.setActionListener(this);
                }
                else
                {
                    current.setActionListener(null);
                }
            }
        }

        game_timer();
    }

    private void game_timer()
    {
        switch (game.Status)
        {
            case Puggan.Fir.Data.Game.WAIT1:
            case Puggan.Fir.Data.Game.WAIT2:
                if (game.playable(token.Player_ID))
                {
                    timer.stop();
                }
                else
                {
                    timer.start();
                }
                break;

            default:
                timer.stop();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object source = actionEvent.getSource();
        if (source == timer)
        {
            try
            {
                timer.stop();
                update_pawns();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return;
        }
        if (source instanceof Pawn)
        {
            Pawn pawn = (Pawn) source;
            Puggan.Fir.Data.Game old_game = game;
            try
            {
                game = pawn.play(game, token.Token);
                status_label.setText(game.Status);
                update_pawns();
                if (listener != null)
                {
                    listener.actionPerformed(new ActionEvent(this, 0, "Played"));
                }
            }
            catch (IOException e)
            {
                game = old_game;
                e.printStackTrace();
            }
        }
    }

    public void addActionListener(ActionListener listner)
    {
        this.listener = listner;
    }
}
