package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo; // Repositorio de games

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private SalvoGameRepository salvoGameRepo;

/*    @RequestMapping("/api")
    public List<SalvoGame> getAll(){
        return salvoGameRepo.findAll();
    }*/

    @RequestMapping("/games") // Queremos que ésta sea la ruta

    // Método para iterar sobre los juegos creados y generar el JSON con los campos y valores adecuados
    private List<Map<String, Object>> getGames(Authentication authentication) {

        List<Game> gamesList = gameRepo.findAll(); // Obtengo todos los juegos existentes y los envío a una lista
        List<Map<String, Object>> result = new ArrayList<>(); // Creo una lista de Maps para retornar el resultado

//        if(authentication != null) {
        // Itero la lista de juegos
        for (Game game : gamesList) {

            if (game != null) {
                Map<String, Object> mapResult = new LinkedHashMap<>();
                Map<String, Object> playerLoggedInDTO = new LinkedHashMap<>();
                Map<String, Object> metaMapResult = new LinkedHashMap<>();
                if(authentication != null) {
                    for (GamePlayer gamePlayer : game.getGamePlayers()) {

                        if (authentication.getName().equals(gamePlayer.getPlayer().getUserName())) {
                            playerLoggedInDTO.put("id", gamePlayer.getPlayer().getId());
                            playerLoggedInDTO.put("name", gamePlayer.getPlayer().getUserName());
                            metaMapResult.put("player", playerLoggedInDTO);
                        }
                    }
                }

                mapResult.put("id", game.getId()); // Por cada campo, busco id y fecha de creación
                mapResult.put("created", game.getCreationDate()); // y los pongo en sus correspondientes campos
                List<Map<String, Object>> gamePlayers = new ArrayList<>();

                // Itero y obtengo los jugadores en cada juego
                for (GamePlayer gamePlayer : game.getGamePlayers()) {

                    if (gamePlayer != null) {

                        Map<String, Object> gamePlayerDTO = new LinkedHashMap<>();
                        Map<String, Object> playerDTO = new LinkedHashMap<>();

                        gamePlayerDTO.put("id", gamePlayer.getId()); // Rescato id de jugador en juego y lo llevo a su campo
                        playerDTO.put("id", gamePlayer.getPlayer().getId()); // Traigo id de jugador
                        playerDTO.put("email", gamePlayer.getPlayer().getUserName()); // Traigo email de jugador
                        if (gamePlayer.getScore() != null) {
                            playerDTO.put("scores", gamePlayer.getScore().getPoints());
                        }
                        gamePlayerDTO.put("player", playerDTO); // Creo el campo "player", donde vuelco el id y el email y lo hago depender del id de jugador en juego

                        gamePlayers.add(gamePlayerDTO); // Incorporo este tramo al general
                    }
                }

                mapResult.put("gamePlayers", gamePlayers); // Creo el campo "gamePlayers", que envuelve lo anterior
                metaMapResult.put("games", mapResult);
                result.add(metaMapResult); // Lo agrego a la lista resultante
            }
        }
        return result;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createGame(Authentication authentication){
        ResponseEntity<Map<String, Object>> response;
        if(authentication == null){
            response = new ResponseEntity<>(makeMap("error", "You must be logged in"), HttpStatus.UNAUTHORIZED);
        }else {
            Player player = playerRepo.findByUserName(authentication.getName());
            Game newGame = gameRepo.save(new Game(LocalDateTime.now()));
            GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(newGame.getCreationDate(), newGame, player));

            response = new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
        }

        return response;
    }

    @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> joinGame(Authentication authentication, @PathVariable long gameId){
        ResponseEntity<Map<String, Object>> response;
        if(authentication == null){
            response = new ResponseEntity<>(makeMap("error", "You must be logged in first"), HttpStatus.UNAUTHORIZED);
        }else {

            Game game = gameRepo.findById(gameId).orElse(null);
            if(game == null){
                response = new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.NOT_FOUND);
            } else if (game.getGamePlayers().size() > 1){
                response = new ResponseEntity<>(makeMap("error", "No place in this game"), HttpStatus.FORBIDDEN);
            } else {
                Player player = playerRepo.findByUserName(authentication.getName());
                if(game.getGamePlayers().stream().anyMatch(gp -> gp.getPlayer().getId() == player.getId())){
                    response = new ResponseEntity<>(makeMap("error", "User already joined"), HttpStatus.FORBIDDEN);
                } else{
                    GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(LocalDateTime.now(), game, player));
                    response = new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
                }
            }

        }

        return response;
    }

    /*       } else {
            for (Game game : gamesList) {

                if (game != null) {
                    Map<String, Object> metaMapResult = new LinkedHashMap<>();
                    Map<String, Object> mapResult = new LinkedHashMap<>();

                    mapResult.put("id", game.getId()); // Por cada campo, busco id y fecha de creación
                    mapResult.put("created", game.getCreationDate()); // y los pongo en sus correspondientes campos
                    List<Map<String, Object>> gamePlayers = new ArrayList<>();

                    // Itero y obtengo los jugadores en cada juego
                    for (GamePlayer gamePlayer : game.getGamePlayers()) {

                        if (gamePlayer != null) {

                            Map<String, Object> gamePlayerDTO = new LinkedHashMap<>();
                            Map<String, Object> playerDTO = new LinkedHashMap<>();

                            gamePlayerDTO.put("id", gamePlayer.getId()); // Rescato id de jugador en juego y lo llevo a su campo
                            playerDTO.put("id", gamePlayer.getPlayer().getId()); // Traigo id de jugador
                            playerDTO.put("email", gamePlayer.getPlayer().getUserName()); // Traigo email de jugador
                            if (gamePlayer.getScore() != null) {
                                playerDTO.put("scores", gamePlayer.getScore().getPoints());
                            }
                            gamePlayerDTO.put("player", playerDTO); // Creo el campo "player", donde vuelco el id y el email y lo hago depender del id de jugador en juego

                            gamePlayers.add(gamePlayerDTO); // Incorporo este tramo al general
                        }
                    }

                    mapResult.put("gamePlayers", gamePlayers); // Creo el campo "gamePlayers", que envuelve lo anterior
                    result.add(mapResult); // Lo agrego a la lista resultante
                }
            }
        }*/

    /*    private Map<String, Object> getGameView(@PathVariable long gamePlayerId){
        return this.gameView(gamePlayerRepo.findById(gamePlayerId).orElse(null));
    }*/

    //   @RequestMapping(path = "api/game_view/{gamePlayerId}", method = RequestMethod.POST)
    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerId, Authentication authentication){
        System.out.println(authentication.getName());
        ResponseEntity<Map<String, Object>> resp;
        GamePlayer gamePlayerParam = gamePlayerRepo.findById(gamePlayerId);
        System.out.println(gamePlayerId);
        System.out.println(gamePlayerParam.getPlayer().getId());
        Player player = playerRepo.findByUserName(authentication.getName());
        if(gamePlayerParam != null && player != null) {
            if (player.getId() != gamePlayerParam.getPlayer().getId()) {

                resp = new ResponseEntity<>(makeMap("error", "user unauthorized"), HttpStatus.UNAUTHORIZED);

            } else {
                resp = new ResponseEntity<>(this.gameView(gamePlayerParam), HttpStatus.OK);
            }
        } else {
            resp = new ResponseEntity<>(makeMap("error", "no user with that id"), HttpStatus.NOT_FOUND);
        }
        return resp;
    }


    private Map<String,Object> gameView(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();

        if(gamePlayer != null){
            dto.put("id", gamePlayer.getGame().getId());
            dto.put("creationDate", gamePlayer.getGame().getCreationDate());
            dto.put("gamePlayer", gamePlayer.getGame().getGamePlayers().stream().map(GamePlayer::gamePlayerDTO));
            dto.put("ships", gamePlayer.getShips().stream().map(Ship::shipDTO));
            dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                    .stream().flatMap(gp -> gp.getSalvo()
                            .stream().map(salvo -> salvo.salvoDTO()))
            );
        }else{
            dto.put("error", "no such game");
        }

        return dto;

    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addShips(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List<Ship> ships){
        ResponseEntity<Map<String,Object>> response;
        if(authentication == null){
            response = new ResponseEntity<>(makeMap("error", "you must be logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);
            System.out.println(gamePlayerId);
            Player player = playerRepo.findByUserName(authentication.getName());
            if(gamePlayer == null){
                response = new ResponseEntity<>(makeMap("error", "no such game"), HttpStatus.NOT_FOUND);
            } else if(gamePlayer.getPlayer().getId() != player.getId()){
                response = new ResponseEntity<>(makeMap("error", "this is not your game"), HttpStatus.UNAUTHORIZED);
            } else if(gamePlayer.getShips().size() > 0){
                response = new ResponseEntity<>(makeMap("error", "you already have ships"), HttpStatus.FORBIDDEN);
            } else if(ships == null || ships.size() != 5){
                System.out.println("Hay " + ships.size() + "barcos");
                response = new ResponseEntity<>(makeMap("error", "you must add 5 ships"), HttpStatus.FORBIDDEN);
            } else {
                if(ships.stream().anyMatch(this::isOutOfRange)){
                    response = new ResponseEntity<>(makeMap("error", "you have ships out of range"), HttpStatus.FORBIDDEN);
                } else if(ships.stream().anyMatch(this::isNotConsecutive)){
                    response = new ResponseEntity<>(makeMap("error", "your ships are not consecutive"), HttpStatus.FORBIDDEN);
                } else if(this.areOverlapped(ships)){
                    response = new ResponseEntity<>(makeMap("error", "your ships are overlapped"), HttpStatus.FORBIDDEN);
                } else{

                    ships.forEach(ship -> gamePlayer.addShip(ship));

                    gamePlayerRepo.save(gamePlayer);

                    response = new ResponseEntity<>(makeMap("success", "ships added"), HttpStatus.CREATED);
                }
            }
        }

        return response;
    }

    private boolean isOutOfRange(Ship ship){

        for(String cell : ship.getShipLocationsList()){
            if(cell == null || cell.length() < 2){
                return true;
            }
            char y = cell.substring(0,1).charAt(0);
            Integer x;
            try{
                x = Integer.parseInt(cell.substring(1));
            }catch(NumberFormatException e){
                x = 99;
            };

            if(x < 1 || x > 10 || y < 'A' || y > 'J'){
                return true;
            }
        }

        return false;
    }

    private boolean isNotConsecutive(Ship ship){
        List<String> cells = ship.getShipLocationsList();
        boolean isHorizontal = cells.get(0).charAt(0) != cells.get(1).charAt(0);
        for(int i = 0; i < cells.size(); i ++){
            if(i < cells.size() - 1){
                if(isHorizontal){
                    char yChar = cells.get(i).substring(0,1).charAt(0);
                    char compareChar = cells.get(i + 1).substring(0,1).charAt(0);
                    if(compareChar - yChar != 1){
                        return true;
                    }
                } else {
                    Integer xInt = Integer.parseInt(cells.get(i).substring(1));
                    Integer compareInt = Integer.parseInt(cells.get(i + 1).substring(1));
                    if(compareInt - xInt != 1){
                        return true;
                    }
                }
            }
            for(int j = i + 1; j < cells.size(); j ++){
                if(isHorizontal){
                    if(!cells.get(i).substring(1).equals(cells.get(j).substring(1))){
                        return true;
                    }
                }else{
                    if(!cells.get(i).substring(0,1).equals(cells.get(j).substring(0,1))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean areOverlapped(List<Ship> ships){
        List<String> allCells = new ArrayList<>();

        ships.forEach(ship -> allCells.addAll(ship.getShipLocationsList()));

        for(int i = 0; i < allCells.size(); i ++){
            for(int j = i + 1; j < allCells.size(); j ++){
                if(allCells.get(i).equals(allCells.get(j))){
                    return true;
                }
            }
        }

        return false;
    }

/*    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }*/

    /*private List<Map<String, Object>> gameView(GamePlayer gamePlayer) {

        //List<GamePlayer> gamePlayersList = gamePlayerRepo.findById(gamePlayerView.getId()); // Obtengo todos los juegos existentes y los envío a una lista
        List<Map<String, Object>> result = new ArrayList<>(); // Creo una lista de Maps para retornar el resultado
        Map<String, Object> mapResult = new LinkedHashMap<>();
        mapResult.put("id", gamePlayer.getGame().getId()); // Por cada campo, busco id y fecha de creación
        mapResult.put("created", gamePlayer.getGame().getCreationDate()); // y los pongo en sus correspondientes campos
        List<Map<String, Object>> gamePlayers = new ArrayList<>();
        // Itero la lista de juegos


                // Itero y obtengo los jugadores en cada juego
                for (GamePlayer gamePlayerIt : gamePlayer.getGame().getGamePlayers()) {

                    if(gamePlayerIt != null) {
                        Map<String, Object> gamePlayerDTO = new LinkedHashMap<>();
                        Map<String, Object> playerDTO = new LinkedHashMap<>();
                        // System.out.println(gamePlayerIt.getId());
                        gamePlayerDTO.put("id", gamePlayerIt.getId()); // Rescato id de jugador en juego y lo llevo a su campo
                        playerDTO.put("id", gamePlayerIt.getPlayer().getId()); // Traigo id de jugador
                        playerDTO.put("email", gamePlayerIt.getPlayer().getUserName()); // Traigo email de jugador
                        gamePlayerDTO.put("player", playerDTO); // Creo el campo "player", donde vuelco el id y el email y lo hago depender del id de jugador en juego

                        gamePlayers.add(gamePlayerDTO); // Incorporo este tramo al general

                    } else {
                        mapResult.put("Offside error", "The ID you are looking for doesn't exist");
                        result.add(mapResult);
                    }
                }

                mapResult.put("gamePlayers", gamePlayers); // Creo el campo "gamePlayers", que envuelve lo anterior
                mapResult.put("ships", gamePlayer.getShips().stream().map(Ship::shipDTO)); // Incorporo los barcos, que surgen del DTO de Ship.java
                mapResult.put("salvoes", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvo().stream().map(Salvo::salvoDTO)));
                result.add(mapResult); // Los agrego a la lista resultante

        return result;
    }*/

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String username, @RequestParam String password) {
        ResponseEntity<Map<String, Object>> response;
        Player player = playerRepo.findByUserName(username);
        if (username.isEmpty() || password.isEmpty()) {
            response = new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        } else if (player != null) {
            response = new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        } else {
            Player newPlayer = playerRepo.save(new Player(username, password));
            response = new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
        }
        return response;
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addSalvo(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List<String> shots){
        ResponseEntity<Map<String,Object>> response;
        if(authentication == null){
            response = new ResponseEntity<>(makeMap("error", "you must be logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);
            Player player = playerRepo.findByUserName(authentication.getName());
            if(gamePlayer == null){
                response = new ResponseEntity<>(makeMap("error", "no such game"), HttpStatus.NOT_FOUND);
            } else if(gamePlayer.getPlayer().getId() != player.getId()){
                response = new ResponseEntity<>(makeMap("error", "this is not your game"), HttpStatus.UNAUTHORIZED);
            } else if(shots.size() != 5){
                response = new ResponseEntity<>(makeMap("error", "wrong number of shots"), HttpStatus.FORBIDDEN);
            } else {
                int turn = gamePlayer.getSalvo().size() + 1;

                Salvo salvo = new Salvo(turn,shots);
                gamePlayer.addSalvo(salvo);

                gamePlayerRepo.save(gamePlayer);

                response = new ResponseEntity<>(makeMap("success", "salvo added"), HttpStatus.CREATED);

            }
        }

        return response;
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}


