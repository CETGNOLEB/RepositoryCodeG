package br.com.belongapps.gustusdelivery.cardapioOnline.BO;

import android.util.Log;

import java.util.Date;

import br.com.belongapps.gustusdelivery.util.DataUtil;

public class PedidoBO {

    public static String gerarNumerodoPedido(String data) {
        String numero = "";

        Date datadeHoje = new Date();
        String dia = DataUtil.formatar(datadeHoje, "dd/mm/yyyy");


        Log.println(Log.ERROR, "RETORNO: ", numero);

        return numero;
    }
}