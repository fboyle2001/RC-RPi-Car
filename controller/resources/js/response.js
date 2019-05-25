function handleIncoming(message) {
  console.log(message);

  var statusCode = message.status.code;

  if(statusCode == 410) {
    $("#result_shutdown").text("Success");
    $("#section_shutdown").show();
    return;
  }

  if(message.context.response == false) {
    return;
  }

  var type = message.context.requestType;
  var key = getKeyByValue(RequestType, type).toLowerCase();

  var output = message.status.message;

  if(type == RequestType.SENSOR_MEASURE_DISTANCE) {
    output = message.content.distance + " m";
  } else if (type == RequestType.TEST_CONNECTION) {
    output = message.content[Object.keys(message.content)[0]];
  }

  $("#result_" + key).text(output);
}

// https://stackoverflow.com/questions/9907419/how-to-get-a-key-in-a-javascript-object-by-its-value
function getKeyByValue(object, value) {
  return Object.keys(object).find(key => object[key] === value);
}
