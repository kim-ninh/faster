package com.ninhhk.galleryexample;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

class MediaStoreData implements Parcelable {

    final long rowId;
    final Uri uri;
    final String mimeType;
    final long dateModified;
    final int orientation;
    private final Type type;
    final long dateTaken;

    MediaStoreData(
            long rowId,
            Uri uri,
            String mimeType,
            long dateTaken,
            long dateModified,
            int orientation,
            Type type) {
        this.rowId = rowId;
        this.uri = uri;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.type = type;
        this.dateTaken = dateTaken;
    }

    protected MediaStoreData(Parcel in) {
        rowId = in.readLong();
        uri = Uri.parse(in.readString());
        mimeType = in.readString();
        dateTaken = in.readLong();
        dateModified = in.readLong();
        orientation = in.readInt();
        type = Type.valueOf(in.readString());
    }

    public static final Creator<MediaStoreData> CREATOR = new Creator<MediaStoreData>() {
        @Override
        public MediaStoreData createFromParcel(Parcel in) {
            return new MediaStoreData(in);
        }

        @Override
        public MediaStoreData[] newArray(int size) {
            return new MediaStoreData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(rowId);
        parcel.writeString(uri.toString());
        parcel.writeString(mimeType);
        parcel.writeLong(dateTaken);
        parcel.writeLong(dateModified);
        parcel.writeInt(orientation);
        parcel.writeString(type.name());
    }

    @Override
    public String toString() {
        return "MediaStoreData{"
                + "rowId="
                + rowId
                + ", uri="
                + uri
                + ", mimeType='"
                + mimeType
                + '\''
                + ", dateModified="
                + dateModified
                + ", orientation="
                + orientation
                + ", type="
                + type
                + ", dateTaken="
                + dateTaken
                + '}';
    }

    public enum Type{
        VIDEO,
        IMAGE
    }
}
