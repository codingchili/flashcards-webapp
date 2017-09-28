/**
 * @author Robin Duda
 *
 * Used to pass application-level events between components.
 */
var MISSING = "MISSING";
var CONFLICT = "CONFLICT";
var ERROR = "ERROR";
var ACCEPTED = "ACCEPTED";

var api = {
    request: function (target, route) {
        let request = {'target': target, 'route': route};
        if (application.token !== null) {
            request.token = application.token;
        }
        return request;
    }
}

var application = {
    handlers: [],
    token: null,
    authenticated: false,

    api: function () {
        return location.origin + ":8180";
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
            for (let subscriber = 0; subscriber < this.handlers[event].length; subscriber++)
                this.handlers[event][subscriber](data);
    }
};

// token can be saved in the settings view.
application.token = JSON.parse(localStorage.getItem('token'));
application.authenticated = (application.token !== null);