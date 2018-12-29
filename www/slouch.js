var exec = require('cordova/exec');

exports.get = function(path) {
    return new Promise(function(res, rej){
        exec(res, rej, 'slouch', 'get', [path]);
    });
};

exports.put = function(path, data) { 
    return new Promise(function(res, rej){
        exec(res, rej, 'slouch', 'put', [path, data]);
    });
};

exports.post = function(path, data) {
    return new Promise(function(res, rej){
        exec(res, rej, 'slouch', 'post', [path, data]);
    });
};

exports.delete = function(path) {
    return new Promise(function(res, rej){
        exec(res, rej, 'slouch', 'delete', [path]);
    });
};
