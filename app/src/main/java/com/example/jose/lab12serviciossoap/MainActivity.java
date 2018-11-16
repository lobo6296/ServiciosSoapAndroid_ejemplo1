package com.example.jose.lab12serviciossoap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText et1,et2;
    private TextView tv1;
    private Button btn1;
    String param1,param2,mensaje;
    SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText) findViewById(R.id.paramTxt);
        et2 = (EditText) findViewById(R.id.param2Txt);
        tv1 = (TextView) findViewById(R.id.resultaView);
        btn1 = (Button) findViewById(R.id.convertirBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param1 = et1.getText().toString();//usd
                param2 = et2.getText().toString();//bob

                SegundoPlano tareaAsync = new SegundoPlano();
                tareaAsync.execute();
            }
        });
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected Void doInBackground(Void... params){
            convertir();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            tv1.setText("Response: " + resultString.toString() + "," + mensaje);
        }
    }

    private void convertir(){
        String SOAP_ACTION = "http://www.webserviceX.NET/ConversionRate";
        String METHOD_NAME = "ConversionRate";
        String NAMESPACE = "http://www.webserviceX.NET/";
        String URL = "http://www.webservicex.net/CurrencyConvertor.asmx";

        try{
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("FromCurrency",param1);
            Request.addProperty("ToCurrency",param2);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            mensaje = "OK";
        } catch (Exception ex){
            mensaje = "ERROR: " + ex.getMessage();
        }
    }

}
