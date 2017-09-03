/**
 * @author Robin Duda
 *
 * Used to pass application-level events between components.
 */
var MISSING = "MISSING";
var CONFLICT = "CONFLICT";
var ERROR = "ERROR";
var ACCEPTED = "ACCEPTED";

var application = {
    handlers: [],
    token: null,
    authenticated: false,

    api: function() {
        return location.origin + ":8080";
    },

    onAuthenticated: function (token) {
        this.authenticated = true;
        this.token = token;
        this.publish('authenticated', token);
    },

    onLogout: function () {
        this.authenticated = false;
        this.token = null;
        this.publish('logout');
    },

    subscribe: function (event, callback) {
        if (this.handlers[event] == null)
            this.handlers[event] = [];

        this.handlers[event].push(callback);
    },

    publish: function (event, data) {
        if (this.handlers[event])
            for (var subscriber = 0; subscriber < this.handlers[event].length; subscriber++)
                this.handlers[event][subscriber](data);
    }
};