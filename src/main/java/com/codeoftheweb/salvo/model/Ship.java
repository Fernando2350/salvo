package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private String shipType;
    @ManyToOne
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="ship_locations")
    private List<String> shipLocationsList = new ArrayList<>();

    public Ship(){}

    public Ship(String shipType, List<String> shipLocationsList){
        this.shipType = shipType;
        this.shipLocationsList = shipLocationsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShipLocationsList() {
        return shipLocationsList;
    }

    public void setShipLocationsList(List<String> shipLocationsList) {
        this.shipLocationsList = shipLocationsList;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String, Object> shipDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", this.getShipType());
        dto.put("locations", this.getShipLocationsList());
        return dto;
    }
}
