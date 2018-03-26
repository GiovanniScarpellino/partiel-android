package com.scarren.partiel.vues;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.scarren.partiel.R;

import org.w3c.dom.Text;

public class FragmentHome extends Fragment {

    private TextView textDummyLocationSwitch;
    private TextView textDummyLocation;
    private SharedPreferences preferences;

    private OnFragmentInteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        textDummyLocationSwitch = vue.findViewById(R.id.texte_value_prefer_dummy_location);
        textDummyLocation = vue.findViewById(R.id.texte_dummy_location);
        return vue;
    }

    @Override
    public void onResume() {
        boolean valueSwitch = preferences.getBoolean(getResources().getString(R.string.key_switch_value), true);
        textDummyLocationSwitch.setText("Prefer Dummy Location : " + valueSwitch);
        if(valueSwitch){
            String latitude = preferences.getString(getResources().getString(R.string.key_latitude_value), "0");
            String longitude = preferences.getString(getResources().getString(R.string.key_longitude_value), "0");
            textDummyLocation.setText("Dummy Location : (" + latitude + "," + longitude + ")");
        }else{
            textDummyLocation.setText("");
        }
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
