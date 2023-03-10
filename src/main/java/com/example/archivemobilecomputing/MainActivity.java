package com.example.archivemobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ListView noteList;
    ArrayList<Note> searchList;
    Note adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setNoteAdapter();
        setOnClickListener();
    }



    private void initWidgets()
    {
        noteList = findViewById(R.id.noteListView);

        SearchView searchView = findViewById(R.id.noteListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                s=s.toLowerCase() ;
                searchList=new ArrayList<>();
                for(Note note: adapter.nonDeletedNotes())
                {
                    String name= note.getDescription().toLowerCase();
                    if (name.contains(s))
                    {
                        searchList.add(note);
                    }
                }

                NoteAdapter adapter = new NoteAdapter((getApplicationContext()),0,searchList);
                noteList.setAdapter(adapter);
                return false;
            }
        });


    }




    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateNoteListArray();
    }

    private void setNoteAdapter()
    {
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), 0, Note.nonDeletedNotes());
        noteList.setAdapter(noteAdapter);
    }


    private void setOnClickListener()
    {
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Note selectedNote = (Note) noteList.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), NoteDetailActivity.class);
                editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }


    public void newNote(View view)
    {
        Intent newNoteIntent = new Intent(this, NoteDetailActivity.class);
        startActivity(newNoteIntent);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        setNoteAdapter();
    }



}