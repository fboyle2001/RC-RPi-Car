var socket;

function onMessage(response) {

}

function onError() {

}

function onConnectionFailure() {
  $("#connect_err").text("Failed to connect. Host is either busy or the hostname/port is incorrect.");
}

function waitForAcknowledgement(count, callback) {
  if(count >= 12) {
    return false;
  }

  if(socket.authKey != "") {
    callback();
    return true;
  } else {
    setTimeout(function () {
      waitForAcknowledgement(count + 1, callback)
    }, 250);
  }
}

function onComplete() {
  $("#initial").hide();
  $("#controls").show();
}

$(document).ready(function () {
  $("#connect").click(function(e) {
    e.preventDefault();
    socket = SocketConnection(onMessage, onError, onConnectionFailure);
    socket.init($("#hostname").val(), $("#port").val());
    waitForAcknowledgement(0, onComplete);
  });
});
