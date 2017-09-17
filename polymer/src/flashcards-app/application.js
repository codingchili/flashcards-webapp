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
    colors: [
        'var(--paper-red-a700)',
        'var(--paper-pink-a700)',
        'var(--paper-purple-a700)',
        'var(--paper-deep-purple-a700)',
        'var(--paper-indigo-a700)',
        'var(--paper-blue-a700)',
        'var(--paper-light-blue-a700)',
        'var(--paper-cyan-a700)',
        'var(--paper-teal-a700)',
        'var(--paper-green-a700)',
        'var(--paper-light-green-a700)',
        'var(--paper-lime-a700)',
        'var(--paper-yellow-a700)',
        'var(--paper-amber-a700)',
        'var(--paper-orange-a700)',
        'var(--paper-deep-orange-a700)',
        'var(--paper-brown-500)',
        'var(--paper-grey-500)',
        'var(--paper-blue-grey-500)'
    ],

    api: function () {
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
            for (let subscriber = 0; subscriber < this.handlers[event].length; subscriber++)
                this.handlers[event][subscriber](data);
    }
};

// token can be saved in the settings view.
application.token = JSON.parse(localStorage.getItem('token'));
application.authenticated = (application.token !== null);