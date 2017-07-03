package br.com.belongapps.gustusdelivery.cardapioOnline.model;


public class ItemCardapio {

    private String nome;//
    private String descricao;//
    private String ref_img;//
    private double valor_unit;//
    private double preco_promocional;//
    private int qtd_recheios;//
    private String status_item;//
    private boolean status_promocao;//

    private String categoria_id;//

    public ItemCardapio(String nome, String descricao, String ref_img, double valor_unit, double preco_promocional, int qtd_recheios, String status_item, boolean status_promocao, String categoria_id) {

        this.nome = nome;
        this.descricao = descricao;
        this.ref_img = ref_img;
        this.valor_unit = valor_unit;
        this.preco_promocional = preco_promocional;
        this.qtd_recheios = qtd_recheios;
        this.status_item = status_item;
        this.status_promocao = status_promocao;
        this.categoria_id = categoria_id;

    }

    public ItemCardapio() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    public int getQtd_recheios() {
        return qtd_recheios;
    }

    public void setQtd_recheios(int qtd_recheios) {
        this.qtd_recheios = qtd_recheios;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public double getPreco_promocional() {
        return preco_promocional;
    }

    public void setPreco_promocional(double preco_promocional) {
        this.preco_promocional = preco_promocional;
    }

    public String getStatus_item() {
        return status_item;
    }

    public void setStatus_item(String status_item) {
        this.status_item = status_item;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public boolean isStatus_promocao() {
        return status_promocao;
    }

    public void setStatus_promocao(boolean status_promocao) {
        this.status_promocao = status_promocao;
    }

}
