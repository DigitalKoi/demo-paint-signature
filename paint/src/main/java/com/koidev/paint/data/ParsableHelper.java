package com.koidev.paint.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class ParsableHelper implements Parcelable  {

    public ArrayList<MotionEvent> events;

    public Parcelable parcelable;

    public ParsableHelper(ArrayList<MotionEvent> events, Parcelable parcelable) {
        this.events = events;
        this.parcelable = parcelable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.events);
        dest.writeParcelable(parcelable, flags);
    }

    protected ParsableHelper(Parcel in) {
        this.events = in.createTypedArrayList(MotionEvent.CREATOR);
        this.parcelable = in.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public static final Parcelable.Creator<ParsableHelper> CREATOR = new Parcelable.Creator<ParsableHelper>() {
        @Override
        public ParsableHelper createFromParcel(Parcel source) {
            return new ParsableHelper(source);
        }

        @Override
        public ParsableHelper[] newArray(int size) {
            return new ParsableHelper[size];
        }
    };
}
