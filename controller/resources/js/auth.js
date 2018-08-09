var AuthHandlers = {
  handleFailure: function () {
    $("#auth_message").text("Server is busy or not online");
    $("#auth_connect").attr("disabled", "disabled");
    $("#auth_host").attr("readonly", "readonly");
    $("#auth_port").attr("readonly", "readonly");
    console.log("Failed to connect");
  },
  handleSuccess: function () {
    $("#auth_message").text("Connected");
    $("#auth_connect").attr("disabled", "disabled");
    $("#auth_host").attr("readonly", "readonly");
    $("#auth_port").attr("readonly", "readonly");
    console.log("Connected");

    $("#section_auth").hide();
    $("#section_menu").show();
    $("#section_main").show();
    $("#section_buttons").show();
  }
}

$(document).ready(function () {
  $("#auth_connect").click(function (e) {
    e.preventDefault();
    $("#auth_message").text("");

    var host = $("#auth_host").val();
    var port = $("#auth_port").val();

    if(host == undefined || host == "") {
      $("#auth_message").text("Please define a host");
      return;
    }

    if(port == undefined || port == "") {
      $("#auth_message").text("Please define a port");
      return;
    }

    window.socket.init(host, port);
    waitForAcknowledgement(0, AuthHandlers.handleSuccess, AuthHandlers.handleFailure);
  });
});

function waitForAcknowledgement(count, success, failure) {
  if(count >= 12) {
    failure();
    return false;
  }

  if(window.socket.authKey != "") {
    success();
    return true;
  } else if ($("#auth_message").text() != "") {
    failure();
    return false;
  } else {
    setTimeout(function () {
      waitForAcknowledgement(count + 1, success, failure)
    }, 250);
  }
}
