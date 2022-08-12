var main = {
    init: function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('.btn-delete').on('click', function (eventObject) {
            _this.delete(eventObject);
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

    },

    save: function () {
        var data = {
            keyword: $('#keyword').val(),
            userEmail: $('#author').val(),
            vacationMessage: $('#vacationMessage').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/keywords',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            if (response.success === true) {
                alert('Success');
                location.href = '/keywords';
            }
            else {
                alert(JSON.stringify(response));
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    delete : function (eventObject) {
        var id = eventObject.target.value;

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/keywords/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (response) {
            if (response.success === true) {alert('Delete success');}
            else {alert(JSON.stringify(response));}
            location.href = '/keywords';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        var data = {
            updateRequestType: 'MESSAGE_UPDATE',
            vacationMessage: $('#vacationMessage').val()
        };
        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/keywords/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            if (response.success === true) {
                alert('Update success');
                location.href = '/keywords';
            }
            else {
                alert(JSON.stringify(response));
            }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    toggleUpdate: function (eventObject) {
        var data = {

        }
        alert(eventObject);
        alert(eventObject.target.value);
    }
};
main.init();

function toggle_click(id, eventObject) {
    alert(id);
    alert(eventObject.value);
}