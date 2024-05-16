
package com.example.lego;
public class uid {
    private static uid instance;
    private String uid;

    private uid() {
        // Constructor privado para evitar la creación de instancias externas
    }

    public static synchronized uid getInstance() {
        if (instance == null) {
            instance = new uid();
        }
        return instance;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
