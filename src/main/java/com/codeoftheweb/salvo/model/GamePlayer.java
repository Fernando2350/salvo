package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime joinDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;
    private String userName;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer(){
    }

    public GamePlayer(LocalDateTime joinDate, Game game, Player player){
        this.joinDate = joinDate;
        this.game = game;
        this.player = player;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDateTime getJoinDate(){
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate){
        this.joinDate = joinDate;
    }

    public Game getGame(){
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship){
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }

    public Set<Salvo> getSalvo(){
        return this.salvoes;
    }

    public void addSalvo(Salvo salvo){
        this.salvoes.add(salvo);
        salvo.setGamePlayer(this);
    }

    public Score getScore(){
        return this.player.getScore(game);
    }

    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getId()).findFirst().orElse(null);
    }

    public Map<String, Object> gamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO());

        Score score =this.getPlayer().getScore(this.getGame());
        if(score != null)
            dto.put("score", score.getPoints());
        else
            dto.put("score", null);


        return dto;
    }
}


