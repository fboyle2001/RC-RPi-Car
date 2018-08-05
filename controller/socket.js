var SocketConnection = function (onMessage, onError, onConnectionFailure) {
  return {
    socket: "",
    authKey: "",
    sendRequest: function(type, data, self = this) {
      var req = JSON.stringify({
        authKey: this.authKey,
        request: {
          type: type,
          data: data
        }
      });

      self.socket.send(req);
    },
    init: function (address, port, self = this) {
      this.socket = new WebSocket("ws://" + address + ":" + port + "/"),
      this.socket.onmessage = function (data) {
        var response = JSON.parse(data.data);

        if(self.authKey == "") {
          var status = response.status.code;

          if(status == 503) {
            onConnectionFailure();
            return;
          }

          if(status == 201) {
            self.authKey = response.content.authKey;
            return;
          }

          console.log("Unexpected code " + status);
          return;
        }

        onMessage(response);
      },
      this.socket.onerror = onError
    }
  };
};

function onMessage(response) {
  console.log(response);
}

function onError() {
  console.log("E");
}

function onConnectionFailure() {
  console.log("Either down or connected already");
}

var socket = SocketConnection(onMessage, onError, onConnectionFailure);
socket.init("10.0.0.14", 5612);
