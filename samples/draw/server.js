/**
 * The server component of mobl draw
 * Requires node.js (http://nodejs.org) and npm (http://npmjs.org)
 * to install required libraries: npm install connect express socket.io
 */
var sys = require('sys');
var connect = require('connect');
var express = require('express');
var io = require('socket.io');
var repl = require('repl');
var fs = require('fs')

function log(o) {
  sys.print(sys.inspect(o) + "\n");
}

var app = express.createServer(
  connect.staticProvider('./www')
);

app.listen(8888);

var socket = io.listen(app);

clients = [];
history = [];

var buf = fs.readFileSync('dump.json');
history = JSON.parse(buf.toString('utf-8'));

saveHistory = function() {
  var f = fs.openSync('dump.json', 'w');
  fs.writeSync(f, JSON.stringify(history));
  fs.closeSync(f);
};

socket.on('connection', function(client) {
    clients.push(client);
    setTimeout(function() {
      history.forEach(function(msg) {
        client.send(msg);
      });
    }, 250);
    client.on('message', function(msg) {
        history.push(msg);
        clients.forEach(function(c) {
            if(c === client) return;
            c.send(msg);
          });
      });
    client.on('disconnect', function() {
        for(var i = 0; i < clients.length; i++) {
          if(clients[i] === client) {
            clients.splice(i, 1);
          }
        }
      });
  });

console.log('Server running at http://127.0.0.1:8888/');
repl.start('mobl-draw> ');
