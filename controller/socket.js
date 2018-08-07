var SocketConnection = function (onMessage, onError, onConnectionFailure) {
  return {
    socket: "",
    authKey: "",
    sendRequest: function(type, data = {}, self = this) {
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
            return false;
          }

          if(status == 201) {
            self.authKey = response.content.authKey;
            return true;
          }

          console.log("Unexpected code " + status);
          return false;
        }

        onMessage(response);
      },
      this.socket.onerror = onError
    }
  };
};
