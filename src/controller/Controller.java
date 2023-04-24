package controller;

import boundary.MainFrame;

public class Controller {
    private MainFrame view;

    public Controller(Client client) {
        view = new MainFrame(this, client);
    }

    public MainFrame getView() {
        return view;
    }

    public void buttonPressed() {

    }

    public void testUpdateGUI(String text, String from_user) {
        view.testUpdateGUI(text, from_user);
    }
}
