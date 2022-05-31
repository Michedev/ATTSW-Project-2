package edu.mikedev.app.task_manager_v2.controller;

import javax.swing.*;

public interface ViewController<T extends JPanel> {

    void addEvents();
    T getView();
}
