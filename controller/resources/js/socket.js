var SocketConnection = function (onMessage, onError) {
  return {
    socket: "",
    authKey: "",
    live: false,
    sendRequest: function(type, data = {}, self = this) {
      if(this.live == false) {
        return;
      }

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
      this.live = true;
    }
  };
};

window.socket = SocketConnection(handleIncoming, handleError);

function handleError() {
  $("#auth_message").text("Server is busy or not online");
}
