package com.cleiton.gerenciar.model;

import com.cleiton.gerenciar.presenter.PrincipalPresenter;

public class UsuarioLogadoState extends LoginState {

    public UsuarioLogadoState(PrincipalPresenter principalPresenter) {
        super(principalPresenter);
    }
 
    @Override
    public void deslogar() {
        getPrincipalPresenter().setEstado(new UsuarioLogadoState(getPrincipalPresenter()));
    }
}
