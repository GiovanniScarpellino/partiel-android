package com.scarren.partiel.api;

import org.json.JSONException;
import org.json.JSONObject;

public interface ActionsResultatAPI {
    void quandErreur();
    void quandSucces(JSONObject donnees) throws JSONException;
}
