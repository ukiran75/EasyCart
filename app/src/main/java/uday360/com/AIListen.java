package uday360.com.mycart;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by uday on 4/19/2016.
 */
public class AIListen extends AppCompatActivity {

    TextView header,responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_listen);
        header=(TextView)findViewById(R.id.header);
        responseText=(TextView)findViewById(R.id.responseText);
        new AITask().execute();
    }

    public class AITask extends AsyncTask<Void,Void,Void> implements AIListener
    {
        ProgressDialog progressDialog=new ProgressDialog(AIListen.this);
        private AIService aiService;
        final AIConfiguration config = new AIConfiguration("f0e4e096f0da4343a441f39b6e642df5",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        @Override
        protected Void doInBackground(Void... params) {
            aiService = AIService.getService(AIListen.this, config);
            aiService.setListener(this);
            aiService.startListening();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Listening");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        public void onResult(AIResponse result) {
            Result response=result.getResult();
            String parameterString = "";
            if (response.getParameters() != null && !response.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : response.getParameters().entrySet()) {
                    parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                }
            }
            responseText.setText(("Query:" + response.getResolvedQuery() +
                    "\nAction: " + response.getAction() +
                    "\nParameters: " + parameterString));

        }

        @Override
        public void onError(AIError error) {

            responseText.setText(error.toString());
        }

        @Override
        public void onAudioLevel(float level) {

        }

        @Override
        public void onListeningStarted() {

        }

        @Override
        public void onListeningCanceled() {

        }

        @Override
        public void onListeningFinished() {

        }
    }

}
