package com.rotimijohnson.notetakingapp.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.adapters.NotesAdapter;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.rotimijohnson.notetakingapp.model.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> modelList;
    private TextView delete_all;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        delete_all = view.findViewById(R.id.delete_all);
        final DataBaseHandler db = new DataBaseHandler(getActivity());
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllItems();
            }
        });
        modelList = db.getAllNotes();
        if (modelList.size() != 0 ){
            notesAdapter = new NotesAdapter(getActivity(), modelList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(notesAdapter);
        }
        return view;
    }
}
