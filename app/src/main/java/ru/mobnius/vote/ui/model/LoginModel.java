package ru.mobnius.vote.ui.model;

public class LoginModel {
    public String login;
    public String password;
    public String version;

    public LoginModel() {
        version = "0.0.0.0";
        login = "";
        password = "";
    }

    public static LoginModel getInstance(String login, String password, String version) {
        LoginModel model = new LoginModel();
        model.login = login;
        model.password = password;
        model.version = version;
        return model;
    }
}
