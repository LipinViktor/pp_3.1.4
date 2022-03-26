$(function() {
    const url = "http://localhost:8080/api/users";
    let tableList = document.getElementById("tBody");
    let editModal = document.getElementById("editModal");
    let deleteModal = document.getElementById("deleteModal");
    function getRolesFromSelect(elementsSelect) {
        let roleSelect = [];
        for (var i = 0; i < elementsSelect.options.length; i++) {
            if (elementsSelect.options[i].selected) {
                roleSelect.push(elementsSelect.options[i].value);
            }
        };
        return roleSelect;
    }
    function newTable() {
        fetch(url)
            .then(resp => resp.json())
            .then(data => {
                let out = "";
                data.forEach(post => {
                    let arrayRoles = [];
                    post.roles.forEach(role => {
                        arrayRoles.push(role.name.substr(5));
                    });
                    out += `
                        <tr data-id="${post.id}">
                            <td>${post.id}</td>
                            <td>${post.name}</td>
                            <td>${post.surName}</td>
                            <td>${post.age}</td>
                            <td>${arrayRoles}</td>
                            <td>
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal" id="butEdit"
                                        data-bs-target="#editModal">
                                Edit
                                </button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-bs-toggle="modal" id="butDelete"
                                        data-bs-target="#deleteModal">
                                Delete
                                </button>
                            </td>
                        </tr>`;
                });
                tableList.innerHTML = out;
                out = "";
            });
    }
    newTable();
    tableList.addEventListener("click", (e) => {
        e.preventDefault();
        let editButPres = e.target.id == "butEdit";
        let deleteButPres = e.target.id == "butDelete";
        let id = e.target.parentElement.parentElement.dataset.id;
        if(deleteButPres) {
            fetch(`${url}/${id}`)
                .then(resp => resp.json())
                .then(data => {
                    $("#idD").attr("value", data.id)
                    $("#nameD").attr("value", data.name);
                    $("#surnameD").attr("value", data.surName);
                    $("#ageD").attr("value", data.age);
                    $("#passwordD").attr("value", data.password);
                    let arrRolesD = [];
                    data.roles.forEach(function(e) {
                        arrRolesD.push(e.name);
                    })
                    $('#rolesD').attr("value", arrRolesD)
                })
        }
        deleteModal.addEventListener("submit", (e) => {
            e.preventDefault();
            fetch(`${url}/${id}`, {
                method: "delete"
            })
                .then(setTimeout(function () {
                    newTable();
                }, 700))
                $("#deleteModal").modal("hide");
        });
        if(editButPres) {
            fetch(`${url}/${id}`)
                .then(resp => resp.json())
                .then(data => {
                    $("#idE").attr("value", data.id);
                    $("#nameE").attr("value", data.name);
                    $("#surnameE").attr("value", data.surName);
                    $("#ageE").attr("value", data.age);
                    $("#passwordE").attr("value", data.password);
                })
        }
        editModal.addEventListener("submit", (e) => {
            e.preventDefault();
            let rolesList = getRolesFromSelect(document.getElementById("rolesE"));
            fetch(url, {
                method: "PUT",
                headers: {
                    "Content-type": "application/json; charset=utf-8"
                },
                body: JSON.stringify({
                    id: id,
                    name: document.getElementById("nameE").value,
                    surName: document.getElementById("surnameE").value,
                    age: document.getElementById("ageE").value,
                    password: document.getElementById("passwordE").value,
                    roles: rolesList
                })
            })
                .then(setTimeout(function () {
                    newTable();
                }, 700))
                $("#editModal").modal("hide");
        })
    });

    const adminPanel = document.getElementById("formPost");
    const nameUser = document.getElementById("nameValue");
    const surNameUser = document.getElementById("surnameValue");
    const ageUser = document.getElementById("ageValue");
    const passwordUser = document.getElementById("passwordValue");
    let rolesUser = document.getElementById("roleValue");
    adminPanel.addEventListener("submit", (e) => {
        e.preventDefault();
        let rolesSelectPost = getRolesFromSelect(rolesUser);
        fetch(url, {
            method: "POST",
            headers: {
                "Content-type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({
                name: nameUser.value,
                surName: surNameUser.value,
                age: ageUser.value,
                password: passwordUser.value,
                roles: rolesSelectPost
            })
        })
            setTimeout(function () {
                newTable();
            }, 700)
        $("#nav-home-tab").tab("show")
    })
})
