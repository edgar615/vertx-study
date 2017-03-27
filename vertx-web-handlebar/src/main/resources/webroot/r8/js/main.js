//Load common code that includes config, then load the app logic for this page.
requirejs(['./common'], function (common) {
    require(['jquery', 'pjax'], function ($) {
        console.log($.pjax);
        $(function() {
            $(document).on('pjax:clicked', function() {
                alert("pjax:clicked");
            });

            $(document).pjax('a', '#pjax-container')
            //$(document).on('click', 'a[data-pjax]', function(event) {
            //    event.preventDefault();
            //    alert(1);
            //    //var container = $(this).closest('[data-pjax-container]')
            //    var container = $('#pjax-container');
            //    $.pjax.click(event, {container: container})
            //});
        });
    });
});
