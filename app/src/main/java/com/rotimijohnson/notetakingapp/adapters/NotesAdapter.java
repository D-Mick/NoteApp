package com.rotimijohnson.notetakingapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.rotimijohnson.notetakingapp.model.Note;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.rotimijohnson.notetakingapp.model.Note;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    Context context;
    List<Note> noteList;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;


    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notes_items,parent,false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotesViewHolder holder, final int position) {
        holder.headText.setText(noteList.get(position).getTitle());
        holder.descText.setText(noteList.get(position).getNote());
        int [] color;


        int color1 = context.getResources().getColor(R.color.color1);
        int color2 = context.getResources().getColor(R.color.color2);
        int color3 = context.getResources().getColor(R.color.color3);
        int color4 = context.getResources().getColor(R.color.color4);
        int color5 = context.getResources().getColor(R.color.color5);
        int color6 = context.getResources().getColor(R.color.color6);
        int color8 = context.getResources().getColor(R.color.color8);
        int color9 = context.getResources().getColor(R.color.color9);
        int color10 = context.getResources().getColor(R.color.color10);
        int color11 = context.getResources().getColor(R.color.color11);
        int color12 = context.getResources().getColor(R.color.color12);
        int color13 = context.getResources().getColor(R.color.color13);
        int color14 = context.getResources().getColor(R.color.color14);

        color = new int[]{color1,color2,color3,color4,color5,color6,color8,color9,color10,color11,color12,color13,color14};
        int selectedColors = color.length;
        Random random = new Random();
        int randomNum = random.nextInt(selectedColors);
        holder.view.setBackgroundColor(color[randomNum]);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView headText, descText;
        private View view;
        ImageView deleteImg;
        public int id;
//         public Boolean bookmark_icon = true;

        public NotesViewHolder(View itemView) {
            super(itemView);
            headText = itemView.findViewById(R.id.head1);
            descText = itemView.findViewById(R.id.desc1);
            view = itemView.findViewById(R.id.view1);
//            bookmarkImg = itemView.findViewById(R.id.bookmark);
            deleteImg = itemView.findViewById(R.id.delete);
//
//            bookmarkImg.setOnClickListener(this);
            deleteImg.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Note note = noteList.get(position);
                    editText(note);
                }
            });
        }

        public void editText(final Note note){
            builder = new AlertDialog.Builder(context);
            View v = LayoutInflater.from(context).inflate(R.layout.add_dialog, null);

            final TextInputLayout title = v.findViewById(R.id.title_note);
            final TextInputLayout description = v.findViewById(R.id.text_note);
            final TextView textView = v.findViewById(R.id.add_note);
            textView.setText("Edit Note");
            Button saveBtn = v.findViewById(R.id.save);
            title.getEditText().setText(note.getTitle());
            Objects.requireNonNull(description.getEditText()).setText(note.getNote());

            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.show();

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler db = new DataBaseHandler(context);
                    note.setTitle(title.getEditText().getText().toString());
                    note.setNote(description.getEditText().getText().toString());

                    if (!title.getEditText().getText().toString().isEmpty() && !description.getEditText().getText().toString().isEmpty()){
                        db.updateNote(note);
                        notifyItemChanged(getAdapterPosition(), note);
                    }else {
                        Snackbar.make(v, "Add title and note", Snackbar.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.delete:
                    int position = getAdapterPosition();
                    Note note = noteList.get(position);
                    deleteItem(note.getId());
                    break;
                default:
                    return;
            }
        }

        public void deleteItem(final int id){

            builder = new AlertDialog.Builder(context);
            View v = LayoutInflater.from(context).inflate(R.layout.delete_layout,null,false);
            Button yes = v.findViewById(R.id.dialog_yes);
            Button no = v.findViewById(R.id.dialog_no);

            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHandler db = new DataBaseHandler(context);
                    db.deleteGrocery(id);
                    noteList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Okay", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
    }
}
