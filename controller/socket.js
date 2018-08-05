var socket = new WebSocket("ws://10.0.0.17:5612/");

var getMessage = function (data) {
  return JSON.parse(data.data);
};

var requester = {
  authKey: "",
  sendRequest: function(type, data) {
    var req = JSON.stringify({
      authKey: this.authKey,
      request: {
        type: type,
        data: data
      }
    });
    socket.send(req);
  },
  sendBadRequest: function(type, data) {
    var req = JSON.stringify({
      authKeyName: this.authKey,
      requestA: {
        type: type,
        data: data
      }
    });
    socket.send(req);
  },
}

socket.onmessage = function (data) {
  var message = getMessage(data);

  console.log("Message received:");
  console.log(message);

  if(message.status.code == 201) {
    requester.authKey = message.content.authKey;
  }
}
