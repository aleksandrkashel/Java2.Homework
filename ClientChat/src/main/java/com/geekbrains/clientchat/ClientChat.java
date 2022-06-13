package com.geekbrains.clientchat;

import com.geekbrains.clientchat.controllers.AuthController;
import com.geekbrains.clientchat.controllers.ClientController;
import com.geekbrains.clientchat.model.AuthTimeout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Timer;
import java.io.IOException;

public class ClientChat extends Application {

    private static ClientChat INSTANCE;

    private Stage chatStage;
    private Stage authStage;

    private FXMLLoader chatWindowLoader;
    private FXMLLoader authLoader;

    private Timer mTimer;
    private AuthTimeout mMyTimerTask;


    @Override
    public void start(Stage primaryStage) throws IOException {
        this.chatStage = primaryStage;

        initViews();
        getChatStage().show();
        getAuthStage().show();
        getAuthController().initializeMessageHandler();
    }

    private void initViews() throws IOException {
        initChatWindow();
        initAuthDialog();
    }

    private void initChatWindow() throws IOException {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mMyTimerTask = new AuthTimeout();
        chatWindowLoader = new FXMLLoader();
        chatWindowLoader.setLocation(ClientChat.class.getResource("chat-template.fxml"));

        Parent root = chatWindowLoader.load();
        chatStage.setScene(new Scene(root));
    }

        private void initAuthDialog() throws IOException {
            authLoader = new FXMLLoader();
            authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));
            AnchorPane authDialogPanel = authLoader.load();

            authStage = new Stage();
            authStage.initOwner(chatStage);
            authStage.initModality(Modality.WINDOW_MODAL);
            authStage.setScene(new Scene(authDialogPanel));
        }


            public void switchToMainChatWindow (String userName){
                getChatStage().setTitle(userName);
                getAuthController().close();
                getAuthStage().close();
                getChatController().initializeMessageHandler();
            }


            @Override
            public void init() throws Exception {
                INSTANCE = this;
            }

            public static void main(String[] args){
                launch();
            }
            public Stage getAuthStage() {
                return authStage;
            }
            public Stage getChatStage() {
                return chatStage;
            }

            public ClientController getChatController() {
                return chatWindowLoader.getController();
            }

            public AuthController getAuthController() {
                return authLoader.getController();
            }

            public static ClientChat getInstance() {
                return INSTANCE;
            }


        }
