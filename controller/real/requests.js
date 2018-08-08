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
    $("#status").text("Server shutdown.");
    socket.live = false;
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
  $("#status").text("Connected");
  $("#initial").hide();
}

var lastMove = 5;
var currentMode = 0;

function getSpeed() {
  return $("#speed_set").val();
}

$(document).ready(function () {
  $("#menu_options > li").click(function (e) {
    var mode = $(this).data("mode");

    if(currentMode == mode) {
      return;
    }

    if(mode == 1) {
      return;
    }

    $("#menu_options").find("[data-mode='" + currentMode + "']").attr("class", "");
    $("#menu_options").find("[data-mode='" + mode + "']").attr("class", "current");

    console.log(mode);

    currentMode = mode;

    if(currentMode == 0) {
      $("#auto_controls").hide();
      $("#raw_controls").show();
    } else if(currentMode == 2) {
      $("#auto_controls").show();
      $("#raw_controls").hide();
    }
  });

  $("#connect").click(function(e) {
    e.preventDefault();
    socket = SocketConnection(onMessage, onError, onConnectionFailure);
    socket.init($("#hostname").val(), $("#port").val());
    waitForAcknowledgement(0, onComplete);
  });

  $("#motion_forward").click(function (e) {
    e.preventDefault();
    lastMove = 1;
    socket.sendRequest(1, {speed: getSpeed()});
  });

  $("#motion_reverse").click(function (e) {
    e.preventDefault();
    lastMove = 2;
    socket.sendRequest(2, {speed: getSpeed()});
  });

  $("#motion_right").click(function (e) {
    e.preventDefault();
    lastMove = 3;
    socket.sendRequest(3, {speed: getSpeed()});
  });

  $("#motion_left").click(function (e) {
    e.preventDefault();
    lastMove = 4;
    socket.sendRequest(4, {speed: getSpeed()});
  });

  $("#motion_halt").click(function (e) {
    e.preventDefault();
    lastMove = 5;
    socket.sendRequest(5);
  });

  $("#speed_set").change(function (e) {
    socket.sendRequest(lastMove, {speed: getSpeed()});
  });

  $("#sensor_distance").click(function (e) {
    e.preventDefault();
    socket.sendRequest(6);
  });

  $("#test_connection").click(function (e) {
    e.preventDefault();
    socket.sendRequest(7, {date: new Date().toLocaleString()});
  });

  $("#shutdown").click(function (e) {
    e.preventDefault();
    socket.sendRequest(8);
  });

  $("#auto_move_start").click(function (e) {
    e.preventDefault();
    socket.sendRequest(9);
  });

  $("#auto_move_stop").click(function (e) {
    e.preventDefault();
    socket.sendRequest(10);
  });

  $("#override_halt").click(function (e) {
    e.preventDefault();
    socket.sendRequest(11);
  });

  $("#led_red_on").click(function (e) {
    e.preventDefault();
    socket.sendRequest(12, {outputNumber: 0});
  });

  $("#led_red_off").click(function (e) {
    e.preventDefault();
    socket.sendRequest(13, {outputNumber: 0});
  });

  $("#led_green_on").click(function (e) {
    e.preventDefault();
    socket.sendRequest(12, {outputNumber: 1});
  });

  $("#led_green_off").click(function (e) {
    e.preventDefault();
    socket.sendRequest(13, {outputNumber: 1});
  });
});
