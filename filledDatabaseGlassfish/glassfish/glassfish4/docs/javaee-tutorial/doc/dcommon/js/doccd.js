var navBackground = "#A4C3DF"; var scrollBar = "auto";

/*
  Copyright 2006, 2012, Oracle and/or its affiliates. All rights reserved.
  Author: Robert Crews
  Version: 2011.3.1
*/

function addLoadEvent(func) {
  var oldOnload = window.onload;
  if (typeof(window.onload) != "function")
    window.onload = func;
  else
    window.onload = function() { oldOnload(); func(); }
}

function createCookie(name, value, days) {
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    var expires = "; expires=" + date.toGMTString();
  }
  else var expires = "";
  document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(";");
  for(var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0)==" ") c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return "";
}

function leftNav() {
  if (!document.createElement) return false;
  if (!document.createTextNode) return false;
  if (!document.getElementById) return false;
  if (!document.getElementsByTagName) return false;
  if (!document.documentElement.appendChild) return false;
  if (!document.documentElement.childNodes) return false;
  if (!document.documentElement.style) return false;

  if (/\bMobile\b/.test(navigator.userAgent)) return false;
  try {
    if (/\/pls\//.test(window.location.href)) return false;
  } catch(e) {}

  document.body.style.padding = "0";
  document.body.style.border = "0";

  var tahitiNav = document.createElement("div");
  tahitiNav.style.padding = "1ex 0.5em"

  var tahitiNavFrame = document.createElement("div");
  tahitiNavFrame.style.top = "0";
  tahitiNavFrame.style.bottom = "0";

  var showNav = document.createElement("div");
  if ((window.XMLHttpRequest == undefined) && (ActiveXObject != undefined)) {
  } else {
    showNav.title = "Show Navigation";
    showNav.style.background = "#A4C3DF";
    showNav.style.width = "0.25em";
    showNav.style.height = "84ex";
    showNav.style.top = "0";
    showNav.style.left = "0";
    showNav.style.position = "fixed";
    showNav.style.cursor = "pointer";
    showNav.style.display = "none";
  }

  var hideNav = document.createElement("div");
  hideNav.style.color = "#039";
  hideNav.style.textAlign = "right";
  hideNav.style.cursor = "pointer";
  hideNav.appendChild(document.createTextNode("Hide Navigation"));

  if ((window.XMLHttpRequest == undefined) && (ActiveXObject != undefined)) {
  // IE6
    document.documentElement.style.overflow = "hidden";
    document.body.style.margin = "0";
    document.body.style.overflow = "hidden";

    var tahitiBody = tahitiNav.cloneNode(false);

    tahitiNavFrame.style.position = "absolute";

    var tahitiBodyFrame = tahitiNavFrame.cloneNode(false);
    tahitiBodyFrame.style.overflow = "auto";

    tahitiNavFrame.style.left = "0";
    tahitiNavFrame.style.overflowX = "hidden";
    tahitiNavFrame.style.overflowY = scrollBar;
    tahitiNavFrame.style.background = navBackground;

    var content = document.getElementsByTagName("div");
    var div = [];
    for (var i = 0; i < content.length; i++)
      if ( content[i].className == "header" ||
        content[i].className == "IND" ||
        content[i].className == "footer" ) {
        div.push(content[i]);
      }
    for (var i = 0; i < div.length; i++) tahitiBody.appendChild(div[i]);

    hideNav.onclick = function() {
      navHiddenIe6();
      createCookie("ORA_TAHITI_NAV", "hide");
      window.onresize = function() {
        navHiddenIe6();
        createCookie("ORA_TAHITI_NAV", "hide");
      }
    }

    showNav.onclick = function() {
      navVisibleIe6();
      createCookie("ORA_TAHITI_NAV", "", -1);
      window.onresize = function() {
        navVisibleIe6();
        createCookie("ORA_TAHITI_NAV", "", -1);
      }
    }

    navVisibleIe6();
    window.onresize = function() { navVisibleIe6(); }

    tahitiNavFrame.appendChild(tahitiNav);
    tahitiBodyFrame.appendChild(tahitiBody);
    document.body.appendChild(tahitiNavFrame);
    document.body.appendChild(tahitiBodyFrame);

    function navVisibleIe6() {
      tahitiNavFrame.style.display = "block";
      tahitiNavFrame.style.width = "25%";
      tahitiBodyFrame.style.left = "25%";
      tahitiBodyFrame.style.width = "75%";
      tahitiNavFrame.style.width = document.documentElement.clientWidth -
        document.documentElement.clientWidth * 0.75;
      tahitiNavFrame.style.height = document.documentElement.clientHeight;
      tahitiBodyFrame.style.width = document.documentElement.clientWidth -
        document.documentElement.clientWidth * 0.25;
      tahitiBodyFrame.style.height = document.documentElement.clientHeight;
      tahitiNav.style.width = document.documentElement.clientWidth -
        document.documentElement.clientWidth * 0.75 - 32;
      tahitiBody.style.width = document.documentElement.clientWidth -
        document.documentElement.clientWidth * 0.25 - 32;
      showNav.style.display = "none";
    }
    function navHiddenIe6() {
      tahitiNavFrame.style.display = "none";
      tahitiBodyFrame.style.left = "0";
      tahitiBodyFrame.style.width = document.documentElement.clientWidth;
      tahitiBodyFrame.style.height = document.documentElement.clientHeight;
      tahitiBody.style.width = document.documentElement.clientWidth - 32;
      var content = document.getElementsByTagName("div");
      for (var i = 0; i < content.length; i++)
        if ( content[i].className == "header" ) {
          content[i].parentNode.insertBefore(showNav, content[i]);
          break;
        }
      showNav.style.display = "block";
    }
    if (readCookie("ORA_TAHITI_NAV") == "hide" ||
      window.name == "glossWindow") navHiddenIe6();
    var img = document.getElementsByTagName("img");
    for (var i = 0; i < img.length; i++) img[i].style.border = "0";
  } else {   // non IE6
    document.body.style.margin = "1ex 0.5em 1ex 26%";

    tahitiNavFrame.style.position = "fixed";
    tahitiNavFrame.style.left = "0";
    tahitiNavFrame.style.width = "25%";
    tahitiNavFrame.style.overflowX = "hidden";
    tahitiNavFrame.style.overflowY = scrollBar;
    tahitiNavFrame.style.background = navBackground;

    tahitiNavFrame.appendChild(tahitiNav);
    document.body.appendChild(tahitiNavFrame);

    hideNav.onclick = function() { navHidden(); }
    showNav.onclick = function() { navVisible(); }
    document.body.appendChild(showNav);

    function navVisible() {
      tahitiNavFrame.style.display = "block";
      document.body.style.marginLeft = "26%";
      showNav.style.display = "none";
      createCookie("ORA_TAHITI_NAV", "", -1);
    }
    function navHidden() {
      tahitiNavFrame.style.display = "none";
      document.body.style.marginLeft = "0.5em";
      showNav.style.display = "block";
      if (window.name != "glossWindow") createCookie("ORA_TAHITI_NAV", "hide");
    }
    if (readCookie("ORA_TAHITI_NAV") == "hide" ||
      window.name == "glossWindow") navHidden();
  }
  tahitiNav.appendChild(hideNav);

  function thisDocument() {
    var p = document.createElement("p"),
      a = document.createElement("a"),
      metas = document.getElementsByTagName("meta"),
      title = "Contents",
      m = 0;

    for (m = 0; m < metas.length; m++) {
      if (metas[m].name == "doctitle" && metas[m].content) {
        title = metas[m].content;
      }
    }

    a.href = "toc.htm#BEGIN";
    a.appendChild(document.createTextNode(title));
    p.appendChild(a);

    return p;
  }
  tahitiNav.appendChild(thisDocument());

  function thisPage() {
    var autoId = 0;
    var heading = document.createElement("p");
    heading.style.marginBottom = "0";
    heading.style.fontWeight = "bold";
    heading.style.borderBottom = "1px solid #999";
    heading.appendChild(document.createTextNode("This Page"));
    heading.style.cursor = "pointer";
    heading.onclick = function() {
      if (document.getElementById("tThisPage").style.display == "none")
        document.getElementById("tThisPage").style.display = "block";
      else
        document.getElementById("tThisPage").style.display = "none";
      };

    var listStack = [];
    var listItem;

    var headings = [];
    getHeadings(document.documentElement);

    for (var i = 0; i < headings.length; i++) {
      var lastItem = listItem;

      var a = document.createElement("a");
      a.href = "#" + headings[i].id;
      listItem = document.createElement("li");
      copyContent(headings[i], a);
      listItem.appendChild(a);

      if (!lastItem) {
        var ul = document.createElement("ul");
        ul.style.margin = "0";
        ul.style.padding = "0 0 0 1.5em";
        ul.style.textIndent = "-0.25em";
        listStack.push(ul);
      }

      else if (headings[i].nodeName > headings[i-1].nodeName) {
        var d = headingDifference(headings[i], headings[i-1]);

        if (d > 1) {
          for (var j = 0; j < d; j++) {
            if (j) {
              var newList = document.createElement("ul");
              newList.style.margin = "0";
              newList.style.padding = "0 0 0 1.5em";
              newList.style.textIndent = "-0.25em";
              listStack[listStack.length-1].appendChild(newList);
              listStack.push(newList);
            } else {
              var newList = document.createElement("ul");
              newList.style.margin = "0";
              newList.style.padding = "0 0 0 1.5em";
              newList.style.textIndent = "-0.25em";
              lastItem.appendChild(newList);
              listStack.push(newList);
            }
          }
        } else {
          var newList = document.createElement("ul");
          newList.style.margin = "0";
          newList.style.padding = "0 0 0 1.5em";
          newList.style.textIndent = "-0.25em";
          lastItem.appendChild(newList);
          listStack.push(newList);
        }
      }

      else if (headings[i].nodeName < headings[i-1].nodeName) {
        var d = headingDifference(headings[i], headings[i-1]);
        for (var j = 0; j < d; j++) {
          listStack.pop();
        }
      }

      listStack[listStack.length-1].appendChild(listItem);
    }

    var thisPage = document.createElement("div");

    if (!listStack[0]) {
      var glossterms = [];
      var p = document.getElementsByTagName("p");
      for (var i = 0; i < p.length; i++) {
        if (p[i].className == "glossterm") {
          var gloss_a = p[i].getElementsByTagName("a");
          for (var j = 0; j < gloss_a.length; j++) {
            if (gloss_a[j].id != null) {
              var ga = document.createElement("a");
              ga.href = "#" + gloss_a[j].id;
              ga.appendChild(document.createTextNode(getTextContent(p[i])));
              var g_li = document.createElement("li");
              g_li.appendChild(ga);
              glossterms.push(g_li);
              break;
            }
          }
        }
      }
      if (glossterms.length > 0) {
        var gList = document.createElement("ul");
        gList.style.margin = "0";
        gList.style.padding = "0 0 0 1.5em";
        gList.style.textIndent = "-0.25em";
        for (var i = 0; i < glossterms.length; i++) {
          gList.appendChild(glossterms[i]);
        }
        listStack.push(gList);
      }
    }

    if (listStack[0]) {
      listStack[0].id = "tThisPage";
      thisPage.appendChild(heading);
      thisPage.appendChild(listStack[0]);
    }

    var h1 = document.getElementsByTagName("h1");
    try {
      if (/\/toc.htm(?:|#.*)$/.test(window.location.href) &&
        (h1 != null && h1[0].className == "toc"))
        return document.createElement("span");
    } catch(e) {}
    return thisPage;

    function getHeadings(e) {
      var c = e.childNodes;
      for (var i = 0; i < c.length; i++) {
        if (c[i].nodeType != 1) continue;
        if (c[i].nodeName.match(/h[1-4]/i)) {
          if (!c[i].id) c[i].id = "autoId" + autoId++;
          headings.push(c[i]);
        } else if (c[i].hasChildNodes()) getHeadings(c[i]);
      }
    }

    function headingDifference(e1, e2) {
        var f1 = new Number(e1.nodeName.charAt(1));
        var f2 = new Number(e2.nodeName.charAt(1));
        return Math.abs(f1 - f2);
    }

    function getTextContent(e) {
      if (e.textContent) return e.textContent;
      if (e.innerText) return e.innerText;
    }

    function copyContent(s, d) {
      if (!s.hasChildNodes()) return null;
      if (s.childNodes.length == 1 && s.childNodes[0].nodeName.match(/a/i))
        return copyContent(s.childNodes[0], d);
      for (var i = 0; i < s.childNodes.length; i++) {
        if (s.childNodes[i].className == "secnum") continue;
        d.appendChild(s.childNodes[i].cloneNode(true));
      }
    }

  }
  tahitiNav.appendChild(thisPage());

}
addLoadEvent(leftNav);

function processToc() {
  try {
    if (!/\/toc.htm(?:|#.*)$/.test(window.location.href)) return false;
  } catch(e) {}
  var h1 = document.getElementsByTagName("h1");
  if (h1[0] == null || h1[0].className != "toc") return false;

  var iUrlStart = "../../nav/";

  try {
    if (/http:\/\/(?:download|docs)\.oracle\.com\//.test(
      window.location.href)) {
      iUrlStart = "http://docs.oracle.com/cd/E23003_01/";
    }
  } catch(e) {
      iUrlStart = "../../nav/";
  }

  var preload = new Image();
  preload.src = iUrlStart + "images/blue_med_dark_down.gif"
  preload.alt = "Click to collapse";

  var IND;
  var div = document.getElementsByTagName("div");
  for (var i = 0; i < div.length; i++) {
    if (div[i].className == "IND") IND = div[i];
  }

  for (var i = 0; i < IND.childNodes.length; i++) {
    if (IND.childNodes[i].nodeName.match(/^ul$/i)) {
      IND.childNodes[i].style.marginTop = "0";
      IND.childNodes[i].style.marginBottom = "1ex";
      IND.childNodes[i].style.display = "none";
    } else if (IND.childNodes[i].nodeName.match(/^h2$/i)) {
      if (IND.childNodes[i].className == "tocheader") {
        IND.childNodes[i].style.fontSize = "140%";
        IND.childNodes[i].style.marginTop = "0";
        IND.childNodes[i].style.marginBottom = "0.5ex";
        if (/Part\s+[IVXLCDM]+\s+/.test(getTextContent(IND.childNodes[i]))) {
          IND.childNodes[i].style.textAlign = "center";
          IND.childNodes[i].style.marginTop = "1.7ex";
          IND.childNodes[i].style.borderTop = "2px solid #A4C3DF";
        } else {
          var img = document.createElement("img");
          img.src = iUrlStart + "images/blue_med_dark_side.gif";
          img.alt = "Click to expand";
          var a = document.createElement("a");
          a.href = "#";
          a.onclick = function() { showNextUl(this); }
          a.onfocus = function() { window.status = ''; return true; }
          a.className = "tDiscTriangle";
          a.style.textDecoration = "none";
          a.appendChild(img);
          if (okShowTriangle(IND.childNodes[i])) {
            IND.childNodes[i].insertBefore(document.createTextNode(" "),
              IND.childNodes[i].firstChild);
            IND.childNodes[i].insertBefore(a, IND.childNodes[i].firstChild);
          }
        }
      }
    }
  }

  var p = document.createElement("p");
  p.style.marginTop = "0";
  p.style.marginBottom = "0";
  p.style.textAlign = "right";
  var a1 = document.createElement("a");
  a1.href = "#";
  a1.onfocus = function() { window.status = ''; return true; }
  a1.style.color = "#039";
  a1.style.textDecoration = "none";
  a1.onclick = expandAll;
  var a2 = document.createElement("a");
  a2.href = "#";
  a2.onfocus = function() { window.status = ''; return true; }
  a2.style.color = "#039";
  a2.style.textDecoration = "none";
  a2.onclick = collapseAll;
  a1.appendChild(document.createTextNode("Expand All"));
  a2.appendChild(document.createTextNode("Collapse All"));
  p.appendChild(a1);
  p.appendChild(document.createTextNode(" \u00B7 "));
  p.appendChild(a2);
  document.getElementsByTagName("h1")[0].style.marginBottom = "0";
  document.getElementsByTagName("h1")[0].parentNode.insertBefore(p,
    document.getElementsByTagName("h1")[0].nextSibling);

  if (readCookie("ORA_TAHITI_TOC") == "expand") expandAll();

  function expandAll() {
    var a = IND.getElementsByTagName("a");
    for (var i = 0; i < a.length; i++) {
      if (a[i].className != "tDiscTriangle") continue;
      showNextUl(a[i]);
    }
    createCookie("ORA_TAHITI_TOC", "expand");
  }

  function collapseAll() {
    var a = IND.getElementsByTagName("a");
    for (var i = 0; i < a.length; i++) {
      if (a[i].className != "tDiscTriangle") continue;
      hideNextUl(a[i]);
    }
    createCookie("ORA_TAHITI_TOC", "", -1);
  }

  function okShowTriangle(node) {
    while (node.nextSibling != null) {
      if (node.nextSibling.nodeName.match(/h2/i)) return false;
      if (node.nextSibling.nodeName.match(/ul/i)) return true;
      node = node.nextSibling;
    }
    return false;
  }

  function showNextUl(owner) {
    var ul = nextUl(owner.parentNode);
    ul.style.display = "block";
    owner.getElementsByTagName("img")[0].src =
      iUrlStart + "images/blue_med_dark_down.gif";
    owner.getElementsByTagName("img")[0].alt = "Click to collapse";
    owner.onclick = function() { hideNextUl(this); }
  }

  function hideNextUl(owner) {
    var ul = nextUl(owner.parentNode);
    ul.style.display = "none";
    owner.getElementsByTagName("img")[0].src =
      iUrlStart + "images/blue_med_dark_side.gif";
    owner.getElementsByTagName("img")[0].alt = "Click to expand";
    owner.onclick = function() { showNextUl(this); }
  }

  function nextUl(node) {
    while (node.nextSibling != null ) {
      if (node.nextSibling.nodeName.match(/ul/i)) return node.nextSibling;
      node = node.nextSibling;
    }
  }

  function getTextContent(e) {
    if (e.textContent) return e.textContent;
    if (e.innerText) return e.innerText;
  }

}
addLoadEvent(processToc);

function positionAnchor() {
  if (!(typeof(ActiveXObject) != "undefined" || window.opera)) return false;
  var anchor = "", location = [];
  try {
    if (/#(.*)$/.test(window.location.href)) anchor = RegExp.$1;
  } catch(e) {
    return;
  }

  if (document.getElementById(anchor)) {
    location = findPos(document.getElementById(anchor));
    window.scrollTo(0, location[1]);
  } else {
    var a = document.getElementsByTagName("a");
    for (var i = 0; i < anchor.length; i++) {
      if (a[i].name == anchor) {
        location = findPos(a[i]);
        window.scrollTo(0, location[1]);
        continue;
      }
    }
  }

  function findPos(obj) {
    var curleft = curtop = 0;
    if (obj.offsetParent)
      do {
        curleft += obj.offsetLeft;
        curtop += obj.offsetTop;
      } while (obj = obj.offsetParent);
    return [curleft, curtop];
  }
}
addLoadEvent(positionAnchor);

function processIndex() {
  try {
    if (!/\/index.htm(?:|#.*)$/.test(window.location.href)) return false;
  } catch(e) {}

  var shortcut = [];
  lastPrefix = "";

  var dd = document.getElementsByTagName("dd");
  for (var i = 0; i < dd.length; i++) {
    if (dd[i].className != 'l1ix') continue;
    var prefix = getTextContent(dd[i]).substring(0, 2).toUpperCase();
    if (!prefix.match(/^([A-Z0-9]{2})/)) continue;
    if (prefix == lastPrefix) continue;
    dd[i].id = prefix;
    var s = document.createElement("a");
    s.href = "#" + prefix;
    s.appendChild(document.createTextNode(prefix));
    shortcut.push(s);
    lastPrefix = prefix;
  }

  var h2 = document.getElementsByTagName("h2");
  for (var i = 0; i < h2.length; i++) {
    var nav = document.createElement("div");
    nav.style.position = "relative";
    nav.style.top = "-1.5ex";
    nav.style.left = "1.5em";
    nav.style.width = "90%";
    while (shortcut[0] &&
      shortcut[0].toString().charAt(shortcut[0].toString().length - 2) ==
      getTextContent(h2[i])) {
      nav.appendChild(shortcut.shift());
      nav.appendChild(document.createTextNode("\u00A0 "));
    }
    h2[i].parentNode.insertBefore(nav, h2[i].nextSibling);
  }

  function getTextContent(e) {
    if (e.textContent) return e.textContent;
    if (e.innerText) return e.innerText;
  }
}
addLoadEvent(processIndex);

function compactLists() {
  var lists = [];
  var ul = document.getElementsByTagName("ul");
  for (var i = 0; i < ul.length; i++) lists.push(ul[i]);
  var ol = document.getElementsByTagName("ol");
  for (var i = 0; i < ol.length; i++) lists.push(ol[i]);

  for (var i = 0; i < lists.length; i++) {
    var collapsible = true, c = [];
    var li = lists[i].getElementsByTagName("li");
    for (var j = 0; j < li.length; j++) {
      var p = li[j].getElementsByTagName("p");
      if (p.length > 1) collapsible = false;
      for (var k = 0; k < p.length; k++) {
        if ( getTextContent(p[k]).split(" ").length > 12 ) collapsible = false;
        c.push(p[k]);
      }
    }
    if (collapsible) {
      for (var j = 0; j < c.length; j++) {
        c[j].style.margin = "0";
      }
    }
  }

  function getTextContent(e) {
    if (e.textContent) return e.textContent;
    if (e.innerText) return e.innerText;
  }
}
addLoadEvent(compactLists);
