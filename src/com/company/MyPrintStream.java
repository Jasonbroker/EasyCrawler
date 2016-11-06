package com.company;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by jason on 06/11/2016.
 */
public class MyPrintStream extends PrintStream {
    private JTextComponent textComponent;
    private  StringBuffer sb = new StringBuffer();

    public MyPrintStream(OutputStream outputStream, JTextComponent textComponent) {
        super(outputStream);
        this.textComponent = textComponent;
    }

    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sb.append(message);
                textComponent.setText(sb.toString());
            }
        });
    }
}
