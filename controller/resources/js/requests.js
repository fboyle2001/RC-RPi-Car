var RequestType = {
  MOTION_FORWARD: 1,
  MOTION_REVERSE: 2,
  MOTION_RIGHT: 3,
  MOTION_LEFT: 4,
  MOTION_HALT: 5,
  SENSOR_MEASURE_DISTANCE: 6,
  TEST_CONNECTION: 7,
  SHUTDOWN: 8,
  AUTO_MOVE_START: 9,
  AUTO_MOVE_STOP: 10,
  OVERRIDE_HALT: 11,
  LED_ON: 12,
  LED_OFF: 13,
  ASSISTED_LEFT: 14,
  ASSISTED_RIGHT: 15
};

var motionLastDirection = RequestType.MOTION_HALT;

function getRawSpeed() {
  return $("#raw_speed").val();
}

$(document).ready(function () {
  $("img").on("dragstart", function(e) {
    e.preventDefault();
  });

  $(".motion").click(function () {
    motionLastDirection = RequestType[$(this).data("action").toUpperCase()];
    window.socket.sendRequest(motionLastDirection, {speed: getRawSpeed()});
    resetClass("motion");
    alterCachedSource($(this).attr("id"), "_clicked");
  });

  $(".assisted").click(function () {
    window.socket.sendRequest(RequestType[$(this).data("action").toUpperCase()], {speed: 70});
    resetClass("assisted");
    alterCachedSource($(this).attr("id"), "_clicked");
  });

  $(".auto").click(function () {
    window.socket.sendRequest(RequestType[$(this).data("action").toUpperCase()]);
    resetClass("auto");
    alterCachedSource($(this).attr("id"), "_clicked");
  });

  $("#raw_speed").change(function (e) {
    var speed = getRawSpeed();

    if(motionLastDirection == RequestType.MOTION_HALT) {
      return;
    }

    socket.sendRequest(motionLastDirection, {speed: speed});
  });

  $('[id^="action_"]').click(function () {
    var actionName = $(this).attr("id").substr(7).toUpperCase();

    if(actionName == "TEST_CONNECTION") {
      socket.sendRequest(RequestType[actionName], {date: new Date().toLocaleString()})
    } else {
      socket.sendRequest(RequestType[actionName]);
    }
  });

});
