package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.estrutura.Sexo;
import com.example.fernando.appcivico.estrutura.Usuario;
import com.example.fernando.appcivico.mask.MaskEditTextChangedListener;
import com.example.fernando.appcivico.servicos.ServicosCadastro;
import com.example.fernando.appcivico.utils.MyAlertDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fernando on 03/10/16.
 */
public class CadastroFragment extends Fragment {
    private Button buttonEnviar;
    private Spinner spinnerSexo;
    private EditText editCep;
    private EditText editDataNascimento;
    private EditText editEmailUsuario;
    private EditText editSenha;
    private EditText editNomeDeUsuario;
    private EditText editNomeCompleto;
    private EditText editBiografia;
    private ServicosCadastro servicosCadastro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro,container,false);

        servicosCadastro = new ServicosCadastro(getActivity());

        editDataNascimento = (EditText)view.findViewById(R.id.edt_data_nascimento);
        MaskEditTextChangedListener maskDataNascimento = new MaskEditTextChangedListener("##/##/####", editDataNascimento);
        editDataNascimento.addTextChangedListener(maskDataNascimento);

        editCep = (EditText)view.findViewById(R.id.edt_cep);
        MaskEditTextChangedListener maskCep = new MaskEditTextChangedListener("##.###-###", editCep);
        editCep.addTextChangedListener(maskCep);

        spinnerSexo = (Spinner)view.findViewById(R.id.spinner_sexo);

        Sexo sexo = new Sexo();

        ArrayAdapter<Sexo> arrayAdapter = new ArrayAdapter(this.getActivity(),R.layout.simple_spinner_item_custom,sexo.getArraylistSexo());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(arrayAdapter);

        editEmailUsuario = (EditText)view.findViewById(R.id.edt_email_usuario);
        editSenha = (EditText)view.findViewById(R.id.edt_senha);
        editNomeDeUsuario = (EditText)view.findViewById(R.id.edt_nome_usuario);
        editNomeCompleto = (EditText)view.findViewById(R.id.edt_nome_completo);
        editBiografia = (EditText)view.findViewById(R.id.edt_biografia);

        buttonEnviar = (Button)view.findViewById(R.id.button_enviar);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaCampos()) {
                    Usuario usuario = getUsuarioPopulado();

                    final MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance("", "Enviando os dados...");
                    myAlertDialogFragment.show(getFragmentManager(),"");

                    servicosCadastro.cadastraPessoa(usuario);
                    servicosCadastro.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            myAlertDialogFragment.dismiss();
                        }
                    });
                }
            }
        });



        return view;
    }

    private Boolean validaCampos() {
        Boolean valid = true;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(editEmailUsuario.getText().toString());

        if(editEmailUsuario.getText().toString().isEmpty()) {
            editEmailUsuario.setError(this.getString(R.string.campo_obrigatorio));
            valid = false;
        }else if(!matcher.matches()) {
            editEmailUsuario.setError(this.getString(R.string.email_invalido));
            valid = false;
        }



        if(editSenha.getText().toString().isEmpty()) {
            editSenha.setError(this.getString(R.string.campo_obrigatorio));
            valid = false;
        }

        if(editNomeDeUsuario.getText().toString().isEmpty()) {
            editNomeDeUsuario.setError(this.getString(R.string.campo_obrigatorio));
            valid = false;
        }

        return valid;
    }

    private Usuario getUsuarioPopulado() {
        String dataDeNascimento = "";

        try {
            String dataNascimentoString = editDataNascimento.getText().toString();
            if(!dataNascimentoString.isEmpty()) {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoString);
                dataDeNascimento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Sexo sexoSelecionado = (Sexo)spinnerSexo.getSelectedItem();

        Usuario usuario = new Usuario();

        if(!editBiografia.getText().toString().isEmpty() ) {
            usuario.setBiografia(editBiografia.getText().toString());
        }

        if(!(editCep.getText().toString().isEmpty())) {
            usuario.setCep((editCep.getText().toString().replace(".", "")).replace("-", ""));
        }

        if(!dataDeNascimento.isEmpty()) {
            usuario.setDataNascimento(dataDeNascimento);
        }

        usuario.setEmail(editEmailUsuario.getText().toString());

        if(!editNomeCompleto.getText().toString().isEmpty()) {
            usuario.setNomeCompleto(editNomeCompleto.getText().toString());
        }

        usuario.setNomeUsuario(editNomeDeUsuario.getText().toString());
        usuario.setSenha(editSenha.getText().toString());

        if(!sexoSelecionado.getId().isEmpty()) {
            usuario.setSexo(sexoSelecionado.getId());
        }


        return usuario;
    }

}
