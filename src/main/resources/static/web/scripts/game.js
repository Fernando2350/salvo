
/*
============================
============================
*/
$.getJSON("/api/games").done(function recurse(data) {
        if(data[0].player != null){
            document.getElementById("user-logged-in-main").innerText = data[0].player.name;
        } else {
            $("#logout-form-main").toggle();
            location.href="/web/games.html";
        }
    });

    $("#logout-form-game-view").click(function(){
            $.post("/api/logout")
            .done( function() {
                console.log("logged out");
                location.reload();
            }).fail( function(){
                console.log("There's something wrong about this. Try again.");
                alert("A frog has jumped out into our servers. It's a mess. Try again in a few minutes");
            });
        });

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

// var prueba = document.getElementById("prueba");

// Construyo la URL que enviaré por parámetro a la llamada de AJAX
function makeUrl() {
  var gamePlayer_Id = getParameterByName('gp');
  return '/api/game_view/' + gamePlayer_Id;
}
//  console.log(makeUrl());

// Función AJAX para traer el JSON de cada gamePlayer
function ajaxCallURL() {
    var ajxCll = $.ajax({
    url: makeUrl(),
    type: 'GET'
    });
    return ajxCll;
}

ajaxCallURL().done(function createTables(data) {
    console.log("Listo");
    console.log(data);
    var playersTag = document.getElementById("players"); // Traigo la etiqueta H3 del HTML
    var versus = ""; // Inicializo la variable para los Strings que luego volcaré dentro de la etiqueta anterior
    var gpViewId = getParameterByName('gp'); // Obtengo el parámetro que viaja en la variable gp de mi URL
    console.log("listo");
/*    for(o in data){
        console.log(data[o]);
    }
    console.log(data.ships);
    var salvoes4Player = data.salvoes;
// Itero los barcos del gamePlayer
    var ships4Player = data.ships;
    for(e in ships4Player){
        console.log(ships4Player[e].locations);
// Itero las ubicaciones de cada barco
        var shLoc4Play = ships4Player[e].locations;
// Por cada String de ubicación del barco, busco la celda en la grilla y la pinto de un color
        for(i in shLoc4Play){
            console.log(shLoc4Play[i]);
            var cellId = document.getElementById(shLoc4Play[i]);
            cellId.style.backgroundColor = "#660000";
// Itero los salvoes de cada jugador
            for(c in salvoes4Player){
                    if(gpViewId == salvoes4Player[c].player){  // si el id de la url es igual al id del gamePlayer...
                        for(d in salvoes4Player[c].locations){ // (itero las ubicaciones de los salvoes)
                        // ...obtengo las celdas de la segunda tabla, las pinto y les escribo "SHOT"
                            var cellSId = document.getElementById("S" + salvoes4Player[c].locations[d]);
                            cellSId.innerText = "SHOT";
                            cellSId.style.color = "#FFF";
                            cellSId.style.backgroundColor = "#000";
                        }
                    } else {  // si no es el mismo id (es el del otro gamePlayer)...
                        for(m in salvoes4Player[c].locations){ // (itero las ubicaciones de los salvoes de ese gamePlayer...
                            if(salvoes4Player[c].locations[m].indexOf(shLoc4Play[i]) != -1){ // y si esa ubicación aparece entre los barcos del otro gamePlayer...
                                // escribo en la celda correspondiente una "X" y el número de turno en que sucedió
                                cellId.innerText = "X" + "-T" + salvoes4Player[c].turn;
                                cellId.style.color = "#FFF";
                            }
                        }
                    }
                }
        }
    }
    */
    //var gpViewId = getParameterByName('gp');
   console.log(gpViewId);
//   console.log(data.gamePlayers[0] + " " + data.gamePlayers[1]);
// Doble iteración para mostrar participantes
// Por un problema de ordenamiento del Array,
// no podía mostrar siempre en primer lugar al gp que está viendo el juego. Esta fue la solución:
    for(k in data.gamePlayer){
        var gppk = data.gamePlayer[k];
        console.log(gppk);
        if(data.gamePlayer[k].id == gpViewId){
            versus += gppk.player.username + " (you) vs ";
            console.log("right");
        }
    }
    for(l in data.gamePlayer){
        var gppl = data.gamePlayer[l];
        console.log(gppl);
        if(data.gamePlayer[l].id != gpViewId){
            versus += gppl.player.username;
         }
        // versus += " vs ";
    }
    playersTag.innerHTML = versus; // los Strings guardados en versus se convierten en HTML de la etiqueta H3 de game.html
    //document.getElementsByClassName("cell").style.minWidth = "66px";
});

let data, player, opponent;
let params = new URLSearchParams(location.search);
let gamePlayerId = params.get('gp');
let gpId = +gamePlayerId;

getGameData(gpId,true)

