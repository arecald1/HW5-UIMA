package com.cs250.joanne.myfragments;

import java.lang.String;

/**
 * Holds data for one item - straight foraward simple little item class
 */
public class Item {
    private String what;


    Item(String what) {
        this.what = what;
    }

    public String getWhat() { return what; }

}
