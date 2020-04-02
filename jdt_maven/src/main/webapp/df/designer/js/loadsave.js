var LoadSaveService = function(serviceurl) {
    var $this = this;
    this.serviceURL = "/df/designer/loadsave.do";
    if (serviceurl) {
        this.serviceURL = serviceurl;
    }
    this.loadDesignData = function(pagename, usercallback, errorcallback) {
        var data = {};
        data.pagename = pagename;
        var callback = {
            onSuccess: function(paramJsonObj) {
                if (paramJsonObj.errcode == 0) {
                    console.log(paramJsonObj);
                    if (usercallback) {//执行usercallback回调函数
                        usercallback(paramJsonObj);
                    }
                } else if (paramJsonObj.errcode == 1) {//说明该页面版本过低，需要升级
                    var upgradeResult = $this.upgradePageVersion(pagename);
                    if(upgradeResult.status == 0){//升级成功
                        if (usercallback) {//执行usercallback回调函数
                            paramJsonObj.htmlstring = upgradeResult.htmlstring;//获取最新的html代码
                            paramJsonObj.jsstring = upgradeResult.jsstring;
                            paramJsonObj.cssstring = upgradeResult.cssstring;
                            usercallback(paramJsonObj);
                        }
                    }else{
                        alertDiv("提示", "自动升级出错，请联系运维人员手工升级页面！")
                    }
                } else {
                    console.log(paramJsonObj);
                    if (errorcallback) {
                        data.data = paramJsonObj;
                        errorcallback(data);
                    }
                }
            },
            onError: function(xmlR, status, e) {
                if (errorcallback) {
                    data.xmlRequest = xmlR;
                    data.status = status;
                    data.data = e;
                    errorcallback(data);
                }
            }
        };

        AjaxCommunicator.ajaxRequest(this.serviceURL + '?method=load', 'GET', data, callback);
    };

    this.upgradePageVersion = function(pageName) {
        var $waitDiv = $("body");
        var upgradeRes = {};
        $waitDiv.block({
            message: '页面升级中，请等待...',
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .5,
                color: '#fff'
            }
        });
        var callback = {
            onSuccess: function (paramJsonObj) {
                $waitDiv.unblock();
                upgradeRes =  paramJsonObj;
            }
        };
        var paramJsonObj = {};
        paramJsonObj.pageName = pageName;
        AjaxCommunicator.ajaxRequest(this.serviceURL + '?method=upgrade', 'POST', paramJsonObj, callback);
        return upgradeRes;
    };

    this.saveasDesignData = function(pageobj, htmlstring, jsstring, cssstring, usercallback, errorcallback) {
        var data = {};
        data.pagename = pageobj.pageEname;
        data.page_cname = pageobj.pageCname;
        data.page_desc = pageobj.pageDesc;
        data.htmlstring = htmlstring;
        data.jsstring = jsstring;
        data.cssstring = cssstring;

        var callback = {
            onSuccess: function(paramJsonObj) {
                console.log(paramJsonObj);
                if (paramJsonObj.errcode == 0) {
                    if (usercallback) {
                        usercallback(paramJsonObj);
                    }
                } else {
                    if (errorcallback) {
                        data.data = paramJsonObj;
                        errorcallback(data);
                    }
                }
            },
            onError: function(xmlR, status, e) {
                if (errorcallback) {
                    data.xmlRequest = xmlR;
                    data.status = status;
                    data.data = e;
                    errorcallback(data);
                }
            }
        };

        AjaxCommunicator.ajaxRequest(this.serviceURL + '?method=saveas', 'POST', data, callback);
    };
    this.saveDesignData = function(pagename, htmlstring, jsstring, cssstring, usercallback, errorcallback) {
        var data = {};
        data.pagename = pagename;
        data.htmlstring = htmlstring;
        data.jsstring = jsstring;
        data.cssstring = cssstring;

        var callback = {
            onSuccess: function(paramJsonObj) {
                console.log(paramJsonObj);
                if (paramJsonObj.errcode == 0) {
                    if (usercallback) {
                        usercallback(paramJsonObj);
                    }
                } else {
                    if (errorcallback) {
                        data.data = paramJsonObj;
                        errorcallback(data);
                    }
                }
            },
            onError: function(xmlR, status, e) {
                if (errorcallback) {
                    data.xmlRequest = xmlR;
                    data.status = status;
                    data.data = e;
                    errorcallback(data);
                }
            }
        };

        var url = this.serviceURL + '?method=save';
        console.log('url:' + url);
        console.log('data:' + data);
        AjaxCommunicator.ajaxRequest(url, 'POST', data, callback);
    };
    this.publishDesignData = function(pagename) {
        window.open(this.serviceURL + '?method=publish&pagename=' + pagename);
    };
};