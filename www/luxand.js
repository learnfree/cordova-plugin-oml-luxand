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
        if(data.status==="SUCCESS") {
            return success({
                status: "SUCCESS",
                message: data.message
            });
        }else {
            return error({
                status: "FAIL",
                message: data.message
            });
        }
    }, error, 'Luxand', 'register', []);
};
exports.login = function (success, error) {
    exec((data)=>{
        if(data.status==="SUCCESS") {
            return success({
                status: "SUCCESS",
                message: data.message
            });
        }else {
            return error({
                status: "FAIL",
                message: data.message
            });
        }
    }, error, 'Luxand', 'login', []);
};
