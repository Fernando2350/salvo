package com.codeoftheweb.salvo.model;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private int turn;

    @ManyToOne
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvo_locations")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo(){
    }
    public Salvo(int turn, List<String> salvoLocations){
        this.turn = turn;
        this.salvoLocations = salvoLocations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public List<String> getHits(List<String> myShots, Set<Ship> opponentShips){

        List<String> allEnemyLocs = new ArrayList<>();

        opponentShips.forEach(ship -> allEnemyLocs.addAll(ship.getShipLocationsList()));

        return myShots.stream().filter(shot -> allEnemyLocs.stream().anyMatch(loc -> loc.equals(shot))).collect(Collectors.toList());

    }

    public List<Ship> getSunkenShips(Set<Salvo> mySalvoes, Set<Ship> opponentShips){

        List<String> allShots = new ArrayList<>();

        mySalvoes.forEach(salvo -> allShots.addAll(salvo.getSalvoLocations()));

        return opponentShips
                .stream()
                .filter(ship -> allShots.containsAll(ship.getShipLocationsList()))
                .collect(Collectors.toList());
    }


    public Map<String, Object> salvoDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocations());

        GamePlayer opponent = this.getGamePlayer().getOpponent();

        if(opponent != null){

            Set<Ship> enemyShips = opponent.getShips();

            dto.put("hits", this.getHits(this.getSalvoLocations(),enemyShips));

            Set<Salvo> mySalvoes = this.getGamePlayer().getSalvo().stream().filter(salvo -> salvo.getTurn() <= this.getTurn()).collect(Collectors.toSet());

            dto.put("sunken", this.getSunkenShips(mySalvoes, enemyShips).stream().map(Ship::shipDTO));
        }

        return dto;
    }


}
