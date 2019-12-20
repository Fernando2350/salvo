package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){
		return (args) -> {

			double win = 1.0;
			double tie = 0.5;
			double lost = 0.0;
			// Instancio la fecha pedida
			LocalDateTime date = LocalDateTime.of(2018, 2, 17, 15, 20, 15);

			// Instancio los 6 juegos
			Game game1 = new Game(date);
			Game game2 = new Game(date.plus(1, ChronoUnit.HOURS));
			Game game3 = new Game(date.plus(2, ChronoUnit.HOURS));
			Game game4 = new Game(date.plus(3, ChronoUnit.HOURS));
			Game game5 = new Game(date.plus(4, ChronoUnit.HOURS));
			Game game6 = new Game(date.plus(5, ChronoUnit.HOURS));

			// Instancio los 5 jugadores
			Player player1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player player2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", passwordEncoder.encode("42"));
			Player player3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			Player player4 = new Player("Tony", "Almeida", "t.almeida@ctu.gov", passwordEncoder.encode("mole"));
			Player player5 = new Player("d", "Palmer", "d.palmer@whitehouse.gov", passwordEncoder.encode("nada"));

			// Guardo los jugadores
			player1 = playerRepository.save(player1);
			player2 = playerRepository.save(player2);
			player3 = playerRepository.save(player3);
			player4 = playerRepository.save(player4);
			player5 = playerRepository.save(player5);

			// Guardo los juegos
			game1 = gameRepository.save(game1);
			game2 = gameRepository.save(game2);
			game3 = gameRepository.save(game3);
			game4 = gameRepository.save(game4);
			game5 = gameRepository.save(game5);
			game6 = gameRepository.save(game6);

			GamePlayer gamePlayer1 = new GamePlayer();
			GamePlayer gamePlayer2 = new GamePlayer();
			GamePlayer gamePlayer3 = new GamePlayer();
			GamePlayer gamePlayer4 = new GamePlayer();
			GamePlayer gamePlayer5 = new GamePlayer();
			GamePlayer gamePlayer6 = new GamePlayer();
			GamePlayer gamePlayer7 = new GamePlayer();
			GamePlayer gamePlayer8 = new GamePlayer();
			GamePlayer gamePlayer9 = new GamePlayer();
			GamePlayer gamePlayer10 = new GamePlayer();
			GamePlayer gamePlayer11 = new GamePlayer();

			// Instancio y guardo jugadores en sendos juegos
			gamePlayer1 = gamePlayerRepository.save(new GamePlayer(date.plus(1, ChronoUnit.HOURS), game1, player1));
			gamePlayer2 = gamePlayerRepository.save(new GamePlayer(date.plus(1, ChronoUnit.HOURS), game1, player2));
			gamePlayer3 = gamePlayerRepository.save(new GamePlayer(date.plus(1, ChronoUnit.HOURS), game2, player1));
			gamePlayer4 = gamePlayerRepository.save(new GamePlayer(date.plus(1, ChronoUnit.HOURS), game2, player2));
			gamePlayer5 = gamePlayerRepository.save(new GamePlayer(date.plus(2, ChronoUnit.HOURS), game3, player2));
			gamePlayer6 = gamePlayerRepository.save(new GamePlayer(date.plus(2, ChronoUnit.HOURS), game3, player4));
			gamePlayer7 = gamePlayerRepository.save(new GamePlayer(date.plus(3, ChronoUnit.HOURS), game4, player1));
			gamePlayer8 = gamePlayerRepository.save(new GamePlayer(date.plus(3, ChronoUnit.HOURS), game4, player2));
			gamePlayer9 = gamePlayerRepository.save(new GamePlayer(date.plus(4, ChronoUnit.HOURS), game5, player4));
			gamePlayer10 = gamePlayerRepository.save(new GamePlayer(date.plus(4, ChronoUnit.HOURS), game5, player1));
			gamePlayer11 = gamePlayerRepository.save(new GamePlayer(date.plus(5, ChronoUnit.HOURS), game6, player5));


			gamePlayer1.addShip(new Ship("submarine", Arrays.asList("E1", "F1", "G1")));
			gamePlayer1.addShip(new Ship("destroyer", Arrays.asList("H2", "H3", "H4")));
			gamePlayer1.addShip(new Ship("patrol_boat", Arrays.asList("B4", "B5")));

			gamePlayer2.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer2.addShip(new Ship("patrol_boat", Arrays.asList("F1", "F2")));

			gamePlayer3.addShip(new Ship("patrol_boat", Arrays.asList("C6","C7")));
			gamePlayer3.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));

			gamePlayer4.addShip(new Ship("submarine", Arrays.asList("A2", "A3", "A4")));
			gamePlayer4.addShip(new Ship("patrol_boat", Arrays.asList("G6", "H6")));

			gamePlayer5.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer5.addShip(new Ship("patrol_boat", Arrays.asList("C6", "C7")));

			gamePlayer6.addShip(new Ship("submarine", Arrays.asList("A2", "A3", "A4")));
			gamePlayer6.addShip(new Ship("patrol_boat", Arrays.asList("G6", "H6")));

			gamePlayer7.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer7.addShip(new Ship("patrol_boat", Arrays.asList("C6", "C7")));

			gamePlayer8.addShip(new Ship("submarine", Arrays.asList("A2", "A3", "A4")));
			gamePlayer8.addShip(new Ship("patrol_boat", Arrays.asList("G6", "H6")));

			gamePlayer9.addShip(new Ship("destroyer", Arrays.asList("B5", "C5", "D5")));
			gamePlayer9.addShip(new Ship("patrol_boat", Arrays.asList("C6", "C7")));

			gamePlayer10.addShip(new Ship("submarine", Arrays.asList("A2", "A3", "A4")));
			gamePlayer10.addShip(new Ship("patrol_boat", Arrays.asList("G6", "H6")));

			gamePlayer1.addSalvo(new Salvo(1, Arrays.asList("B5", "C5", "F1")));
			gamePlayer2.addSalvo(new Salvo(1, Arrays.asList("B4", "B5", "B6")));

			gamePlayer1.addSalvo(new Salvo(2, Arrays.asList("F2", "D5")));
			gamePlayer2.addSalvo(new Salvo(2, Arrays.asList("E1", "H3", "A2")));

			gamePlayer3.addSalvo(new Salvo(1, Arrays.asList("A2", "A4", "G6")));
			gamePlayer4.addSalvo(new Salvo(1, Arrays.asList("B5", "D5", "C7")));

			gamePlayer3.addSalvo(new Salvo(2, Arrays.asList("A3", "H6")));
			gamePlayer4.addSalvo(new Salvo(2, Arrays.asList("C5", "C6")));

			gamePlayer5.addSalvo(new Salvo(1, Arrays.asList("G6", "H6", "H4")));
			gamePlayer6.addSalvo(new Salvo(1, Arrays.asList("H1", "H2", "H3")));

			gamePlayer5.addSalvo(new Salvo(2, Arrays.asList("A2", "A3", "D8")));
			gamePlayer6.addSalvo(new Salvo(2, Arrays.asList("E1", "F2", "G3")));

			gamePlayer7.addSalvo(new Salvo(1, Arrays.asList("A3", "A4", "F7")));
			gamePlayer8.addSalvo(new Salvo(1, Arrays.asList("B5", "C6", "H1")));

			gamePlayer7.addSalvo(new Salvo(2, Arrays.asList("A2", "G6", "H6")));
			gamePlayer8.addSalvo(new Salvo(2, Arrays.asList("C5", "C7", "D5")));

			gamePlayer9.addSalvo(new Salvo(1, Arrays.asList("A1", "A2", "A3")));
			gamePlayer10.addSalvo(new Salvo(1, Arrays.asList("B5", "B6", "C7")));

			gamePlayer9.addSalvo(new Salvo(2, Arrays.asList("G6", "G7", "G8")));
			gamePlayer10.addSalvo(new Salvo(2, Arrays.asList("C6", "D6", "E6")));

			gamePlayer10.addSalvo(new Salvo(3, Arrays.asList("H1", "H8")));


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);

			Score scoreG1P1 = new Score(game1, player1, win, date);
			Score scoreG1P2 = new Score(game1, player2, lost, date);
			Score scoreG2P1 = new Score(game2, player1, tie, date);
			Score scoreG2P2 = new Score(game2, player2, tie, date);
			Score scoreG3P2 = new Score(game3, player2, win, date);
			Score scoreG3P4 = new Score(game3, player4, lost, date);
			Score scoreG4P1 = new Score(game4, player1, tie, date);
			Score scoreG4P2 = new Score(game4, player2, tie, date);

			player1.addScore(scoreG1P1);
			player1.addScore(scoreG2P1);
			player1.addScore(scoreG4P1);
			player2.addScore(scoreG1P2);
			player2.addScore(scoreG2P2);
			player2.addScore(scoreG3P2);
			player2.addScore(scoreG4P2);
			player4.addScore(scoreG3P4);

			// Guardo los jugadores
			player1 = playerRepository.save(player1);
			player2 = playerRepository.save(player2);
			player3 = playerRepository.save(player3);
			player4 = playerRepository.save(player4);
			player5 = playerRepository.save(player5);

		/*	player1.addScore(new Score(game1, win, date));
			player2.addScore(new Score(game1, lost, date));

			player1.addScore(new Score(game2, tie, date));
			player2.addScore(new Score(game2, tie, date));

			player2.addScore(new Score(game3, win, date));
			player4.addScore(new Score(game3, lost, date));

			player2.addScore(new Score(game4, tie, date));
			player1.addScore(new Score(game4, tie, date));


			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);*/
		};
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{

	@Autowired
	PlayerRepository playerRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			// System.out.println(player);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		}).passwordEncoder(passwordEncoder());
	}
}

