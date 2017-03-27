//The build will inline common dependencies into this file.

//For any third party dependencies, like jQuery, place them in the lib folder.

//Configure loading modules from the lib directory,
//except for 'app' ones, which are in a sibling
//directory.
requirejs.config({
    baseUrl: 'js/lib',
    paths: {
        app: '../app',
        text: '//cdn.bootcss.com/require-text/2.0.12/text.min',
        hbs: 'hbs',
        handlebars: '//cdn.bootcss.com/handlebars.js/4.0.6/handlebars.min',
        underscore: '//cdn.bootcss.com/underscore.js/1.8.3/underscore-min',
        json2: '//cdn.bootcss.com/json2/20160511/json2'
    },
    shim : {
        'hbs': ['handlebars', 'json2', 'underscore'],
        'json2': {
            exports: 'JSON'
        }
    },
    hbs: { // optional
        helpers: true,            // default: true
        templateExtension: 'html', // default: 'hbs'
        partialsUrl: 'app/tpl/partials',           // default: '',
        helperPathCallback:           // Callback to determine the path to look for helpers
            function (name) {           // ('/templates/helpers/'+name by default)
                return 'app/tpl/helper/' + name;
            },
        handlebarsPath : 'handlebars'
    }
});
