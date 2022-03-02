package com.cleiton.gerenciar.model;

import com.cleiton.gerenciar.presenter.PrincipalPresenter;

public abstract class LoginState {
    
    private PrincipalPresenter principalPresenter;

    public LoginState(PrincipalPresenter principalPresenter) {
        setPrincipalPresenter(principalPresenter);
    }

    public void logar(UserModel userType) {
        throw new RuntimeException("Erro ao realizar login.");
    }

    public void deslogar() {
        throw new RuntimeException("Erro ao deslogar.");
    }


    public PrincipalPresenter getPrincipalPresenter() {
        return this.principalPresenter;
    }

    private void setPrincipalPresenter(PrincipalPresenter principalPresenter) {
        this.principalPresenter = principalPresenter;
    }

}
