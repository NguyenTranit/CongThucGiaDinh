package com.nhatnguyen.congthucgiadinh.model;

import java.io.Serializable;
import java.util.Comparator;

public class NoteModel implements Serializable {
    private String id,note,timer,title;

    public NoteModel() {
    }


    public NoteModel(String id, String note, String timer, String title) {
        this.id = id;
        this.note = note;
        this.timer = timer;
        this.title = title;
    }
    public static final Comparator<NoteModel> BY_NAME_ALPHABETICAL=new Comparator<NoteModel>() {
        @Override
        public int compare(NoteModel o1, NoteModel o2) {
            return o1.timer.compareTo(o2.timer);
        }
    };
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }



    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
