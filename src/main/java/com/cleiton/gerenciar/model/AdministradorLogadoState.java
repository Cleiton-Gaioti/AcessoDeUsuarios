package com.cleiton.gerenciar.model;

import com.cleiton.gerenciar.presenter.PrincipalPresenter;

public class AdministradorLogadoState extends LoginState {

    public AdministradorLogadoState(PrincipalPresenter principalPresenter) {
        super(principalPresenter);
    }

    @Override
    public void deslogar() {
        getPrincipalPresenter().setEstado(new UsuarioDeslogadoState(getPrincipalPresenter()));
    }
    
}
