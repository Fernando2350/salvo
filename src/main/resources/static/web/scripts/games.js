        // Solicito el JSON y, cuando es un éxito, lo itero y voy creando los elementos de lista
        var gamesJson = $.getJSON("/api/games").done(function recurse(data) {
            if(data[0].player != null){
                document.getElementById("user-logged-in-main").innerText = data[0].player.name;
                $("#login-form").toggle();
            } else {
                $("#logout-form-main").toggle();
                $("#btnCrtGame").toggle();
            }
            var table;
            table = "<tr><th>Name</th><th>Total</th><th>Win</th><th>Tie</th><th>Lost</th></tr>";
            var uName = [];
            for(let k=0; k<data.length; k++){  // Itero el JSON de api/games
                //console.log("mmm");
                    for(let h=0; h<data[k].games.gamePlayers.length; h++){  // Itero cada gamePlayer
                    //console.log("mmmmm");
                        if(uName.indexOf(data[k].games.gamePlayers[h].player.email) == -1){ // Si el email no está en la lista
                            uName.push(data[k].games.gamePlayers[h].player.email);  // lo agrego
                            //console.log("Ok");
                            table += "<tr><td>" + data[k].games.gamePlayers[h].player.email + "</td><td>" + getPointsPerPlayer(data[k].games.gamePlayers[h].player.email) + "</td><td>" + getWins(data[k].games.gamePlayers[h].player.email) + "</td><td>" + getTieds(data[k].games.gamePlayers[h].player.email) + "</td><td>" + getDefeats(data[k].games.gamePlayers[h].player.email) + "</td></tr>";
                        } else {
                        //console.log("Already Exists");
                        }
                    }
            }

            var lBoard = document.getElementById("leaderboard").innerHTML = table;
            console.log( "Success" );

            function getPointsPerPlayer(player2Compare){
                      var pTotal = 0.0;
                      for(let k=0; k<data.length; k++){  // Itero el JSON de api/games
                          for(let h=0; h<data[k].games.gamePlayers.length; h++){  // Itero cada gamePlayer
                              var player = data[k].games.gamePlayers[h].player.email;
                              if(player === player2Compare){
                                  if(data[k].games.gamePlayers[h].player.scores != null){
                                      pTotal += parseFloat(data[k].games.gamePlayers[h].player.scores);
                                  }
                              }
                          }
                      }
                      return pTotal;
                  }

                  function getWins(player2Compare){
                      var win = 0;
                      for(let k=0; k<data.length; k++){  // Itero el JSON de api7/games
                          for(let h=0; h<data[k].games.gamePlayers.length; h++){   // Itero cada gamePlayer
                              var player = data[k].games.gamePlayers[h].player.email;
                              var points = data[k].games.gamePlayers[h].player.scores;
                              if(player === player2Compare && points != null && points == "1.0"){ // Mismo jugador? Puntos igual a 1? Sumale 1 al win
                                  win++;
                              }
                          }
                      }
                      return win;
                  }

                  function getTieds(player2Compare){
                      var tied = 0;
                      for(let k=0; k<data.length; k++){  // Itero el JSON de api7/games
                          for(let h=0; h<data[k].games.gamePlayers.length; h++){  // Itero cada gamePlayer
                               var player = data[k].games.gamePlayers[h].player.email;
                               var points = data[k].games.gamePlayers[h].player.scores;
                               if(player === player2Compare && points != null && points == "0.5"){
                                   tied++;
                               }
                          }
                      }
                      return tied;
                   }


                  function getDefeats(player2Compare){
                      var lost = 0;
                      for(let k=0; k<data.length; k++){  // Itero el JSON de api7/games
                          for(let h=0; h<data[k].games.gamePlayers.length; h++){   // Itero cada gamePlayer
                              var player = data[k].games.gamePlayers[h].player.email;
                              var points = data[k].games.gamePlayers[h].player.scores;
                              if(player === player2Compare && points != null && points == "0.0"){
                                  lost++;
                              }
                          }
                      }
                      return lost;
                  }
                  // Obtengo la lista ordenada de id="lista"
                  var games = document.getElementById("lista");
                  var lista ="";
                          for(let i=0; i<data.length; i++){

                              lista += "<li>" + "<div class='col-lg'>" + "<li>" + "<ul class='gameInfo'>" + "<li>" + "Game " + data[i].games.id + "</li>" + "<li>" + "Created: " + data[i].games.created + "</li>" + "<li>" + "Players: " + "<ol>";
                              if(data[0].player != null){
                                for(let e=0; e<data[i].games.gamePlayers.length; e++){
                                    var gPlayer1 = data[i].games.gamePlayers[e];
                                        if(data[0].player.id === gPlayer1.player.id){
                                            //console.log(data[i].games.gamePlayers[e].id);
                                                lista += "<a href='/web/game.html?gp=" + data[i].games.gamePlayers[e].id + "'>";
                                        }
                                }
                              }
                              for(let e=0; e<data[i].games.gamePlayers.length; e++){
                                  var gPlayer = data[i].games.gamePlayers[e];
                                  //console.log(gPlayer);
                                  lista += "<ul>" + "<li>" + "User " + gPlayer.player.email + "</li>" + "</ul>";
                              }
                              if(data[0].player != null){
                                var count = 0;
                                for(let e=0; e<data[i].games.gamePlayers.length; e++){
                                    var gPlayer1 = data[i].games.gamePlayers[e];
                                    count++;
                                    if(data[0].player.id === gPlayer1.player.id){
                                        lista += "</a>";
                                    }
                                }
                                if(count == 1){
                                    lista += "<button type='button' class='btn btn-dark' id='" + data[i].games.id + "' data-game=''>Join</button>";
                                }
                              }
                              lista += "</ol>" + "</li>" + "</ul>" + "</li>" + "</div>" + "</li>";
                          }
                          // Envío la cadena guardada en "lista" al HTML de la lista ordenada accedida al principio
                          games.innerHTML += lista;
                          for(let i=0; i<data.length; i++){
                            if(document.getElementById(data[i].games.id) != null){
                                document.getElementById(data[i].games.id).setAttribute('data-game', data[i].games.id);
                            }
                          }
                          //console.log("I can't stop succeeding...");
        })
      .fail(function() { /* Por si falla... */
       console.log( "Salió mal! Rajemos!" );
      })
      .always(function() { /* Para lanzar por consola cuando se completa, pase lo que pase */
       console.log( "Completed." );
      });

        var form = document.getElementById("login-form");

        $("#login").click(function(){
            $.post("/api/login",
            {
                username: form.elements.namedItem("username").value,
                password: form.elements.namedItem("password").value
            }).done( function() {
                console.log("logged in");
                form.elements.namedItem("username").value = "";
                form.elements.namedItem("password").value = "";
                location.reload();
                location.reload();
            }).fail( function(){
                console.log("Invalid password or username");
                alert("Invalid password or username");
            });
        });

        $("#logout-form-main").click(function(){
            $.post("/api/logout")
            .done( function() {
                console.log("logged out");
                location.reload();
            }).fail( function(){
                console.log("There's something wrong about this. Try again.");
                alert("A frog has jumped out into our servers. It's a mess. Try again in a few minutes");
            });
        });

        $("#signUp").click(function(){
                    $.post("/api/players",
                    {
                        username: form.elements.namedItem("username").value,
                        password: form.elements.namedItem("password").value
                    }).done( function() {
                        console.log("New User signed up");
                        $.post("/api/login",
                        {
                            username: form.elements.namedItem("username").value,
                            password: form.elements.namedItem("password").value
                        }).done( function() {
                            console.log("New User logged in");
                        });
                        form.elements.namedItem("username").value = "";
                        form.elements.namedItem("password").value = "";
                        location.reload();
                    }).fail( function() {
                        alert("User already exists");
                    });
                });

        $("#btnCrtGame").click(function(){
                    fetch('/api/games',{
                        method: 'POST'
                    }).then(res => {
                      	if(res.ok){
                      	    return res.json()
                      	}else{
                      		return Promise.reject(res.json())
                      	}
                    }).then(json => {

                      	location.href = '/web/game.html?gp=' + json.gpId
                    }).catch(error => error)
                    .then(error => console.log(error))
        });

        $(document).ready(function(){
            $('[data-game]').click(function(){ console.log("Holas"); var gpId = $(this).data('game');
                $.post('/api/games/' + gpId + '/players').done(function(response){
                    window.location.assign('/web/game.html?gp=' + response.gpId + '');
            }).fail(function(response){
                alert(response.responseJSON.error);
            });
            });
        });