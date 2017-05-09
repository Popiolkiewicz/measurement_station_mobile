package org.ms.mobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ms.mobile.R;
import org.ms.mobile.connection.ConnectionConstants;
import org.ms.mobile.connection.WebServiceTask;
import org.ms.mobile.connection.EncryptUnit;
import org.ms.mobile.connection.OnResult;
import org.ms.mobile.conversion.Parser;
import org.ms.mobile.model.UserDTO;
import org.ms.mobile.sp.SPUtil;
import org.ms.mobile.ui.activity.MainActivity;

public class LoginFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        if (getView() != null) {
            final EditText loginField = (EditText) getView().findViewById(R.id.loginField);
            final EditText passwordField = (EditText) getView().findViewById(R.id.passwordField);
            final Button loginButton = (Button) getView().findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login(loginField.getText().toString(), passwordField.getText().toString());
                }
            });
        }
    }

    private void login(final String login, String password) {
        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Login/hasło nie mogą być puste!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String encryptedPassword = EncryptUnit.md5(password);
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(login);
        userDTO.setPassword(encryptedPassword);
        new WebServiceTask(ConnectionConstants.USER_LOGIN, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                loginSuccess(new Parser().getLoginRequestDTO(result));
            }

            @Override
            public void onFailure(String result) {
                super.onFailure(result);
                Toast.makeText(getContext(), "Niepoprawny login lub hasło!", Toast.LENGTH_LONG).show();
            }
        }).showProgressDialog().execute(new Parser().toJSON(userDTO));
    }

    private void loginSuccess(UserDTO userDTO) {
        SPUtil.applyLoginData(getContext(), userDTO);
        Toast.makeText(getContext(), getString(R.string.log_in_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        ((Activity) getContext()).finish();
    }
}