function getGameData(gpId, viewShips){

	document.getElementById("dock").innerHTML = `<div id="display">
									                <p>Welcome...</p>
									            </div>
									            <div id="board">

									            </div>
									            `
	document.getElementById("grid-ships").innerHTML = ""
	document.getElementById("grid-salvoes").innerHTML = ""

	createGrid(11, document.getElementById('grid-ships'), 'ships')


	fetch('/api/game_view/' + gamePlayerId)
	.then(res => {
		if(res.ok){
			return res.json()

		}else{
			throw new Error(res.statusText)
		}
	})
	.then(json => {
		 data = json

		 if(data.ships.length > 0){
		 	getShips(data.ships)
		 	createGrid(11, document.getElementById('grid-salvoes'), 'salvoes')
		 	document.getElementById('grid-ships').classList.add('active')
		 	document.getElementById('grid-salvoes').classList.remove('active')
		 	document.getElementById("board").innerHTML += '<div class="hide" id="fire"><button class="btn" onclick="readyToShoot()">Fire!</button></div>'
		 	document.getElementById("board").innerHTML += '<div><button id="grid-view" class="btn" onclick="gridView(event)">View Salvoes</button></div>'
		 	if(!viewShips){
		 		document.getElementById('grid-view').click()
		 	}
		 	target()
		 } else {
		 	document.getElementById("board").innerHTML += '<div><button class="btn" onclick="addShips()">Add Ships</button></div>'
		 	createShips('carrier', 5, 'horizontal', document.getElementById('dock'),false)
			createShips('battleship', 4, 'horizontal', document.getElementById('dock'),false)
			createShips('submarine', 3, 'horizontal', document.getElementById('dock'),false)
			createShips('destroyer', 3, 'horizontal', document.getElementById('dock'),false)
			createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'),false)

		 }

		 data.gamePlayer.forEach(e =>{
		 	if(e.id == gpId){
		 		player = e.player
		 	} else {
		 		opponent = e.player
		 	}
		 })
		 if(data.salvoes.length > 0){
		 	getSalvoes(data.salvoes, player.id)
		 }




	})
	.catch(error => console.log(error))

}

function getShips(ships){

	ships.forEach(ship => {

		createShips(ship.type,
					ship.locations.length,
					ship.locations[0][0] == ship.locations[1][0] ? "horizontal" : "vertical",
					document.getElementById(`ships${ship.locations[0]}`),
					true
					)



	})
}

function getSalvoes(salvoes, playerId){
	salvoes.forEach(salvo => {
		salvo.locations.forEach(loc => {
			if(salvo.player == playerId){
				let cell = document.getElementById("salvoes"+loc)
				salvo.hits.includes(loc) ? cell.classList.add('hit') : cell.classList.add('water')
				cell.innerText = salvo.turn
			}else{
				let cell = document.getElementById("ships"+loc)
				if(cell.classList.contains('busy-cell')){
					cell.style.background = "red"
				}

			}
		})

		if(salvo.sunken != null){
			salvo.sunken.forEach(ship => {
				if(salvo.player == playerId){
					ship.locations.forEach(loc => {
						let cell = document.getElementById("salvoes"+loc)
						cell.classList.add('sunken')
					})
				}

			})
		}

	})
}

let shipsCreated = [];
/*	{
		"type": "Carrier",
		"locations": ["A1", "A2", "A3", "A4", "A5"]
	},
	{
		"type": "Battleship",
		"locations": ["A10", "B10", "C10", "D10"]
	},
	{
		"type": "Submarine",
		"locations": ["C1", "C2", "C3"]
	},
	{
		"type": "Destroyer",
		"locations": ["D1", "D2", "D3"]
	},
	{
		"type": "Patrol Boat",
		"locations": ["E1", "E2"]
	}
]
*/

function addShips(){
	let ships = []
    var countItems = 0;
    var countHLocations = 0;
    var countVLocations = 0;
	document.querySelectorAll(".grid-item").forEach( item => {
	    countItems++;
	    console.log(countItems);
		let ship = {}

		ship.type = item.id
		ship.locations = []

		if(item.dataset.orientation == "horizontal"){
		    console.log("Testing: ok");
			for(i = 0; i < item.dataset.length; i++){
			    countHLocations++;
			    console.log(countHLocations);
				ship.locations.push(item.dataset.y + (parseInt(item.dataset.x) + i))
			}
		}else{
			for(i = 0; i < item.dataset.length; i++){
			    countVLocations++;
			    console.log(countVLocations);
				ship.locations.push(String.fromCharCode(item.dataset.y.charCodeAt() + i) + item.dataset.x)
			}
		}

		ships.push(ship)
	})

	sendShips(ships,gpId)
}


function sendShips(ships, gpId){
    console.log(ships);
	let url = '/api/games/players/' + gamePlayerId + '/ships'
	let init = {
		method: 'POST',
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(ships)
	}
	fetch(url,init)
	.then(res => {
		if(res.ok){
			return res.json()
		}else{
			return Promise.reject(res.json())
		}
	})
	.then(json => {

		getGameData(gp,true)
	})
	.catch(error => error)
	.then(error => console.log(error))

}

$('#createShips').click(function(){
    sendhips(shipsCreated);
});

function gridView(ev){

	let text = ev.target.innerText == "View Salvoes" ? "View Ships" : "View Salvoes"

	ev.target.innerText = text

	document.querySelectorAll(".grid").forEach(grid => grid.classList.toggle("active"))
	document.getElementById("fire").classList.toggle("hide")
}

function target(){
	document.querySelectorAll("#grid-salvoes .grid-cell").forEach(cell => cell.addEventListener('click',aim))
}

function aim(evt){
	if(!evt.target.classList.contains('hit')){
		if(document.querySelectorAll('.target').length < 5){
			evt.target.classList.toggle('target')
		}else{
			console.log('to many shots')
		}
	} else{
		console.log('you already have shooted here')
	}
}

function readyToShoot(){

	let shots = Array.from(document.querySelectorAll('.target')).map(cell => cell.dataset.y + cell.dataset.x)

	shoot(shots, gpId)
}

function shoot(shots,gpId){
	let url = '/api/games/players/'+ gamePlayerId +'/salvoes'
	let init = {
		method: 'POST',
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(shots)
	}
	fetch(url,init)
	.then(res => {
		if(res.ok){
			return res.json()
		}else{
			return Promise.reject(res.json())
		}
	})
	.then(json => {

		getGameData(gpId,false)
	})
	.catch(error => error)
	.then(error => console.log(error))

}

/*
=====================================
=====================================
*/
