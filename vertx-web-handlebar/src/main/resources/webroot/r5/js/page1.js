//Load common code that includes config, then load the app logic for this page.
requirejs(['./common'], function (common) {
    requirejs(['app/module'], function(module) {

        require(["text!some/module.html", "text!some/module.css"],
            function( html, css) {
                //module()
                module.append(html);
                //the html variable will be the text
                //of the some/module.html file
                //the css variable will be the text
                //of the some/module.css file.
            }
        );
    });

});
