package main;

import view.JF_Manage_Videos;
import view.JF_Home_Page;
import controller.Controller;
import model.Model;

public class Main {

    public static void main(String[] args) {
        JF_Manage_Videos viewManage = new JF_Manage_Videos();
        JF_Home_Page viewHome = new JF_Home_Page();
        Model model = new Model();

        Controller controller = new Controller(viewManage, viewHome, model);

        viewHome.setVisible(true);
    }
}
