define(['handlebars'], function ( Handlebars ) {
    function roundNumber ( context, options ) {
        // Simple function for example
        return Math.round( context );
    }
    Handlebars.registerHelper( 'roundNumber', roundNumber );
    return roundNumber;
});