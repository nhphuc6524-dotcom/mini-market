function logout(){
    localStorage.removeItem("user")
    window.location.href="/login.html"
}

function checkLogin(){
    if(!localStorage.getItem("user")){
        window.location.href="/login.html"
    }
}