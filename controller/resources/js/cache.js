var ImageCache = {
  up_arrow: "./resources/images/",
  down_arrow: "./resources/images/",
  left_arrow: "./resources/images/",
  right_arrow: "./resources/images/",
  halt: "./resources/images/",
}

const IMAGE_CACHE_VERSION = "0.2.0";

$(document).ready(function () {
  $("img").each(function () {
    var id = $(this).attr("id");

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

    if($(this).attr("cc-ext") != undefined) {
      ext = $(this).attr("cc-ext");
    }

    $(this).attr("src", srcFolder + ccId + ext + "?v=" + IMAGE_CACHE_VERSION);
  });
});
