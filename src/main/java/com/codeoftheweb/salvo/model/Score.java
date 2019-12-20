package com.codeoftheweb.salvo.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Player player;

    /*private double win = 1.0;
    private double tie = 0.5;
    private double lost = 0.0;*/

    private double points;

    private LocalDateTime finishDate;

    public Score(){}

    public Score(Game game, Player player, double points, LocalDateTime finishDate){
        this.game = game;
        this.player = player;
        this.points = points;
        this.finishDate = finishDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getPoints() {
        return points;
    }

    public void setScore(double points) {
        this.points = points;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
