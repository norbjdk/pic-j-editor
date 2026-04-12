package com.norbjdk.picjeditor.ui.model;

public interface Presentable {
    void initComponents();
    void setupComponents();
    void setupStyle();
    void setupLayout();
    void setupEventListeners();
    void setupEventHandlers();

    default void present() {
        initComponents();
        setupComponents();
        setupStyle();
        setupLayout();
        setupEventListeners();
        setupEventHandlers();
    }
}
