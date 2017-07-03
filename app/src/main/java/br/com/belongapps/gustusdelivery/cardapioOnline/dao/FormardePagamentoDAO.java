package br.com.belongapps.gustusdelivery.cardapioOnline.dao;


import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.gustusdelivery.R;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.FormadePagamento;

public class FormardePagamentoDAO {

    public static List<FormadePagamento> getFormasdePagamento() {
        List<FormadePagamento> formas = new ArrayList<>();

        FormadePagamento dinheiro = new FormadePagamento();
        dinheiro.setNome("DINHEIRO");
        dinheiro.setDescricao("");
        dinheiro.setImagem(R.drawable.ic_money);
        dinheiro.setStatus(false);

        FormadePagamento cartao = new FormadePagamento();
        cartao.setNome("CARTÃO");
        cartao.setDescricao("");
        cartao.setImagem(R.drawable.ic_credit_card);
        cartao.setStatus(false);

        FormadePagamento dinheiroCartao = new FormadePagamento();
        dinheiroCartao.setNome("DINHEIRO E CARTÃO");
        dinheiroCartao.setDescricao("");
        dinheiroCartao.setImagem(R.drawable.ic_credit_money);
        dinheiroCartao.setStatus(false);

        formas.add(dinheiro);
        formas.add(cartao);
        formas.add(dinheiroCartao);

        return formas;
    }
}
