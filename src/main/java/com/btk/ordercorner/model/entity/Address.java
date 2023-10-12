package com.btk.ordercorner.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adresler")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adres_id")
    private int addressId;

    @Column(name = "sehir_adi")
    @Size(max = 50)
    private String city;

    @Column(name = "ilce_adi")
    @Size(max = 50)
    private String district;

    @Column(name = "cadde_adi")
    @Size(max = 50)
    private String street;

    @Column(name = "bina_no")
    private int buildingNumber;

    @Column(name = "bina_adi")
    @Size(max = 50)
    private String buildingName;

    @ManyToMany(mappedBy = "addresses", cascade = CascadeType.ALL)
    private List<Customer> customers = new ArrayList<Customer>();

    @OneToOne(mappedBy = "address")
    private Order order;

    @Override
    public String toString() {
        return "Address{" +
        
                "id=" + addressId +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' + 
                ", buildingNumber='" + buildingNumber + '\'' +
                ", buildingName='" + buildingName + '\'' +
                '}';
    }

}
