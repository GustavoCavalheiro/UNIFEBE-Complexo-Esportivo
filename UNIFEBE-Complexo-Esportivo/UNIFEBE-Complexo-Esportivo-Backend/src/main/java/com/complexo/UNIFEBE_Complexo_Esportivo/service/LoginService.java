package com.complexo.UNIFEBE_Complexo_Esportivo.service;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Login;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private Login dao = new Login();

    public boolean validarEntrada(int matricula, String senha) {
        if (dao.validarLogin(matricula, senha)) {
            return true;
        } else {
            return false;
        }
    }

}
