var currentMode = "0";

$(document).ready(function () {

  $("#section_menu > nav > ul > li").click(function () {
    var mode = $(this).data("mode");

    if(mode == currentMode) {
      return;
    }

    if(mode == 1) {
      //Assisted not yet used
      return;
    }

    $("#section_menu > nav > ul").find("[data-mode='" + currentMode + "']").attr("class", "");
    $("#section_menu > nav > ul").find("[data-mode='" + mode + "']").attr("class", "current");

    currentMode = mode;

    console.log(currentMode);

    if(currentMode == 0) {
      $("#sub_raw_controls").show();
      $("#sub_auto_controls").hide();
    } else if (currentMode == 2) {
      console.log("here");
      $("#sub_raw_controls").hide();
      $("#sub_auto_controls").show();
    }
  });

});
