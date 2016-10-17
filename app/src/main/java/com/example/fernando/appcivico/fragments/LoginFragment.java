package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.servicos.ServicosCadastro;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.StaticFunctions;

import org.json.JSONObject;


/**
 * Created by fernando on 05/10/16.
 */
public class LoginFragment extends Fragment {
    private EditText edtEmailUsuarioLogin;
    private EditText edtSenhaLogin;
    private Button buttonEnviarLogin;
    private Servicos servicos;
    private FragmentActivity fragmentActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        servicos = new Servicos(getActivity());
        edtEmailUsuarioLogin = (EditText)view.findViewById(R.id.edt_email_usuario_login);
        edtSenhaLogin = (EditText)view.findViewById(R.id.edt_senha_login);
        buttonEnviarLogin = (Button)view.findViewById(R.id.button_enviar_login);
        fragmentActivity = this.getActivity();

        buttonEnviarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((ApplicationAppCivico) fragmentActivity.getApplication()).setUsuarioAutenticado(response.toString());
                        LoginFragment.this.getActivity().setResult(Constants.LOGIN_AUTENTICADO);
                        StaticFunctions.exibeMensagemEFecha(fragmentActivity.getString(R.string.usuario_autenticado_com_sucesso), fragmentActivity);
                    }
                };

                if(validaCampos()) {
                    String email = edtEmailUsuarioLogin.getText().toString();
                    String senha = edtSenhaLogin.getText().toString();
                    servicos.autenticarUsuario(email, senha, listener);
                }
            }
        });

        return view;
    }

    private Boolean validaCampos() {
        Boolean valid = true;
        if(edtEmailUsuarioLogin.getText().toString().isEmpty()) {
            edtEmailUsuarioLogin.setError(this.getString(R.string.campo_obrigatorio));
            valid = false;
        }

        if(edtSenhaLogin.getText().toString().isEmpty()) {
            edtSenhaLogin.setError(this.getString(R.string.campo_obrigatorio));
            valid = false;
        }

        return valid;
    }
}
