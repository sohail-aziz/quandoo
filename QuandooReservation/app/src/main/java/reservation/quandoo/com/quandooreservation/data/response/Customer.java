package reservation.quandoo.com.quandooreservation.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

/**
 * Created by sohailaziz on 15/12/17.
 */

@AutoParcelGson
public abstract class Customer implements Parcelable {

    @SerializedName("customerFirstName")
    public abstract String firstName();

    @SerializedName("customerLastName")
    public abstract String lastName();

    @SerializedName("id")
    public abstract long id();

    public static Customer create(String firstname, String lastName, long id) {
        return new AutoParcelGson_Customer(firstname, lastName, id);


    }

}
