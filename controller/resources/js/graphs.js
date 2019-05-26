$(function () {
  setupGraphs();
});

var maximumLength = 40;
var dataPoints = {
  aX: [],
  aY: [],
  aZ: [],
  gX: [],
  gY: [],
  gZ: []
};

function processDataPoints(readings) {
  Object.keys(readings).forEach(function (key) {
    if(dataPoints[key].length == maximumLength) {
      dataPoints[key].shift();
    }

    dataPoints[key].push(readings[key]);
  });

  updateGraphs();
}

function updateGraphs() {
  
}

function setupGraphs() {

}
