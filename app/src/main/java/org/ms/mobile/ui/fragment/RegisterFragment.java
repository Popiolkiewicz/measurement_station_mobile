package org.ms.mobile.ui.fragment;

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

/**
 * Created by Hubert on 2017-04-03.
 */
public class RegisterFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
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
            final EditText repeatPasswordField = (EditText) getView().findViewById(R.id.repeatPasswordField);
            final Button registerButton = (Button) getView().findViewById(R.id.registerButton);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    register(loginField.getText().toString(),
                            passwordField.getText().toString(),
                            repeatPasswordField.getText().toString());
                }
            });
        }
    }

    private void register(String login, String password, String repeatedPassword) {
        if (login.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()) {
            Toast.makeText(getContext(), "Nie wypełniono wszystkich pól!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repeatedPassword)) {
            Toast.makeText(getContext(), "Wprowadzone hasła różnią się!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String encryptedPassword = EncryptUnit.md5(password);
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(login);
        userDTO.setPassword(encryptedPassword);
        new WebServiceTask(ConnectionConstants.USER_REGISTER, getContext(), new OnResult() {
            @Override
            public void onSuccess(String result) {
                registerSuccess();
            }
        }).showProgressDialog().execute(new Parser().toJSON(userDTO));
    }

    private void registerSuccess() {
        Toast.makeText(getContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }

}
