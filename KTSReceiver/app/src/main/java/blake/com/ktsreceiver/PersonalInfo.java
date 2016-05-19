package blake.com.ktsreceiver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raiders on 5/19/16.
 */
public class PersonalInfo implements Parcelable {

    public String name;
    public String month;
    public String day;
    public String year;

    public PersonalInfo(String name, String month, String day, String year) {
        this.name = name;
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public PersonalInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.month = parcel.readString();
        this.day = parcel.readString();
        this.year = parcel.readString();
    }

    public static Creator<PersonalInfo> CREATOR = new Creator<PersonalInfo>() {
        @Override
        public PersonalInfo createFromParcel(Parcel source) {
            return new PersonalInfo(source);
        }

        @Override
        public PersonalInfo[] newArray(int size) {
            return new PersonalInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeString(year);
    }
}
