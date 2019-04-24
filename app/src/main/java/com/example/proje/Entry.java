package com.example.proje;

import java.io.Serializable;

public class Entry{
    public Integer _class;
    public Integer x_axis;
    public Integer y_axis;
    public Integer z_axis;

    public Entry() {
        this.x_axis = 0;
        this.y_axis = 0;
        this.z_axis = 0;
        this._class = 1;
    }

    public Entry(Integer _class, Integer x_axis, Integer y_axis, Integer z_axis) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.z_axis = z_axis;
        this._class = _class;
    }
/*
    public Integer getX_axis() {
        return x_axis;
    }

    public void setX_axis(Integer x_axis) {
        this.x_axis = x_axis;
    }

    public Integer getY_axis() {
        return y_axis;
    }

    public void setY_axis(Integer y_axis) {
        this.y_axis = y_axis;
    }

    public Integer getZ_axis() {
        return z_axis;
    }

    public void setZ_axis(Integer z_axis) {
        this.z_axis = z_axis;
    }

    public Integer getA_class() {
        return a_class;
    }

    public void setA_class(Integer a_class) {
        this.a_class = a_class;
    }*/
}
