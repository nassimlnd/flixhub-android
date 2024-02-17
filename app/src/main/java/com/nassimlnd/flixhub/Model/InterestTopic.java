package com.nassimlnd.flixhub.Model;

import java.util.ArrayList;

public class InterestTopic {

    private ArrayList<Data> group_title;

    public InterestTopic(ArrayList<Data> name) {
        this.group_title = name;
    }

    public class Data {
        public String group_title;

        public Data(String name) {
            this.group_title = name;
        }

        public String getGroupTitle() {
            return group_title;
        }
    }

    public ArrayList<Data> getData() {
        return group_title;
    }


}
