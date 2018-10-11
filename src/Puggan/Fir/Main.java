
package Puggan.Fir;

import Puggan.Fir.Windows.Login;

import javax.swing.JFrame;
import java.awt.GridBagConstraints;

public class Main {
    public static JFrame current_window;

    public static void main(String[] args) {
        current_window = new Login();
    }

    public static GridBagConstraints BagPosition(int x, int y, int w, int h)
    {
        GridBagConstraints pos = new GridBagConstraints();
        pos.gridx = x;
        pos.gridy = y;
        pos.gridwidth = w;
        pos.gridheight = h;
        pos.fill = GridBagConstraints.HORIZONTAL;
        return pos;
    }
}
