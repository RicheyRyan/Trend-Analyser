window.Trend = Backbone.Model.extend({

    urlRoot: "/trends",

    idAttribute: "_id",

    initialize: function () {
    defaults: {
        name: "",
    }
});

window.TrendCollection = Backbone.Collection.extend({

    model: Trend,

    url: "/trends"

});