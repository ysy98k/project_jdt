/**
 * Created by xutingting on 2017/6/5.
 */
$(document).ready(function (){
    var paramJsonObj = new Object();
    var callback = {
        onSuccess : function(paramJsonObj) {
            if(!paramJsonObj){
                alert("无法获取许可证！");
                return;
            }else{
                if(paramJsonObj.deployMode=="product"){
                    $("#product").show();
                    $("#companyName").html(paramJsonObj.AuthorizingCompany);
                    $("#softWareName").html(paramJsonObj.AuthorizingSoftware);
                    $("#productExpiringDate").html(paramJsonObj.ExpiringDate);
                    $("#hostMac").html(paramJsonObj.HostMAC);
                    $("#productionEdition").html(paramJsonObj.ProductionEdition);
                    $("#version").html(paramJsonObj.version);
                    $("#buildDate").html(paramJsonObj.buildDate);
                    $("#baseLine").html(paramJsonObj.baseLine);
                    $("#deliveryPkg").html(paramJsonObj.deliveryPkg);
                }else if(paramJsonObj.deployMode=="cloud_xinsight" || paramJsonObj.deployMode=="cloud_xinsight_aas"){
                    $("#cloud").show();
                    $("#licenseInfo").html(paramJsonObj.licenseInfo);
                    $("#cloudExpiringDate").html(paramJsonObj.ExpiringDate);
                }else{
                    $("#cloud").show();
                    $("#licenseInfo").html("deployMode参数有误，许可证获取失败！");
                    $("#cloudExpiringDate").hide();
                }
            }
        }
    };
    AjaxCommunicator.ajaxRequest(
        '/df/license/license.do?method=getLicense', 'POST',
        paramJsonObj, callback);
    }
);
