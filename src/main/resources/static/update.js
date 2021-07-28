const buttonUpdate = $("#btnUpdate");
buttonUpdate.click(
    function (event) {
        event.preventDefault();
        let select = document.getElementById('rolesSelect');
        let form = document.getElementById('up_user');
        let formData = new FormData(form);

        let roleSet = [];
        let options = select && select.options;
        let opt;

        for (let i = 0, iLen = options.length; i < iLen; i++) {
            opt = options[i];

            if (opt.selected) {
                roleSet.push({"id": opt.value, "role": opt.text})
            }
        }

        let response = fetch('http://localhost:8080/api', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json;utf-8'
            },
            body: JSON.stringify({
                id: formData.get('id'),
                name: formData.get('name'),
                age: formData.get('age'),
                weight: formData.get('weight'),
                password: formData.get('password'),
                roles: roleSet
            })
        }) .then(() => location.reload());
        $('#editModal').modal('hide');
        $('.modal-backdrop').remove();
    }
)