$(document).ready(function() {
    const URL = "http://localhost:8080/api/users";
    const idUser = String(document.getElementById("777"));
    fetch("http://localhost:8080/api/users/1")
        .then(resp => resp.json())
        .then(data => {
            document.getElementById("showId").innerText = data.id
            document.getElementById("showName").innerText = data.name;
            document.getElementById("showSurName").innerText = data.surName;
            document.getElementById("showAge").innerText = data.age;
            let arrListRoles = [];
            data.roles.forEach(e => {
                arrListRoles.push(e.name);
            })
            document.getElementById("showRoles").innerText = arrListRoles;
        })
})
