package com.example.lego;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class OdometerService extends Service {
    private final IBinder binder = new OdometerBinder();
    private final Random random=new Random();

    public class OdometerBinder extends Binder {
        OdometerService getOdometer(){
            return OdometerService.this;
        }
    }

    public OdometerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public double getDistance(){
        return random.nextDouble();
    }
}
