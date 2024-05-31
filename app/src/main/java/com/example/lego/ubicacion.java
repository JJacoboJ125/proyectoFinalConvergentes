
package com.example.lego;
public class ubicacion {
    private static ubicacion instanceI;
    private static ubicacion instanceF;
    private String inicio;
    private String finall;

    private ubicacion() {
    }

    public static synchronized ubicacion getInstanceInicio() {
        if (instanceI == null) {
            instanceI = new ubicacion();
        }
        return instanceI;
    }

    public static synchronized ubicacion getInstancefinal() {
        if (instanceF == null) {
            instanceF = new ubicacion();
        }
        return instanceF;
    }

    public void setInicio(String coordenada) {
        this.inicio = coordenada;
    }

    public String getInicio() {
        return inicio;
    }


    public void setFinall(String coordenada) {
        this.finall = coordenada;
    }

    public String getFinall() {

        return finall;
    }
}
