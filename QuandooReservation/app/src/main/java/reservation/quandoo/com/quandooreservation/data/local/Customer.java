package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sohailaziz on 19/12/17.
 */

@Entity (tableName = "customer")
public class Customer implements Parcelable {

    private String customerFirstName;
    private String customerLastName;
    @PrimaryKey
    private int id;


    public Customer(String customerFirstName, String customerLastName, int id) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerFirstName='" + customerFirstName + '\'' +
                ", customerLastName='" + customerLastName + '\'' +
                ", id=" + id +
                '}';
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerFirstName);
        dest.writeString(this.customerLastName);
        dest.writeInt(this.id);
    }

    protected Customer(Parcel in) {
        this.customerFirstName = in.readString();
        this.customerLastName = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
