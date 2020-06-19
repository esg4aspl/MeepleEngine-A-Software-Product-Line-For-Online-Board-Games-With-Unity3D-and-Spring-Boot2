
const WebSocket = require('ws');
const readline = require("readline");

// console initialization
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

rl.on("close", function() {
  console.log("\nBYE BYE !!!");
  process.exit(0);
});

const ws = new WebSocket('ws://localhost:8080/game');

var myArgs = process.argv.slice(2);

ws.on('open', function open() {
  console.log("connected!");
  ws.send(`{"channel":"Player", "data":{"playerId":"${myArgs[0]}"}}`, () => {
    console.log("player message sent!");
    rl.question("Enter data: ", function(username) {
      ws.send('{"channel":"Click", "data":{"buttonName": "Spymaster" }}', () => {
        console.log("messages sent!");
      });
    })
  });
});

ws.on('message', function incoming(data) {
  console.log(data);
});