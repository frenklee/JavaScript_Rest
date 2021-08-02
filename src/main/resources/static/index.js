
$(async function () {
    await getTableWithUsers();
    getNewUserForm();
    getDefaultModal();
    addNewUser();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json;utf-8',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('http://localhost:8080/api'),
    findAllRoles: async () => await fetch('http://localhost:8080/api/roles'),
    findOneUser: async (id) => await fetch(`http://localhost:8080/api/${id}`),
    addNewUser: async (user) => await fetch('api/', {method: 'POST',  headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`http://localhost:8080/api/${id}`, {method: 'PATCH', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`api/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.age}</td>
                            <td>${user.name}</td>
                            <td>${user.weight}</td>
                            <td>
                               
                                ${user.authorities.map(function (role){
                                    return `<p style="display: inline;">${role.name.substr(5) + " "}</p>`
                }) }

                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-info" 
                                data-toggle="modal" data-target="#someDefaultModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#someDefaultModal">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })

    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#mainTableWithUsers").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}
async function getNewUserForm() {
    let button = $(`#SliderNewUserForm`);
    let form = $(`#defaultSomeForm`)
    form.show();
}


// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let roless = await userFetchService.findAllRoles();

    let user = preuser.json();
    let roles = roless.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  type="button" class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button form="editUser" type="submit" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`

    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        roles.then(roles=>{
            let bodyForm = `
            <form class="form-group" id="editUser">
            <label for="idA" class="form-label ">ID</label>
                <input type="text" class="form-control" id="idA" name="id" value="${user.id}" disabled><br>
                <label for="nameA" class="form-label ">Name</label>
                <input type="text" class="form-control" id="nameA" name="name" value="${user.name}"><br>
                <label for="ageA" class="form-label ">Age</label>
                <input type="text" class="form-control" id="ageA" name="age" value="${user.age}"><br>
                <label for="weightA" class="form-label ">Weight</label> 
                <input type="text" class="form-control" id="weightA" name="weight" value="${user.weight}"><br>
                <label for="passwordA" class="form-label ">Password</label>
                <input class="form-control" type="text" id="passwordA" value="${user.password}"><br>        
                <label>Roles
                <select multiple size=${roles.length} name="roles"
                 class="form-control" id="rolesA" style="text-align:center;">
                 ${roles.map(function (role){
                return `<option value="${role.id}">${role.name}</option>`})}
                </select>
                <br/>
                </label>
            </form>
        `;
            modal.find('.modal-body').append(bodyForm);
        })
    })
    $("#editButton").on('click', async () => {
        let id = modal.find("#idA").val().trim();
        let name = modal.find("#nameA").val().trim();
        let age = modal.find("#ageA").val().trim();
        let weight = modal.find("#weightA").val().trim();
        let password = modal.find("#passwordA").val().trim();
        let rolesA = modal.find("#rolesA").val();
        let data = {
            id: id,
            name: name,
            age: age,
            weight: weight,
            password: password,
            roles: [
                {
                    id: 0,
                    name: String(rolesA),
                    users: null
                }
            ]
        }

        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            await getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    await userFetchService.deleteUser(id);
    getTableWithUsers();
    modal.find('.modal-title').html('');
    modal.find('.modal-body').html('User was deleted');
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
}

async function addNewUser() {
    $('#AddNewUserButton').click(async () =>  {
        let addUserForm = $('#defaultSomeForm')

        let name = addUserForm.find("#AddNewUserName").val().trim();
        let age = addUserForm.find("#AddNewUserAge").val().trim();
        let weight = addUserForm.find("#AddNewUserWeight").val().trim();
        let password = addUserForm.find("#AddNewUserPassword").val().trim();
        let rolesA = addUserForm.find("#AddNewUserRole").val();
        let data = {
            name: name,
            age: age,
            weight: weight,
            password: password,
            roles: [
                {
                    id: 0,
                    name: String(rolesA),
                    users: null
                }
            ]
        }
        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            await getTableWithUsers();
            addUserForm.find('#AddNewUserName').val('');
            addUserForm.find('#AddNewUserAge').val('');
            addUserForm.find('#AddNewUserWeight').val('');
            addUserForm.find('#AddNewUserPassword').val('');
            addUserForm.find('#AddNewUserRole').val('');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger    alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}
