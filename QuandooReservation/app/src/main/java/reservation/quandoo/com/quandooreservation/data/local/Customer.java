package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by sohailaziz on 19/12/17.
 */

@Entity
public class Customer {

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
}
