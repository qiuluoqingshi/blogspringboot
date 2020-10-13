function GetCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        //alert(j);
        if (document.cookie.substring(i, j) == arg) return getCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return null;
}
var isPostBack = "<%= IsPostBack %>";

function getCookieVal(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) endstr = document.cookie.length;
    return decodeURI(document.cookie.substring(offset, endstr));
}

function setCookie(name, value) {
    document.cookie = name + "="+ encodeURI (value);
}

function setCookie(name, value, expire) {
    var expire_time = new Date(expire)
    var now = new Date();
    var remaining_time = expire_time.getTime() - now.getTime()
    now.setTime(now.getTime() + remaining_time);
    document.cookie = name + "="+ encodeURI (value) + ";expires=" + now.toGMTString()+"; path=/";
}

function deleteCookie(name, value){
    var date=new Date();
    date.setTime(date.getTime()-10000);
    document.cookie=name+"="+ decodeURI(value)+"; expires="+date.toGMTString()+"; path=/";
}