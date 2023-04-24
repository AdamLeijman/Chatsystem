package controller;

import boundary.MainFrame;

import javax.swing.*;

public class Controller {
    private MainFrame view;

    public Controller(Client client) {
        view = new MainFrame(this, client);
    }

    public MainFrame getView() {
        return view;
    }


}
