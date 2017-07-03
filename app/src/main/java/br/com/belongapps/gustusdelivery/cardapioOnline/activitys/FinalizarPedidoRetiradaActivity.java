package br.com.belongapps.gustusdelivery.cardapioOnline.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.belongapps.gustusdelivery.R;
import br.com.belongapps.gustusdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.Cliente;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.KeyPedido;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.Pagamento;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.gustusdelivery.posPedido.activities.AcompanharPedidoActivity;
import br.com.belongapps.gustusdelivery.util.DataUtil;

import static android.content.ContentValues.TAG;

public class FinalizarPedidoRetiradaActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Views
    private TextView txtTotalPedido;
    private TextView txtTotalDosItens;
    private Button finalizarPedido;

    //Dialogs
    AlertDialog dialogPedidoEnviado;
    AlertDialog.Builder mBilder;

    private double totaldoPedido; //parâmetro recebido

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private String numerodopedido = "";

    private Pedido pedido = new Pedido();

    private ProgressDialog dialog;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido_retirada);

        mBilder = new AlertDialog.Builder(FinalizarPedidoRetiradaActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enviar_pedido_retirada);
        mToolbar.setTitle("Finalizar Pedido");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recebe Valor total do pedido
        Intent i = getIntent();
        totaldoPedido = i.getDoubleExtra("totalPedido", 0);

        populateView();

        finalizarPedido = (Button) findViewById(R.id.bt_finalizar_pedido_retirada);

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean podeEnviar = true;

                if (podeEnviar) {

                    beforeEnviarPedido();

                    new exibirDialogdeEnvio().execute((Void[]) null);

                } else {

                    AlertDialog.Builder mBilder = new AlertDialog.Builder(FinalizarPedidoRetiradaActivity.this, R.style.MyDialogTheme);
                    View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_selecionar_fpgm, null);

                    mBilder.setView(layoutDialog);
                    final AlertDialog dialogEscolhaFormPag = mBilder.create();
                    dialogEscolhaFormPag.show();

                    Button btCancel = (Button) layoutDialog.findViewById(R.id.bt_entendi_finalizar);

                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogEscolhaFormPag.dismiss();
                        }
                    });
                }

            }
        });

    }

    private class exibirDialogdeEnvio extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(FinalizarPedidoRetiradaActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Estamos enviando seu pedido!!");
            dialog.show();
        }

        protected Void doInBackground(Void... param) {
            try {
                Thread.currentThread();
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            dialog.dismiss();

            View layoutDialog = createDialog();
            mBilder.setView(layoutDialog);
            dialogPedidoEnviado = mBilder.create();
            dialogPedidoEnviado.show();
        }
    }

    private View createDialog() {
        mBilder = new AlertDialog.Builder(FinalizarPedidoRetiradaActivity.this);
        View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_pedido_finalizado, null);

        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_ok_pedido_enviado);
        Button acompanhar = (Button) layoutDialog.findViewById(R.id.bt_acompanhar_pedido_enviado);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoRetiradaActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        acompanhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoRetiradaActivity.this, AcompanharPedidoActivity.class);
                i.putExtra("NumeroPedido", pedido.getNumero_pedido());
                i.putExtra("DataPedido", DataUtil.getDataPedido(pedido.getData()));
                i.putExtra("HoraPedido", DataUtil.getHoraPedido(pedido.getData()));
                i.putExtra("ValorPedido", totaldoPedido);
                i.putExtra("StatusPedido", pedido.getStatus());
                i.putExtra("TipoEntrega", pedido.getEntrega_retirada());
                i.putExtra("StatusTempo", pedido.getStatus_tempo());

                ArrayList<ItemPedido> itens = new ArrayList<>();
                for (ItemPedido item: pedido.getItens_pedido()) {
                    itens.add(item);
                }
                i.putParcelableArrayListExtra("ItensPedido" , itens);

                startActivity(i);
                finish();

            }
        });

        return layoutDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(FinalizarPedidoRetiradaActivity.this, CarrinhoActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FinalizarPedidoRetiradaActivity.this, CarrinhoActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateView() {

        txtTotalPedido = (TextView) findViewById(R.id.valor_total_pedido_retirada);
        txtTotalPedido.setText("Total: R$ " + String.format(Locale.US, "%.2f", (totaldoPedido)).replace(".", ","));

    }

    private void beforeEnviarPedido() {
        pedido = new Pedido();

        Date data = DataUtil.getCurrenteDate();
        String dataPedido = DataUtil.formatar(data, "dd/MM/yyyy HH:mm");
        String diaPedido = DataUtil.formatar(data, "ddMMyyyy");

        pedido.setData(dataPedido);
        pedido.setStatus_tempo("n" + DataUtil.formatar(data, "HH:mm"));
        pedido.setNumero_pedido(gerarNumeroPedido(numerodopedido));
        pedido.setStatus(0);
        pedido.setEntrega_retirada(getIntent().getIntExtra("tipoEntrega", 0));
        pedido.setItens_pedido(getItensdoPedido());

        //Pegar usuário logado
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Thiago Oliveira");

        //setar apenas se a entrega for delivery
        if (pedido.getEntrega_retirada() == 0) {
            cliente.setRuaEndCliente("Tv. Aristides Gonçalves");
            cliente.setNumeroEndCliente("179");
            cliente.setBairroEndCliente("Rodoviária");
            cliente.setComplementoEndCliente("Ap 202");
        }

        pedido.setCliente(cliente); //Adicionar Cliente ao Pedido

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(totaldoPedido);

        pedido.setPagamento(pagamento);

        salvarPedido(pedido, diaPedido);

    }

    private void salvarPedido(Pedido pedido, String hj) {
        String key = database.child("pedidos").push().getKey();
        Pedido pedidoAux = pedido;
        Map<String, Object> pedidoValues = pedidoAux.toMap();

        Log.println(Log.ERROR, "NODO:", hj);

        Map<String, Object> childUpdatesPedido = new HashMap<>();
        childUpdatesPedido.put("/pedidos/" + hj + "/" + key, pedidoValues);

        database.updateChildren(childUpdatesPedido);

        //ATUALIZA PEDIDOS DO USUÁRIO LOGADO
        atualizarPedidosdoCliente(pedido.getCliente(), key);

        //LIMPAR CARRINHO
        CarrinhoDAO dao = new CarrinhoDAO(this);
        dao.deleteAll();
    }

    public void atualizarPedidosdoCliente(Cliente cliente, String keyPedido) {
        KeyPedido keyp = new KeyPedido(keyPedido);
        keyp.setId(keyPedido);

        String key = database.child("clientes").child("1").push().getKey();//pegar id do usuário logado

        database.child("clientes").child("1").child("pedidos").child(key).setValue(keyp);
    }

    public void updateNumero(List<Integer> list) {

        if (list.isEmpty()) {
            numerodopedido = "0";

        } else {

            Collections.sort(list);

            numerodopedido = list.get(list.size() - 1).toString();
            Log.println(Log.ERROR, "Ultimo Pedido: ", list.get(list.size() - 1).toString());
        }

    }

    public List<ItemPedido> getItensdoPedido() {
        List<ItemPedido> itensAux;
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itensAux = crud.getAllItens();
        return itensAux;
    }

    public String gerarNumeroPedido(String numero) {

        Log.println(Log.ERROR, "PEDIDO ANTERIOR:", numero);

        int intnum = Integer.parseInt(numero);
        intnum++;

        if (intnum < 10) {
            NumberFormat formatter = new DecimalFormat("00");

            numero = formatter.format(intnum);
        } else {
            numero = String.valueOf(intnum);
        }

        Log.println(Log.ERROR, "NUMERO DO PEDIDO:", numero);

        return numero;
    }

    @Override
    protected void onStart() {
        super.onStart();

        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Integer> list = new ArrayList<>();

                Log.println(Log.ERROR, "NUMBERMETODY", "Entrou no metodo!");

                try {
                    for (DataSnapshot pedido : dataSnapshot.getChildren()) {
                        for (DataSnapshot dia : pedido.getChildren()) {
                            String numpedido = dia.child("numero_pedido").getValue().toString();
                            list.add(Integer.parseInt(numpedido));
                        }
                    }

                    updateNumero(list);
                } catch (Exception n) {
                    updateNumero(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.child("pedidos").addValueEventListener(postListener);

    }
}
