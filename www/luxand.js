var exec = require('cordova/exec');

exports.init = function (config, success, error) {
    let licence = config.licence;
    let dbname = config.dbname;
    let tryCount = config.loginTryCount;
    console.log(config);
    exec(success, error, 'Luxand', 'init', [licence, dbname, tryCount]);
};
exports.identify = function (success, error) {
    exec((data)=>{
        console.log("data:"+JSON.stringify(data));
        delete data.error;
        //data.extra = JSON.parse(data.extra || "{}");
        if(data.status==="SUCCESS") {
            return success(data);
        }else {
            return error(data);
        }
    }, error, 'Luxand', 'register', []);
};
exports.login = function (success, error) {
    exec((data)=>{
        delete data.error;
        //data.extra = JSON.parse(data.extra || "{}");
        if(data.status==="SUCCESS") {
            return success(data);
        }else {
            return error(data);
        }
    }, error, 'Luxand', 'login', []);
};
