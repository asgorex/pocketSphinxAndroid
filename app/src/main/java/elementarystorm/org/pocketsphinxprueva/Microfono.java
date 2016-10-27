package elementarystorm.org.pocketsphinxprueva;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by Vico on 29/08/2016.
 */
public class Microfono implements
        RecognitionListener {
     MainActivity mainActivity;
    boolean sw=false;

    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";

    /* Keyword we are looking for to activate menu */

    private static final String Llave = "empezar";
    private SpeechRecognizer recognizer;

    public Microfono(final MainActivity mainActivity){
        this.mainActivity=mainActivity;

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                Log.e("AsyncTask  ", "doInBackground");
                try {
                    Assets assets = new Assets(mainActivity);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                Log.e("AsyncTask  ","onPostExecute");
                if (result != null) {
                    //Microphone might be already in use.
                    //((TextView) findViewById(R.id.caption_text))
                            //.setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    /**
     * En consecuencia parcial obtenemos actualizaciones rápidas sobre hipótesis actual . En
     * Modo manchado palabra clave aquí podemos reaccionar , en otros modos tenemos que esperar
     * para el resultado final en onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {

        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        Log.e("onParialResult ", text);
        if (text.equals(Llave)||sw){
            Log.e("if ","si");
            sw=true;
            switchSearch(MENU_SEARCH);
        }
        else {
            if(sw)
                switchSearch(MENU_SEARCH);
            else{
                Log.e("onPartialResult ",text);
            }

        }
    }

    /**
     * Esta devolución de llamada cuando dejamos el reconocedor.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.e("oonResult ",text);
            if(text.compareTo(Llave)!=0){
                mainActivity.Mensage(text);
            }else
                mainActivity.Mensage("Escuchando.....");


        }else
            Log.e("oonResult ","NUll");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e("onBeginningOfSpeech ", "");
    }

    /**
     * Nos detenemos reconocedor aquí para obtener un resultado final
     */
    @Override
    public void onEndOfSpeech() {
        Log.e("onEndOfSpeech() ", "" + recognizer.getSearchName());
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
        else{
            if(sw)
                switchSearch(MENU_SEARCH);
            //mainActivity.Mensage("Para Inicar el Demo diga \n '"+Llave+"'");
        }
    }

    private void switchSearch(String searchName) {
        detener();
        Log.e("switchSearch(", searchName + ");");
        // Si no estamos en manchas, empezar a escuchar con el tiempo de espera ( 10000 ms o 10 segundos) .
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else if(searchName.equals(MENU_SEARCH))
            recognizer.startListening(searchName);
        //((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        Log.e("Funtion", "");
        // El sistema de reconocimiento puede ser configurado para llevar a cabo múltiples búsquedas
        // de diferentes tipos y cambiar entre ellas

        recognizer = defaultSetup()//llamamos a speeckecoizerSetup
                .setAcousticModel(new File(assetsDir, "es_es-ptm"))
                .setDictionary(new File(assetsDir, "es.dict"))
                // Para desactivar el registro de comentario de sonido en bruto a cabo esta llamada ( toma mucho espacio en el dispositivo )
                .setRawLogDir(assetsDir)
                // Umbral de ajustar para obtener frase clave de equilibrio entre falsas alarmas y errores
                .setKeywordThreshold(1e-45f)
                //  Uso independiente del contexto de búsqueda fonética , dependiente del contexto es demasiado lento para móviles
                //.setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);

        /** En su aplicación es posible que no tenga que añadir todas esas búsquedas .
         * Se añaden aquí para la demostración . Puede dejar sólo uno.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, Llave);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "calculadora.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
    }

    @Override
    public void onError(Exception error) {
        mainActivity.Error("El Microfono esta siendo usado por otra aplicacion");
    }

    @Override
    public void onTimeout()
    {
        Log.e("onTimeout ","");
        switchSearch(KWS_SEARCH);
    }
    public void iniciar(){
        switchSearch(KWS_SEARCH);
    }
    public void detener(){
        if(recognizer != null)
            recognizer.stop();
        else
            Log.e("Micronofo ","null");
    }

}
