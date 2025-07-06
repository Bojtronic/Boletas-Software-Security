function setCookie(name, value, days) {
   let expire = "";
   if(days){
    let date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expire = "; expires=" + date.toUTCString();
   }
   document.cookie = name + "=" + (value || "") + expire + "; path=/";
}

function deleteCookie(name){
    document.cookie = name + "=; path=/";
}

function getCookie(name){
   var cookies = document.cookie.split(";");
   for (var i = 0; i < cookies.length; i++) {
       var cookie = cookies[i].trim();
       if (cookie.startsWith(name + "=")) {
           return cookie.substring(name.length + 1);
       }
   }
   return null;
}