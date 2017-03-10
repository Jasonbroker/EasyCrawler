package com.company;

import javax.swing.*;

/**
 * Created by jason on 15/01/2017.
 */
public class TypeWindow {
    private JTextArea textArea1;
    private JPanel panel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("TypeWindow");
        frame.setContentPane(new TypeWindow().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
