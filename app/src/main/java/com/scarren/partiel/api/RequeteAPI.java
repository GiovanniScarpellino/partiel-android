package com.scarren.partiel.api;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequeteAPI {

    /**
     * Methode qui effectue une requete vers l'API
     *
     * @param actionsResultatDemande Methodes a executer en cas de reussite ou d'echec de la demande vers l'API
     */
    @SuppressLint("StaticFieldLeak")
    public static void effectuerRequete(final ActionsResultatAPI actionsResultatDemande) {
        //Creation et demarrage de la tache asynchrone qui acceder et lire l'API
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    //Creation de l'URL et de la connexion HTTP
                    URL urlRequete = new URL("zqdqzdzqd");
                    HttpURLConnection connexion = (HttpURLConnection) urlRequete.openConnection();
                    //Definition des parametres de la connexion
                    connexion.setRequestMethod("GET");
                    connexion.setReadTimeout(12000);
                    connexion.setConnectTimeout(12000);
                    //Connexion a l'URL
                    connexion.connect();

                    //Creation des objets necessaire a la lecture du resultat
                    InputStreamReader streamReader = new InputStreamReader(connexion.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    //Lecture du resultat
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }

                    //Fermeture des flux
                    reader.close();
                    streamReader.close();

                    //Envoi du resultat a la fin de la tache asynchrone
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("API", "Erreur lors de la connexion et de la lecture du service web");
                }
                //Si il y a eu une erreur, on envoie null
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                //On regarde si il y a eu une erreur et si c'est le cas on execute quandErreur()
                if (s == null) {
                    actionsResultatDemande.quandErreur();
                    return;
                }

                try {
                    //On convertit le resultat en JSON
                    JSONObject resultat = new JSONObject(s);
                    actionsResultatDemande.quandSucces(resultat);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("API", "Impossible d'interpreter le JSON recu");
                    actionsResultatDemande.quandErreur();
                }
            }
        }.execute(); //On lance la tache asynchrone diretement
    }
}
