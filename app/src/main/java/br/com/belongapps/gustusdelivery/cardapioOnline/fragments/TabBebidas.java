package br.com.belongapps.gustusdelivery.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.gustusdelivery.R;
import br.com.belongapps.gustusdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.gustusdelivery.cardapioOnline.model.ItemPedido;


public class TabBebidas extends Fragment {

    private RecyclerView mBebidaList;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;
    private boolean statusDelivery = true;
    private boolean statusEstabelecimento = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bebidas, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child("configuracoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean status = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = status;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        itemPedido = new ItemPedido();

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_escolher_bebida);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("3");
        mDatabaseReference.keepSynced(true);

        mBebidaList = (RecyclerView) getView().findViewById(R.id.list_bebidas);
        mBebidaList.setHasFixedSize(true);
        mBebidaList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();

        final FirebaseRecyclerAdapter<ItemCardapio, SanduichesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, SanduichesViewHolder>(
                ItemCardapio.class, R.layout.card_bebida, TabBebidas.SanduichesViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final TabBebidas.SanduichesViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_bebida);
            }

            @Override
            protected void populateViewHolder(TabBebidas.SanduichesViewHolder viewHolder, final ItemCardapio model, int position) {
                closeProgressBar();

                viewHolder.setNome(model.getNome());
                viewHolder.setValorUnitarioEPromocao(model.getValor_unit(), model.isStatus_promocao(), model.getPreco_promocional());
                viewHolder.setImagem(getContext(), model.getRef_img());
                viewHolder.setStatus(model.getStatus_item());

                if (model.getStatus_item().equals("Ativado")) {

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (statusEstabelecimento == false) {
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                View layoutDialog = inflater.inflate(R.layout.dialog_estabelecimento_fechado, null);

                                Button btEntendi = (Button) layoutDialog.findViewById(R.id.bt_entendi_estabeleciemento_fechado);

                                mBilder.setView(layoutDialog);
                                final AlertDialog dialogEstabelecimentoFechado = mBilder.create();
                                dialogEstabelecimentoFechado.show();

                                btEntendi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogEstabelecimentoFechado.dismiss();
                                    }
                                });
                            } else if (statusDelivery == false) {
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                View layoutDialog = inflater.inflate(R.layout.dialog_delivery_fechado, null);

                                Button btVoltar = (Button) layoutDialog.findViewById(R.id.bt_voltar_delivery_fechado);
                                Button btContinuar = (Button) layoutDialog.findViewById(R.id.bt_continuar_delivery_fechado);

                                mBilder.setView(layoutDialog);
                                final AlertDialog dialogDeliveryFechado = mBilder.create();
                                dialogDeliveryFechado.show();

                                btVoltar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogDeliveryFechado.dismiss();
                                    }
                                });

                                btContinuar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogDeliveryFechado.dismiss();
                                        selecionarItem(model);
                                    }
                                });

                            } else {
                                selecionarItem(model);
                            }

                        }
                    });
                }
            }


        };

        mBebidaList.setAdapter(firebaseRecyclerAdapter);

    }

    public void selecionarItem(ItemCardapio model) {
        itemPedido.setNome(model.getNome());
        itemPedido.setDescricao(model.getDescricao());
        itemPedido.setRef_img(model.getRef_img());

        if (model.isStatus_promocao() == true) {
            itemPedido.setValor_unit(model.getPreco_promocional());
        } else {
            itemPedido.setValor_unit(model.getValor_unit());
        }

        Log.println(Log.ERROR, "VALOR: ", String.valueOf(model.getValor_unit()));

        Intent intent = new Intent(getActivity(), DetalhesdoItemActivity.class);

        intent.putExtra("ItemPedido", itemPedido);
        intent.putExtra("TelaAnterior", "TabBebidas");
        startActivity(intent);

        getActivity().finish();
    }

    public static class SanduichesViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_bebida;

        public SanduichesViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_bebida = (CardView) mView.findViewById(R.id.card_bebida);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_bebida);
            item_nome.setText(nome);

        }

        public void setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = (TextView) mView.findViewById(R.id.item_valor_promo_bebida);
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor_unit_bebida);

            if (status_promocao == true) {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_promocional).replace(".", ","));
                item_valor_unit.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
            } else {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
            }

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_bebida);

            Picasso.with(context).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(item_ref_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(url).into(item_ref_image);
                }
            });
        }

        public void setStatus(String status) {
            TextView item_status = (TextView) mView.findViewById(R.id.status_bebida);

            if (!status.equals("Ativado")) {
                item_status.setVisibility(View.VISIBLE);
            }

        }
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
