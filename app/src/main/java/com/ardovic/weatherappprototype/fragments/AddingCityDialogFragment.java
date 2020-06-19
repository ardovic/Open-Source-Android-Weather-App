package com.ardovic.weatherappprototype.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ardovic.weatherappprototype.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddingCityDialogFragment extends DialogFragment {

    @BindView(R.id.et_city_name)
    EditText city_name;

    Unbinder unbinder;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_city_adding, null);


        builder.setView(rootView)
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), city_name.getText().toString(), Toast.LENGTH_SHORT).show();
                        AddingCityDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddingCityDialogFragment.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
