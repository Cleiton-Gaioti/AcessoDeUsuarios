package com.cleiton.gerenciar.model;

import com.cleiton.gerenciar.presenter.PrincipalPresenter;

public class UsuarioDeslogadoState extends LoginState {

    public UsuarioDeslogadoState(PrincipalPresenter principalPresenter) {
        super(principalPresenter);
    }

    @Override
    public void logar(UserModel userType) {
        // this.getPrincipalPresenter().setEstado(new );
        if(Administrador.class.isInstance(userType)) {
            getPrincipalPresenter().setEstado(new AdministradorLogadoState(getPrincipalPresenter()));
        } else {
            getPrincipalPresenter().setEstado(new UsuarioLogadoState(getPrincipalPresenter()));
        }
    }
    
}
