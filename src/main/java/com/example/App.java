package com.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class App extends Application {

    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        System.out.println("Starting JavaFX application...");
        // ── Title bar ──────────────────────────────────────────────
        Label title = new Label("📋  Contact List");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#1a1a2e"));

        // ── Search box ─────────────────────────────────────────────
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search contacts…");
        searchField.setStyle(fieldStyle());
        searchField.setMaxWidth(Double.MAX_VALUE);

        // ── Table ──────────────────────────────────────────────────
        TableView<Contact> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");

        TableColumn<Contact, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Contact, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Contact, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        nameCol.setStyle("-fx-font-size: 13px;");
        phoneCol.setStyle("-fx-font-size: 13px;");
        emailCol.setStyle("-fx-font-size: 13px;");

        table.getColumns().setAll(nameCol, phoneCol, emailCol);

        // Wire search filter
        FilteredList<Contact> filtered = new FilteredList<>(contacts, c -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filtered.setPredicate(c -> {
            if (newVal == null || newVal.isBlank())
                return true;
            String lower = newVal.toLowerCase();
            return c.getName().toLowerCase().contains(lower)
                    || c.getPhone().toLowerCase().contains(lower)
                    || c.getEmail().toLowerCase().contains(lower);
        }));
        table.setItems(filtered);
        table.setPlaceholder(new Label("No contacts found."));

        // ── Input fields ───────────────────────────────────────────
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setStyle(fieldStyle());

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.setStyle(fieldStyle());

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(fieldStyle());

        // ── Buttons ────────────────────────────────────────────────
        Button addBtn = new Button("➕  Add Contact");
        Button deleteBtn = new Button("🗑  Delete Selected");
        Button clearBtn = new Button("✖  Clear Fields");

        addBtn.setStyle(primaryBtnStyle("#4361ee"));
        deleteBtn.setStyle(primaryBtnStyle("#ef233c"));
        clearBtn.setStyle(primaryBtnStyle("#6c757d"));

        // Hover effects
        addBtn.setOnMouseEntered(e -> addBtn.setStyle(primaryBtnStyle("#3a0ca3")));
        addBtn.setOnMouseExited(e -> addBtn.setStyle(primaryBtnStyle("#4361ee")));
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle(primaryBtnStyle("#b5001f")));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(primaryBtnStyle("#ef233c")));
        clearBtn.setOnMouseEntered(e -> clearBtn.setStyle(primaryBtnStyle("#495057")));
        clearBtn.setOnMouseExited(e -> clearBtn.setStyle(primaryBtnStyle("#6c757d")));

        // Status label
        Label statusLabel = new Label("");
        statusLabel.setFont(Font.font("Segoe UI", 12));
        statusLabel.setTextFill(Color.web("#4361ee"));

        // ── Button Actions ─────────────────────────────────────────
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty()) {
                showStatus(statusLabel, "⚠  Name is required.", "#e63946");
                return;
            }
            contacts.add(new Contact(name, phone, email));
            nameField.clear();
            phoneField.clear();
            emailField.clear();
            showStatus(statusLabel, "✔  Contact added successfully.", "#4caf50");
        });

        deleteBtn.setOnAction(e -> {
            Contact selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showStatus(statusLabel, "⚠  Please select a contact to delete.", "#e63946");
                return;
            }
            contacts.remove(selected);
            showStatus(statusLabel, "🗑  Contact deleted.", "#ef233c");
        });

        clearBtn.setOnAction(e -> {
            nameField.clear();
            phoneField.clear();
            emailField.clear();
            statusLabel.setText("");
        });

        // ── Layouts ────────────────────────────────────────────────
        // Input row
        HBox inputRow = new HBox(10, nameField, phoneField, emailField);
        inputRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nameField, Priority.ALWAYS);
        HBox.setHgrow(phoneField, Priority.ALWAYS);
        HBox.setHgrow(emailField, Priority.ALWAYS);

        // Button row
        HBox buttonRow = new HBox(10, addBtn, deleteBtn, clearBtn, statusLabel);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        // Form card
        VBox formCard = new VBox(10, sectionLabel("Add New Contact"), inputRow, buttonRow);
        formCard.setPadding(new Insets(16));
        formCard.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; "
                + "-fx-border-radius: 10; -fx-background-radius: 10;");

        // Table section
        VBox tableSection = new VBox(10, sectionLabel("All Contacts  (" + contacts.size() + ")"), searchField, table);
        contacts.addListener(
                (javafx.collections.ListChangeListener<Contact>) c -> ((Label) tableSection.getChildren().get(0))
                        .setText("All Contacts  (" + contacts.size() + ")"));
        VBox.setVgrow(table, Priority.ALWAYS);

        // Root
        VBox root = new VBox(20, title, formCard, tableSection);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f0f2f5;");
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        Scene scene = new Scene(root, 780, 560);
        stage.setTitle("Contact List App");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(460);
        stage.show();
    }

    // ── Helpers ────────────────────────────────────────────────────
    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lbl.setTextFill(Color.web("#343a40"));
        return lbl;
    }

    private String fieldStyle() {
        return "-fx-font-size: 13px; -fx-padding: 8 12; "
                + "-fx-border-color: #ced4da; -fx-border-radius: 6; "
                + "-fx-background-radius: 6; -fx-background-color: #ffffff;";
    }

    private String primaryBtnStyle(String hex) {
        return "-fx-background-color: " + hex + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-padding: 8 16; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand;";
    }

    private void showStatus(Label label, String msg, String color) {
        label.setText(msg);
        label.setTextFill(Color.web(color));
    }

    public static void main(String[] args) {
        System.out.println("Launching application...");
        launch();
    }
}