package reynosojesus.ittepic.edu.tpdm_u5_practica1_jesusreynoso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

    public  Receiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("aqui");
        Bundle extras=intent.getExtras();
        Object[] pdus=(Object[]) extras.get("pdus");
        SmsMessage mensaje=SmsMessage.createFromPdu((byte[])pdus[0]);

        String contenido=mensaje.getMessageBody();
        if(contenido.startsWith("HOROSCOPO")){
            String[] _argumentos=contenido.split(" ");
            if(_argumentos.length==2){
                String argumentos=_argumentos[1];
                String[] __argumentos=argumentos.split(",");
                if(__argumentos.length==2){
                    String signo= __argumentos[0];
                    String lenguaje= __argumentos[1];

                    if(!(lenguaje.equals("es")||lenguaje.equals("en"))){
                        MainActivity.getInstance().mensaje("Lenguaje no soportado");
                        return;
                    }
                    SQLiteDatabase db=MainActivity.getInstance().bd.getReadableDatabase();
                    String query="select "+(lenguaje.equals("es")?"español":"ingles")+" from respuestas where signo='"+signo+"'";
                    Cursor c=db.rawQuery(query,null);
                    if(!c.moveToFirst()){
                        MainActivity.getInstance().mensaje("Municipio no encontrado");
                        return;
                    }
                    MainActivity.getInstance().respuesta.setText(c.getString(0));

                }else{
                    MainActivity.getInstance().mensaje("Revisa el formato del mensaje");
                }
            }else{
                MainActivity.getInstance().mensaje("Revisa el formato del mensaje");
            }
        }
    }
}
