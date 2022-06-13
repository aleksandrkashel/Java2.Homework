package com.geekbrains.clientchat.controllers;


import com.example.command.Command;
import com.example.command.CommandType;
import com.example.command.commands.AuthOkCommandData;
import com.geekbrains.clientchat.ClientChat;
import com.geekbrains.clientchat.dialogs.Dialogs;
import com.geekbrains.clientchat.model.Network;
import com.geekbrains.clientchat.model.ReadMessageListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthController {

    public static final String AUTH_COMMAND = "/auth";
    public static final String AUTH_OK_COMMAND = "/authOk";

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button authButton;

    public ReadMessageListener readMessageListener;

    @FXML
    public void executeAuth() {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || password == null || login.isBlank() || login.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }

        if (!isConnectedToServer()) {
            Dialogs.NetworkError.SERVER_CONNECT.show();
        }

        try {
            Network.getInstance().sendAuthMessage(login, password);
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
            e.printStackTrace();
        }

    }

    public void initializeMessageHandler() {
            readMessageListener = getNetwork().addReadMessageListener(new ReadMessageListener() {
                @Override

                public void processReceivedCommand(Command command) {
                    if (command.getType() == CommandType.AUTH_OK) {
                        AuthOkCommandData data = (AuthOkCommandData) command.getData();
                        String userName = data.getUserName();
                        Platform.runLater(() -> {
                            ClientChat.getInstance().switchToMainChatWindow(userName);
                        });
                    } else {
                    Platform.runLater(() -> {Dialogs.AuthError.INVALID_CREDENTIALS.show();
                                        });
                                    }
                                }
                            });
                        }

                        public boolean isConnectedToServer() {
                            Network network = getNetwork();
                            return network.isConnected() || network.connect();
                        }

                        private Network getNetwork() {
                            return Network.getInstance();
                        }

                        public void close() {
                            getNetwork().removeReadMessageListener(readMessageListener);
                        }
                    }
