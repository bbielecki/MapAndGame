package com.example.bartomiej.mapandgame;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.Map;

/**
 * Created by Bartłomiej on 03.05.2017.
 */

public class MarkerInfoDialog {
    TextView infoTextView;
    Spinner actionsSpinner;
    String selectedAction;
    RadioButton yesRadioButton;
    RadioButton noRadioButton;
    Button exitButton;
    MarkerDescription markerDescription;
//    Activity mapAndGame;

//    MarkerInfoDialog(Activity mapAndGame){
//
//    }

    public MarkerDescription showMarkerInfoDialog(Activity activity, String msg, Map<Marker, MarkerDescription> markerDescriptionMap, Marker marker){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.marker_info);
        initilizeVariables(dialog);

        infoTextView.setText(msg);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.actions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionsSpinner.setAdapter(adapter);

        buttonsHandler(dialog, markerDescriptionMap, marker);


        dialog.show();

        return markerDescription;
    }

    private void initilizeVariables(Dialog dialog){
        actionsSpinner = (Spinner) dialog.findViewById(R.id.actionsSpinner);
        infoTextView = (TextView) dialog.findViewById(R.id.infoTextView);
        yesRadioButton = (RadioButton) dialog.findViewById(R.id.deleteYesRadioButton);
        noRadioButton = (RadioButton) dialog.findViewById(R.id.deleteNoRadioButton);
        exitButton = (Button) dialog.findViewById(R.id.exitButton);
        noRadioButton.setChecked(true);
        markerDescription = new MarkerDescription();
    }

    private void setDefoultOptions(MarkerDescription description){
        if(description.selectedActivity != null){

        }
    }

    private void buttonsHandler(final Dialog dialog, final Map<Marker, MarkerDescription> markerDescriptionMap, final Marker marker){
        noRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noRadioButton.setChecked(true);
                yesRadioButton.setChecked(false);
            }
        });

        yesRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noRadioButton.setChecked(false);
                yesRadioButton.setChecked(true);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerDescription.selectedActivity = selectedAction;
                if(noRadioButton.isChecked()) {
                    markerDescription.shouldBeDeleted = false;
                }
                else {
                    markerDescription.shouldBeDeleted = true;
                    marker.remove();
                    //TODO: ogarnij markerHandler -> routeMarkers
                }


                if(markerDescriptionMap.containsKey(marker))
                {
                    markerDescriptionMap.remove(marker);
                    markerDescriptionMap.put(marker, markerDescription);
                }else {
                    markerDescriptionMap.put(marker, markerDescription);
                }
                dialog.dismiss();
            }
        });

        actionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAction = actionsSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAction = null;
            }
        });
    }
}
