package elementarystorm.org.pocketsphinxprueva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    Microfono microfono;
    String cadena="";
    boolean activo=false;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        microfono=new Microfono(this);
    }
    public void Iniciar(View view){
        microfono.iniciar();
        Log.e("Operaciones ", "Iniciar");
    }
    public void Detener(View view){
        microfono.detener();
        Log.e("Operaciones ", "Detener");
    }
    public void Mensage(String string){
        //Log.e("Mensage  ", string);
        TextView temp=(TextView) findViewById(R.id.entrada);
        ((TextView) findViewById(R.id.caption_text))
            .setText(string);
        if (string.compareTo("Escuchando.....")!=0){
           /* if (string.compareTo("cerrar")==0){
                finish();
            }*/
            if ( string.compareTo("limpiar")==0){
                temp.setText("");
            }else {
                String temporal="";
                    switch (string){
                        case "mas":
                            temporal=" + ";
                            break;
                        case "menos":
                            temporal=" - ";
                            break;
                        case "por":
                            temporal=" * ";
                            break;
                        case "dividido":
                            temporal=" / ";
                            break;
                        case "uno":
                            temporal="1";
                            break;
                        case "dos":
                            temporal="2";
                            break;
                        case "tres":
                            temporal="3";
                            break;
                        case "cuatro":
                            temporal="4";
                            break;
                        case "cinco":
                            temporal="5";
                            break;
                        case "seis":
                            temporal="6";
                            break;
                        case "siete":
                            temporal="7";
                            break;
                        case "ocho":
                            temporal="8";
                            break;
                        case "nueve":
                            temporal="9";
                            break;
                        case "diez":
                            temporal="10";
                            break;
                    }
                if (string.compareTo("igual")==0){
                    if (string!=""){
                        temp.setText(temp.getText().toString()+" = "+Double.toString(resolver((String) temp.getText())));
                        activo=true;
                    }

                }else{
                    if (activo){
                        temp.setText(temporal);
                        activo=false;
                    }else{
                        temp.setText(temp.getText() + temporal);

                    }

                }

            }
        }



    }
    public void Error(String string){
        ((TextView) findViewById(R.id.caption_text))
                .setText(string);
    }

    @Override
    protected void onRestart() {
        Log.e("Override  ", "onRestart");
        microfono.iniciar();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        microfono.detener();
        Log.e("Override  ","onStop");
        super.onStop();
    }
    public void ActivityNew(View view){
        Intent intent= new Intent(MainActivity.this,AuxActivity.class);
        startActivity(intent);
    }
    public double resolver(String data){
        data=data.trim();
        String vec[]=data.split("\\ ");
        double resultado=0;
        if (vec.length>=3) {
            switch (vec[1].trim()){
                case "+":
                    resultado = Double.parseDouble(vec[0]) + Double.parseDouble(vec[2]);
                    break;
                case "-":
                    resultado = Double.parseDouble(vec[0]) - Double.parseDouble(vec[2]);
                    break;
                case "*":
                    resultado = Double.parseDouble(vec[0]) * Double.parseDouble(vec[2]);
                    break;
                case "/":
                    resultado = Double.parseDouble(vec[0]) / Double.parseDouble(vec[2]);
                    break;
            }
           /* if (vec[1].trim().compareTo("+") == 0) {
                resultado = Double.parseDouble(vec[0]) + Double.parseDouble(vec[2]);
            }*/
        }
        return resultado;
    }
}
