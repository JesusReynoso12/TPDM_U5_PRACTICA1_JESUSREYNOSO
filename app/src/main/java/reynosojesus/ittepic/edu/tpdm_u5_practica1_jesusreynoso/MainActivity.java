package reynosojesus.ittepic.edu.tpdm_u5_practica1_jesusreynoso;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public BaseDatos bd;
    private EditText contenido,telefono;
    public TextView respuesta;
    private Receiver receiver;
    private IntentFilter filter;
    private static MainActivity main;

    public static MainActivity getInstance(){
        return main;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main=this;
        bd=new BaseDatos(this);
        setContentView(R.layout.activity_main);

        contenido=findViewById(R.id.contenido);
        telefono=findViewById(R.id.telefono);
        respuesta=findViewById(R.id.respuesta);

        filter=new IntentFilter();
        filter.addAction(Manifest.permission.RECEIVE_SMS);

        receiver=new Receiver();

        validar_permisos();
    }

    public void enviar_mensaje(View view) {
        if(!validar_permisos()){
            mensaje("Se necesitan tener todos los permisos");
            return;
        }
        if(telefono.getText().toString().isEmpty()||contenido.getText().toString().isEmpty()){
            mensaje("Llena todos los campos");
            return;
        }
        try{
            String t = telefono.getText().toString();
            String m=contenido.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(t,null,m,null,null);
            mensaje("Mensaje enviado");
            telefono.setText("");
            contenido.setText("");
            respuesta.setText("");
        }catch (Exception e){
            mensaje(e.getMessage());
        }
    }

    public void mensaje(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_SHORT).show();
    }

    private boolean validar_permisos(){
        boolean v=true;
        if(this.checkSelfPermission(Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
            mensaje("No hay permiso de enviar mensajes");
            v=false;
        }
        if(this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},2);
            mensaje("No hay permiso de leer el estado del telefono");
            v=false;
        }
        if(this.checkSelfPermission(Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.READ_SMS},3);
            mensaje("No hay permiso de leer los sms");
            v=false;
        }
        if(this.checkSelfPermission(Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},4);
            mensaje("No hay permiso de recibir mensajes");
            v=false;
        }
        return v;
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver,filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

}
