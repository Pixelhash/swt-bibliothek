$(document).ready(function () {
    console.log("Ready!")

    $('.navbar-burger').each(function (index, element) {
       $(this).on('click', function () {
           var target = $(this).data('target');
           $(element).toggleClass('is-active');
           $(target).toggleClass('is-active');
       })
    });

});