//The build will inline common dependencies into this file.

//For any third party dependencies, like jQuery, place them in the lib folder.

//Configure loading modules from the lib directory,
//except for 'app' ones, which are in a sibling
//directory.
requirejs.config({
    baseUrl: 'js/lib',
    paths: {
        app: '../app',
        style: '../../style'
    },
    map: {
        '*': {
            'css': 'css' // or whatever the path to require-css is
        }
    },
    //map: {
    //    '*': {
    //        'style': '../../style'
    //    }
    //},
    shim : {
        //'app': ['css!../page1.css']
    }
});
