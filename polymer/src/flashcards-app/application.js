/**
 * @author Robin Duda
 *
 * Used to pass application-level events between components.
 */
const MISSING = 'MISSING';
const CONFLICT = 'CONFLICT';
const UNAUTHORIZED = 'UNAUTHORIZED';
const ERROR = 'ERROR';
const ACCEPTED = 'ACCEPTED';
const BAD = 'BAD';
const TOAST_ERROR = 'toast_error';
const TOAST = 'toast';
const LOADING = 'loading';

const api = {
    request: function (target, route, json) {
        if (!json) {
            json = {};
        }
        json.target = target;
        json.route = route;
        if (application.token != null) {
            json.token = application.token;
        }
        return json;
    }
};

const application = {
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