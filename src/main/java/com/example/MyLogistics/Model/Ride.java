package com.example.MyLogistics.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Rides")
public class Ride {
    @Id
    private String id;
    private String customerName;
    private String customerPhoneNumber;
    private String customerAddress;
    private String destinationAddress;
    private String riderName;
    private String riderPhoneNumber;
    private List<String> messageBox;
    private boolean atAddress;
    private boolean rideStarted;

}
