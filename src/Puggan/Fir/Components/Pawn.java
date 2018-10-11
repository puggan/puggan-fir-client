package Puggan.Fir.Components;

import Puggan.Fir.Data.Game;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends JPanel implements MouseListener
{
    public int x;
    public int y;
    private Puggan.Fir.Data.Pawn pawn;
    public int margin;
    public int color_id;
    public boolean playable;
    private ActionListener listener;

    public static List<Color> colors;

    public Pawn(Puggan.Fir.Data.Pawn pawn)
    {
        this(pawn.X, pawn.Y);
        link_pawn(pawn);
    }

    public void link_pawn(Puggan.Fir.Data.Pawn pawn)
    {
        this.pawn = pawn;
        color_id = pawn.Side;
        playable = false;
        repaint();
    }

    public Pawn(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
        color_id = 0;
        margin = 5;
        playable = false;
        this.setPreferredSize(new Dimension(60, 60));
        addMouseListener(this);

        if (colors == null)
        {
            colors = new ArrayList<>();
            // 0: White
            colors.add(new Color(255, 255, 255));
            // 1: Red
            colors.add(new Color(255, 0, 0));
            // 2: Blue
            colors.add(new Color(0, 0, 255));
            // 3: Black
            colors.add(new Color(0, 0, 0));
            // 4: Yellow
            colors.add(new Color(255, 255, 0));
            // 5: Light Yellow
            colors.add(new Color(255, 255, 192));
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Dimension size = this.getSize();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colors.get(3));
        Ellipse2D.Double circle = new Ellipse2D.Double(margin, margin, size.width - 2 * margin, size.height - 2 * margin);
        g2d.setColor(colors.get(color_id));
        g2d.fill(circle);
    }

    public void set_playable(boolean playable)
    {
        this.playable = playable;
        if (playable && color_id == 0)
        {
            color_id = 5;
        }
        else if (!playable && color_id == 5)
        {
            color_id = 0;
        }
        repaint();
    }

    public void setActionListener(ActionListener listener)
    {
        this.listener = listener;
    }

    public Game play(Game game, String token) throws IOException
    {
        return game.play(token, this.x, this.y);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        if (playable && listener != null)
        {
            listener.actionPerformed(new ActionEvent(this, 0, "click"));
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {

    }
}
