var main = {
    init: function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
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
        }).done(function () {
            alert('Success');
            location.href = '/keywords';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();