define(function (require) {
    var $ = require('jquery'),
        lib = require('./lib');

    //A fabricated API to show interaction of
    //common and specific pieces.
    return {
        append: function (html) {
            $('body').append(html);
        }
    };
});
