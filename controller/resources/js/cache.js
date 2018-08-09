var ImageCache = {
  up_arrow: "./resources/images/raw/",
  down_arrow: "./resources/images/raw/",
  left_arrow: "./resources/images/raw/",
  right_arrow: "./resources/images/raw/",
  motion_halt: "./resources/images/raw/",
  auto_start: "./resources/images/auto/",
  auto_stop: "./resources/images/auto/",
  auto_halt: "./resources/images/auto/"
}

var IMAGE_CACHE_VERSION = $('script[src*="cache.js"]').data("v");

$(document).ready(function () {
  $("img").each(function () {
    setSource($(this));
  });
});

function setSource(element) {
  var id = element.attr("id");

  if(!id.startsWith("cc")) {
    return;
  }

  var ccId = id.substr(3);
  var srcFolder = ImageCache[ccId];

  if(srcFolder == undefined) {
    console.error("Unable to find source folder for " + ccId);
    return;
  }

  var ext = ".png";

  if(element.attr("cc-ext") != undefined) {
    ext = element.attr("cc-ext");
  }

  element.attr("src", srcFolder + ccId + ext + "?v=" + IMAGE_CACHE_VERSION);
}

function resetClass(clazz) {
  $("." + clazz).each(function () {
    setSource($(this));
  });
}

function alterCachedSource(elementId, alterName) {
  if($("#" + elementId) == undefined) {
    return;
  }

  if($("#" + elementId).get(0).tagName == "img") {
    return
  }

  if(!elementId.startsWith("cc_")) {
    return;
  }

  var ccId = elementId.substr(3);
  var srcFolder = ImageCache[ccId];

  var ext = ".png";

  if($(this).attr("cc-ext") != undefined) {
    ext = $(this).attr("cc-ext");
  }

  $("#" + elementId).attr("src", ImageCache[ccId] + ccId + alterName + ext + "?v=" + IMAGE_CACHE_VERSION);
}
