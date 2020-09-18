package com.rotimijohnson.notetakingapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.rotimijohnson.notetakingapp.model.User;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    TextView totalNumberOfNotes,fullname,username;
    Button updatebtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        totalNumberOfNotes = v.findViewById(R.id.booking_label2);
        fullname = v.findViewById(R.id.full_name);
        username = v.findViewById(R.id.short_name);
        updatebtn = v.findViewById(R.id.update);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                }catch (Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Error");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });

        DataBaseHandler db = new DataBaseHandler(getActivity());
        int notes = db.getNotesCount();
        totalNumberOfNotes.setText(String.valueOf(notes));
        return v;
    }
}