package Puggan.Fir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplitInput extends InputStream
{
    private InputStream real_input;
    private StringBuffer extra_input;

    public SplitInput(InputStream input)
    {
        super();
        real_input = input;
        extra_input = new StringBuffer();
    }

    @Override
    public int read() throws IOException
    {
        int int_value = real_input.read();
        if (int_value > 0)
        {
            extra_input.append((char) int_value);
        }
        return int_value;
    }

    @Override
    public String toString()
    {
        return extra_input.toString();
    }
}
