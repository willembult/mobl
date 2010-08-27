/**
 * The server component
 * Requires node.js (http://nodejs.org) and npm (http://npmjs.org)
 * to install required libraries: npm install connect express socket.io
 */
var sys = require('sys');
var connect = require('connect');
var express = require('express');
var io = require('socket.io');
var repl = require('repl');

function log(o) {
  sys.print(sys.inspect(o) + "\n");
}

var app = express.createServer(
  connect.staticProvider('./samples/znake/www')
);

app.listen(8888);

var socket = io.listen(app);

servers = {};

socket.on('connection', function(client) {
    client.on('message', function(msg) {
        var parsed = JSON.parse(msg);
        if(parsed.type) {
          var type = parsed.type;
          switch(type) {
          case 'join':
            if(servers[parsed.name]) {
              servers[parsed.name].clients.push(client);
              client.id = Math.floor(Math.random() * 999999);
              servers[parsed.name].host.send(JSON.stringify({type: 'connect', server: parsed.name, client: client.id}));
            }
            break;
          case 'list':
            client.send(JSON.stringify(Object.keys(servers)));
            break;
          case 'create':
            //if(!servers[parsed.name]) {
              var server = {
                name: parsed.name,
                clients: [],
                host: client
              };
              servers[parsed.name] = server;
            //}
            break;
          case 'broadcast':
            if(parsed.server) {
              servers[parsed.server].clients.forEach(function(c) {
                  c.send(JSON.stringify({type: 'broadcast', server: parsed.server, message: parsed.message}));
                });
            }
            break;
          case 'message':
            servers[parsed.server].host.send(JSON.stringify({type: 'message', server: parsed.server, client: client.id, message: parsed.message}));
            break;
          }
        }
      });
    client.on('disconnect', function() {
        for(var s in servers) {
          if(servers.hasOwnProperty(s)) {
            var server = servers[s];
            for(var i = 0; i < server.clients.length; i++) {
              if(server.clients[i] === client) {
                server.clients.splice(i, 1);
                server.host.send(JSON.stringify({type: 'disconnect', server: s, client: client.id}));
              }
            }
            if(server.host === client) {
              server.clients.forEach(function(client) {
                  client.send(JSON.stringify({type: 'server-disconnect', server: s}));
                });
              log("Exit " + server.name);
              delete servers[s];
            }
          }
        }
      });
  });

console.log('Server running at http://127.0.0.1:8888/');
repl.start('server> ');
