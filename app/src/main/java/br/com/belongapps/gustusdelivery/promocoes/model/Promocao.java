package br.com.belongapps.gustusdelivery.promocoes.model;


public class Promocao {

    private String ref_img;

    public Promocao( String ref_img) {
        this.ref_img = ref_img;
    }

    public Promocao(){

    }

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
