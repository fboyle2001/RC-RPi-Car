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
  LED_OFF: 13
};

var motionLastDirection = RequestType.MOTION_HALT;

function getSpeed() {
  return $("#raw_speed").val();
}

$(document).ready(function () {

  $(".motion").click(function () {
    motionLastDirection = RequestType["MOTION_" + $(this).data("direction")];
    window.socket.sendRequest(motionLastDirection, {speed: 10});
    resetClass("motion");
    alterCachedSource($(this).attr("id"), "_clicked");
  });

  $("#raw_speed").change(function (e) {
    var speed = getSpeed();

    if(motionLastDirection == RequestType.MOTION_HALT) {
      return;
    }

    socket.sendRequest(motionLastDirection, {speed: getSpeed()});
  });

});
