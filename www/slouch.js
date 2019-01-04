var exec = require('cordova/exec');

function resolveJson(str, callback) {
    callback(JSON.parse(str));
}

exports.get = function(path) {
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'get', [path]);
    });
};

exports.put = function(path, data) { 
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'put', [path, data]);
    });
};

exports.post = function(path, data) {
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'post', [path, data]);
    });
};

exports.delete = function(path) {
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'delete', [path]);
    });
};

exports.table = function(path, data) {
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'table', [path, data]);
    });
};

exports.database = function(path) {
    return new Promise(function(res, rej){
        exec(r => resolveJson(r, res), rej, 'slouch', 'database', [path]);
    });
};