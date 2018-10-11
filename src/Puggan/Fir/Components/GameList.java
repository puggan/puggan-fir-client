package Puggan.Fir.Components;

import Puggan.Fir.Data.Game;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.util.List;

public class GameList extends JList
{
    private List<Game> games;
    private DefaultListModel<String> model;
    private int player_id;

    public GameList(List<Game> games, int player_id)
    {
        this(new DefaultListModel<String>());
        this.player_id = player_id;
        replace_list(games);
    }

    public void replace_list(List<Game> games)
    {
        this.games = games;
        model.removeAllElements();
        for (Game game : games)
        {
            String prefix;
            switch (game.Status)
            {
                case Game.WAIT1:
                case Game.WAIT2:
                    if (game.playable(player_id))
                    {
                        prefix = "[P] ";
                        break;
                    }
                    prefix = "[W] ";
                    break;

                case Game.WON1:
                    if (game.Player1_ID == player_id)
                    {
                        prefix = "[V] ";
                        break;
                    }
                    prefix = "[L] ";
                    break;

                case Game.WON2:
                    if (game.Player2_ID == player_id)
                    {
                        prefix = "[V] ";
                        break;
                    }
                    prefix = "[L] ";
                    break;

                case Game.TIE:
                default:
                    prefix = "[=] ";
                    break;
            }
            model.addElement(prefix + game.Player1 + " vs " + game.Player2 + " @ " + game.Start_Time);
        }
    }

    private GameList(DefaultListModel<String> model)
    {
        super(model);
        this.model = model;
    }

    public Game get_selected_game()
    {
        return games.get(getSelectedIndex());
    }
}
