/**
 * websoket聊天对象
 *
 * @type {{}}
 */
const ichat = {
    ws: null,
    linkState: false,
    //重连时间
    reLinkTime: 5 * 1000,
    link: function () {
        let ROOT_RUL = localStorage.ROOT_RUL ? localStorage.ROOT_RUL : '';
        if (!ROOT_RUL) {
            ROOT_RUL = window.location.host;
        }
        if (ROOT_RUL.substr(0, 'https://'.length) == 'https://') {
            ROOT_RUL = ROOT_RUL.substr('https://'.length, ROOT_RUL.length);
        }
        if (ROOT_RUL.substr(0, 'http://'.length) == 'http://') {
            ROOT_RUL = ROOT_RUL.substr('http://'.length, ROOT_RUL.length);
        }
        ichat.ws = new WebSocket('ws://' + ROOT_RUL + '/websocket/' + localStorage.token)
        ichat.ws.onopen = ichat.onopen;
        ichat.ws.onmessage = ichat.onmessage;
        ichat.ws.onclose = ichat.onclose;
        ichat.ws.onerror = ichat.onerror;
        ichat.ws.onopen = ichat.onopen;
    },
    /**
     * 打开链接
     */
    onopen: function () {
        ichat.ws.send(JSON.stringify({msg: '用户' + JSON.parse(localStorage.user).userName + '已登录'}));
        ichat.linkState = true;
    },
    /**
     * 返回消息信息
     * @param event
     */
    onmessage: function (event) {
        if (typeof event.data === String) {
            console.log("Received data string");
        } else if (event.data instanceof ArrayBuffer) {
            var buffer = event.data;
            console.log("Received arraybuffer");
        }
    },
    /**
     * 关闭
     */
    onclose: function () {
        ichat.linkState = false;
    },
    /**
     * 错误
     */
    onerror: function () {
        setTimeout(function () {
            ichat.link();
            ichat.reLinkTime = ichat.reLinkTime + 1000;
        }, ichat.reLinkTime)
    }
}


//开始链接
ichat.link();