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
  //connect.staticProvider('./www')
);

app.listen(8888);

var socket = io.listen(app);

servers = {};

socket.on('connection', function(client) {
    client.on('message', function(msg) {
        var message = JSON.parse(msg);
        var type = message.type;
        var messageId = message.id;
        switch(type) {
        case 'join':
          if(servers[message.name]) {
            client.id = Math.floor(Math.random() * 999999);
            servers[message.name].clients[client.id] = client;
            servers[message.name].host.send(JSON.stringify({
                  type: 'connect', 
                  server: message.name, 
                  client: client.id
                }));
            client.send(JSON.stringify({
                  type: 'join',
                  message: "ok"
                }));
          } else {
            client.send(JSON.stringify({
                  type: 'join',
                  message: "not-exists"
                }));
          }
          break;
        case 'list':
          client.send(JSON.stringify({type: 'list', message: Object.keys(servers)}));
          break;
        case 'create':
          if(!servers[message.name]) {
            var server = {
              name: message.name,
              clients: {},
              host: client
            };
            servers[message.name] = server;
            client.send(JSON.stringify({
                  type: 'create',
                  message: "ok"
                }));
          } else {
            client.send(JSON.stringify({
                  type: 'create',
                  message: "already-exists"
                }));
          }
          break;
        case 'broadcast':
          var msg = JSON.stringify({
              type: 'broadcast', 
              server: message.server, 
              message: message.message
            });
          var clients = servers[message.server].clients;
          for(var c in clients) {
            if(clients.hasOwnProperty(c)) {
              clients[c].send(msg);
            }
          }
          break;
        case 'reply':
          var toclient = servers[message.server].clients[message.client];
          toclient.send(JSON.stringify({
                type: 'reply', 
                server: message.server, 
                client: message.client,
                replyTo: message.replyTo,
                message: message.message
              }));
          break;
        case 'message':
          servers[message.server].host.send(JSON.stringify({
                type: 'message', 
                server: message.server, 
                client: client.id, 
                message: message.message
              }));
          break;
        case 'query':
          servers[message.server].host.send(JSON.stringify({
                type: 'query', 
                server: message.server, 
                client: client.id, 
                id: messageId,
                message: message.message
              }));
          break;
        }
      });
    client.on('disconnect', function() {
        for(var s in servers) {
          if(servers.hasOwnProperty(s)) {
            var server = servers[s];
            delete server.clients[client.id];
            server.host.send(JSON.stringify({type: 'disconnect', server: s, client: client.id}));
            if(server.host === client) {
              var clients = server.clients;
              for(var c in clients) {
                if(clients.hasOwnProperty(c)) {
                  clients[c].send(JSON.stringify({type: 'server-disconnect', server: s}));
                }
              }
              log("Exit " + server.name);
              delete servers[s];
            }
          }
        }
      });
  });

console.log('Server running at http://127.0.0.1:8888/');
repl.start('server> ');
