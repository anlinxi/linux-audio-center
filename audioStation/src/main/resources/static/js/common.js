/**
 * 公用js文件
 */

//根路径
const ROOT_RUL = localStorage.ROOT_RUL ? localStorage.ROOT_RUL : '';

/**
 * 建单的ajax封装
 * @param params
 */
function ajax(params) {
    let xhr = new XMLHttpRequest();
    if (params.type) {
        xhr.open(params.type.toUpperCase(), params.url);
    } else {
        xhr.open("POST", params.url);
    }

    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('__token', localStorage.token);
    xhr.setRequestHeader('__userId', localStorage.username);
    xhr.onload = function () {
        if (xhr.status == 200) {
            const result = JSON.parse(xhr.responseText);
            console.log('%c [信息]ajax请求返回:', 'color:rgba(77,194,43,0.81)', result, params.url, xhr);
            if (!result.code) {
                params.success(result, result, xhr);
            } else if (result.code == 200) {
                params.success(result.result, result, xhr);
            } else if (result.code == 401) {
                location.href = '/login.html';
            } else {
                params.error(result.message, result, xhr);
            }

        } else if (xhr.status == 401) {
            layer.msg("鉴权失败，请重新登录！")
            setTimeout(function () {
                location.href = '/login.html';
            }, 3000)
        } else {
            params.error(xhr.responseText, xhr.status, xhr);
        }
    }
    xhr.send(JSON.stringify(params.data));
}

/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 * 例子：
 * (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 * @param fmt
 * @returns {*}
 * @constructor
 * @author: meizz
 */
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;

};

/**
 * 首字母小写，转换为属性名
 */
function fristLowerCase(text) {
    var reText = text;
    if (text.charAt(0).toUpperCase() == text.charAt(0)) {
        reText = text.charAt(0).toLowerCase() + text.substring(1, text.length);
    } else {
        reText = "_" + text;
    }
    return reText;
}

/**
 * 二次封装的请求
 * @param {Object} url
 * @param {Object} data
 * @param {Object} method
 */
function post(url, data) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", ROOT_RUL + url);
        xhr.setRequestHeader('Accept', 'application/json');
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.setRequestHeader('__token', localStorage.token);
        xhr.setRequestHeader('__userId', localStorage.username);
        xhr.onload = function () {
            if (xhr.status == 200) {
                const result = JSON.parse(xhr.responseText);
                console.log('%c [信息]ajax请求返回:', 'color:rgba(77,194,43,0.81)', result, url, xhr);
                if (!result.code) {
                    resolve(result, result, xhr);
                } else if (result.code == 200) {
                    resolve(result, result, xhr);
                } else if (result.code == 401) {
                    location.href = '/login.html';
                } else {
                    reject(result.message, result, xhr);
                }

            } else if (xhr.status == 401) {
                setTimeout(function () {
                    location.href = '/login.html';
                }, 3000)
                layer.msg("鉴权失败，请重新登录！")
            } else {
                reject(xhr.responseText, xhr.status, xhr);
            }
        }
        xhr.send(JSON.stringify(data));
    })
}

/**
 * 获取url的传参
 * @returns {{}}
 */
function getParams(parent) {
    const urlParams = {};
    let query = null;
    if (parent) {
        query = parent.location.search.substring(1);
    } else {
        query = window.location.search.substring(1);
    }
    const vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        const pair = vars[i].split("=");
        if (pair.length >= 2 && pair[0] && pair[1]) {
            urlParams[pair[0]] = pair[1]
        }
    }
    return urlParams;
}