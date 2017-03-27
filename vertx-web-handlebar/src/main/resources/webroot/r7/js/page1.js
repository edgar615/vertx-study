//Load common code that includes config, then load the app logic for this page.
requirejs(['./common'], function (common) {
    require(['hbs!app/tpl/one'], function (tmplOne) {
        // Use whatever you would to render the template function
        document.body.innerHTML = tmplOne({
            adjective: "favorite",
            threeFourths: 3 / 4,
            listofstuff: ['bananas', 'democracy', 'expired milk']
        });
    });
});
