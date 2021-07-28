$(document).delegate('.eBtn_', 'click', function (event) {
    event.preventDefault();

    let href = $(this).attr('href');
    $.get(href, function (user, status) {
        $('.user_update #id').val(user.id);
        $('.user_update #first_name').val(user.name);
        $('.user_update #last_name').val(user.age);
        $('.user_update #age').val(user.weight);
        $('.user_update #password').val(user.password);
        $('.user_update #roles').val(user.roles);
    });
    $('.user_update').modal();
});

$(document).delegate('.dBtn_', 'click' ,function (event) {
    event.preventDefault();

    let href = $(this).attr('href');

    $.get(href, function (user, status) {
        $('.user_delete #id1').val(user.id);
        $('.user_delete #first_name1').val(user.name);
        $('.user_delete #age1').val(user.age);
        $('.user_delete #email1').val(user.weight);
        $('.user_delete #password1').val(user.password);
        $('.user_delete #roles').val(user.roles);
    });
    $('.user_delete').modal();
});