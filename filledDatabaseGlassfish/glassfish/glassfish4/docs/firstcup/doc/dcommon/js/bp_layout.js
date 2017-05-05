/*
  Copyright 2007, 2011, Oracle and/or its affiliates. All rights reserved.
  Author: Robert Crews
  Version: 2011.8.12
*/
 
function init() {
  stripeLi();
  stripeTr();
}

function stripeLi() {
  if (document.getElementsByTagName) {
    var divs = document.getElementsByTagName("div");
    for (var i = 0; i < divs.length; i++) {
      if (divs[i].className == "portlet") {
        var uls = divs[i].getElementsByTagName("ul");
        if (uls.length > 0) {
          var lis = uls[uls.length - 1].getElementsByTagName("li");
          for (var j = 0; j < lis.length; j++) {
            if (j % 2 == 0) {
              lis[j].style.background = "white";
            }
          }
        }
      }
    }
  }
}

function stripeTr() {
  if (document.getElementsByTagName) {
    var tbodys = document.getElementsByTagName("tbody");
    for (var i = 0; i < tbodys.length; i++) {
      var trs = tbodys[i].getElementsByTagName("tr");
      for (var j = 0; j < trs.length; j++) {
        if (j % 2 == 0) {
          trs[j].style.background = "white";
        }
      }
    }
  }
}

function advancedSearch(mode) {
  if (document.getElementById) {
    if (mode == 'show') {
      document.getElementById("BASIC").style.display = "none";
      document.getElementById("ADVANCED").style.display = "block";
    } else {
      document.getElementById("BASIC").style.display = "block";
      document.getElementById("ADVANCED").style.display = "none";
    }
  }
}
