$(document).ready(function () {
    console.log("Ready!")

    let errorCodes = {
        1: "Falscher Content-Type!",
        2: "ID Parameter fehlt!",
        3: "ID ist ungültig!",
        4: "Objekt zur ID nicht gefunden!",
        5: "Löschen nicht möglich, es gibt noch Abhängigkeiten!",
        6: "Erfolgreich gelöscht!"
    };

    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
        }
    });

    $('.navbar-burger').each(function (index, element) {
       $(this).on('click', function () {
           var target = $(this).data('target');
           $(element).toggleClass('is-active');
           $(target).toggleClass('is-active');
       })
    });

    // Delete button on advanced search
    $('.btn-delete').on('click', function (e) {
        e.preventDefault();
        const card = $(this).parents('.card');
        const id = $(this).parents('.card').data('id');
        const lang = $(this).parents('.card').data('lang');
        const type = $(this).parents('.card').data('type');
        swal({
            title: "Bist du dir sicher?",
            text: `${lang} mit der ID ${id} löschen?`,
            icon: "warning",
            buttons: ["Nein", "Ja"],
            dangerMode: true,
        })
        .then((willDelete) => {
            if (willDelete) {
                deleteObject(`/employee/${type}/${id}/`, function (errorCode) {
                    let msg = errorCodes[errorCode];
                    console.log(errorCodes);
                    console.log(msg);
                    if (errorCode === 6) {
                        card.remove();
                        swal(msg, {
                            icon: "success",
                        });
                    } else {
                        swal(msg, {
                            icon: "error",
                        });
                    }
                });
            } else {
                swal("Das Buch wurde nicht gelöscht!");
            }
        });
    });

    function deleteObject(url, fun) {
        let errorCode = -1;
        $.ajax(url, {
            contentType: "application/json",
            type: "DELETE",
            dataType: "json",
            success: function (result) {
                errorCode = result["code"];
                fun(errorCode);
            }
        });
    }

});