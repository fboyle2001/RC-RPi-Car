var socket;

// https://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
function syntaxHighlight(json) {
  if (typeof json != 'string') {
    json = JSON.stringify(json, undefined, 2);
  }
  json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
    var cls = 'number';
    if (/^"/.test(match)) {
      if (/:$/.test(match)) {
        cls = 'key';
      } else {
        cls = 'string';
      }
    } else if (/true|false/.test(match)) {
      cls = 'boolean';
    } else if (/null/.test(match)) {
      cls = 'null';
    }

    return '<span class="' + cls + '">' + match + '</span>';
  });
}

function onMessage(response) {
  console.log(response);
  $("#last_response").html(syntaxHighlight(response));

  if(response.status.code == 410) {
    $("#connected").text("Server shutdown.");
    $("button").attr("disabled", "disabled");
  }
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

  $("#motion_forward").click(function (e) {
    e.preventDefault();
    socket.sendRequest(1, {speed: 20});
  });

  $("#motion_reverse").click(function (e) {
    e.preventDefault();
    socket.sendRequest(2, {speed: 20});
  });

  $("#motion_right").click(function (e) {
    e.preventDefault();
    socket.sendRequest(3, {speed: 20});
  });

  $("#motion_left").click(function (e) {
    e.preventDefault();
    socket.sendRequest(4, {speed: 20});
  });

  $("#motion_halt").click(function (e) {
    e.preventDefault();
    socket.sendRequest(5, {speed: 0});
  });

  $("#sensor_distance").click(function (e) {
    e.preventDefault();
    socket.sendRequest(6, {});
  });

  $("#test_connection").click(function (e) {
    e.preventDefault();
    socket.sendRequest(7, {date: new Date().toLocaleString()});
  });

  $("#shutdown").click(function (e) {
    e.preventDefault();
    socket.sendRequest(8, {});
  });

  $("#auto_move_start").click(function (e) {
    e.preventDefault();
    socket.sendRequest(9, {});
  });

  $("#auto_move_stop").click(function (e) {
    e.preventDefault();
    socket.sendRequest(10, {});
  });
});
