var ImageCache = {
  up_arrow: "./resources/images/",
  down_arrow: "./resources/images/",
  left_arrow: "./resources/images/",
  right_arrow: "./resources/images/",
  halt: "./resources/images/",
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
  $(".motion").each(function () {
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

  console.log(ImageCache[ccId] + ccId + alterName + ext + "?v=" + IMAGE_CACHE_VERSION);
  $("#" + elementId).attr("src", ImageCache[ccId] + ccId + alterName + ext + "?v=" + IMAGE_CACHE_VERSION);
}
