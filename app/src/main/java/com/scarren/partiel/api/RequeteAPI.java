package com.scarren.partiel.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
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
    public static void effectuerRequete(final float longitude, final float latitude, final int rayon, final Context contexte, final ActionsResultatAPI actionsResultatDemande) {
        //Creation et demarrage de la tache asynchrone qui acceder et lire l'API
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    //Récupération des metadatas
                    Bundle bundle = contexte.getApplicationInfo().metaData;
                    String CLE_API = bundle.getString("com.google.android.geo.API_KEY");
                    //Creation de l'URL et de la connexion HTTP
                    URL urlRequete = new URL(
                            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                            + "&location=" + longitude + "," + latitude
                            + "&radius=" + rayon
                            + "&type=restaurant"
                            + "&key=" + CLE_API
                    );
                    HttpURLConnection connexion = (HttpURLConnection) urlRequete.openConnection();
                    //Definition des parametres de la connexion
                    connexion.setRequestMethod("GET");
                    connexion.setReadTimeout(10000);
                    connexion.setConnectTimeout(10000);
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
                    //On traduit le resultat en JSON
                    JSONObject resultat = new JSONObject(s);
                    //On regarde si la requête est valide
                    if(!resultat.getString("status").equals("OK")){
                        actionsResultatDemande.quandErreur();
                        return;
                    }
                    //On convertit le resultat en JSON
                    JSONArray restaurants = new JSONObject(s).getJSONArray("results");
                    Log.e("API", "Nombre = " + restaurants.length());
                    //Si il n'y a aucun restaurant, on ne fait rien
                    if(restaurants.length() == 0)
                        return;
                    //On récupère le restaurant le mieux noté
                    JSONObject restaurantLeMieuxNote = null;
                    //On vérifie tous les restaurants
                    for(int i = 0; i < restaurants.length(); i++){
                        JSONObject restaurantActuel = restaurants.getJSONObject(i);
                        //On verifie que le restaurant a bien un attribut rating
                        if(!restaurantActuel.has("rating"))
                            continue;
                        //On regarde si le restaurant testé est mieux noté que le mieux noté actuel
                        if(restaurantLeMieuxNote == null || restaurantActuel.getDouble("rating") > restaurantLeMieuxNote.getDouble("rating"))
                            restaurantLeMieuxNote = restaurantActuel;
                    }
                    //On regarde si le restaurant le mieux noté n'est pas null
                    if(restaurantLeMieuxNote == null)
                        return;
                    //On renvoie le restaurant le mieux noté trouvé
                    actionsResultatDemande.quandSucces(restaurantLeMieuxNote);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("API", "Impossible d'interpreter le JSON recu");
                    actionsResultatDemande.quandErreur();
                }
            }
        }.execute(); //On lance la tache asynchrone diretement
    }
}
