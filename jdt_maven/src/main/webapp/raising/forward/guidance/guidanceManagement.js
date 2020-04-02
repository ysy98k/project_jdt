var info = new Vue({
    el:"#father",
    data:{
        ringNumber:"",
        incisionHorizontalDeviation:"",
        articulationHorizontalDeviation:"",
        shieldTailHorizontalDeviation:"",

        incisionVerticalDeviation:"",
        articulationVerticalDeviation:"",
        shieldVerticalDeviation:"",

        rollingAngle:"",
        pitchAngle:"",
        mileage:"",
        drivingLength:"",

        horizontalTrend:"",
        veticalTrend:""

    },
    computed: {
        // 计算属性的 getter
        incisionHorizontalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.incisionHorizontalDeviation//Math.abs(this.incisionHorizontalDeviation)
        },
        articulationHorizontalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.articulationHorizontalDeviation//Math.abs(this.articulationHorizontalDeviation)
        },
        shieldTailHorizontalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.shieldTailHorizontalDeviation//Math.abs(this.shieldTailHorizontalDeviation)
        },
        horizontalTrendAbsolute: function () {
            // `this` 指向 info 实例
            return this.horizontalTrend//Math.abs(this.horizontalTrend)
        },
        incisionVerticalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.incisionVerticalDeviation//Math.abs(this.incisionVerticalDeviation)
        },
        articulationVerticalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.articulationVerticalDeviation//Math.abs(this.articulationVerticalDeviation)
        },
        shieldVerticalDeviationAbsolute: function () {
            // `this` 指向 info 实例
            return this.shieldVerticalDeviation//Math.abs(this.shieldVerticalDeviation)
        },
        veticalTrendAbsolute: function () {
            // `this` 指向 info 实例
            return this.veticalTrend//Math.abs(this.veticalTrend)
        }
    }
})


$(document).ready(function () {
    initHeight();

    getData();
    drawBaseLine();
    drawDeviation();
    drawHorizontalDeviation();
    drawVertical();

})

function getData(){
    $.ajax({
        "url":toolkitPath+"/raising/forward/guidance/guidanceManagement/getGuidanceData.do?ajaxParam="+$.cookie("selected_id"),
        "type":"get",
        "async":false,
        "success":function(data){
            if(data !=null &&   data.status == "0"){
                info.ringNumber = data.newData.ringNumber;
                $("#progress").css("width",data.progress+"%");
                $("#progressBackGround").attr("title","当前进度为"+data.progress+"%");
                //水平偏差
                info.incisionHorizontalDeviation = data.newData.incisionHorizontalDeviation;
                info.articulationHorizontalDeviation = data.newData.articulationHorizontalDeviation;
                info.shieldTailHorizontalDeviation = data.newData.shieldTailHorizontalDeviation;
                //垂直偏差
                info.incisionVerticalDeviation = data.newData.incisionVerticalDeviation;
                info.articulationVerticalDeviation = data.newData.articulationVerticalDeviation;
                info.shieldVerticalDeviation = data.newData.shieldVerticalDeviation;
                //趋势
                info.horizontalTrend = data.newData.horizontalTrend;
                info.veticalTrend = data.newData.veticalTrend;

                info.rollingAngle = data.newData.rollingAngle;
                info.pitchAngle = data.newData.pitchAngle;
                info.mileage = data.newData.mileage;
                info.drivingLength = data.newData.drivingLength;
            }
            var x = data.xAxis;
            var d1 = data.incisionHorizontalDeviationsList;
            var d2 = data.incisionVerticalDeviationsList;
            var d3 = data.horizontalTrendsList;
            var d4 = data.veticalTrendsList;
            drawCharts(x,d1,d2,d3,d4);

        }
    })
}

function drawCharts(xAxis,incisionHorizontalDeviationsList,incisionVerticalDeviationsList,horizontalTrendsList,veticalTrendsList){
    initCharts(xAxis,incisionHorizontalDeviationsList,incisionVerticalDeviationsList,"myCharts1","切口姿态变化曲线");
    initCharts(xAxis,horizontalTrendsList,veticalTrendsList,"myCharts2","前盾趋势变化曲线");
}

function initCharts(xAxis,yHorizontalAxis,yVerticalAxis,idName,titleName){
    var myChart = echarts.init(document.getElementById(idName));
    var subtext = '偏差mm';
    if(idName == "myCharts2"){
        subtext = "趋势mm/m";
    }
    option = {
        textStyle:{
            color:'#FFFFFF'
        },
        title: {
            text: titleName,
            subtext: subtext,
            textStyle:{
                color:'#FFFFFF'
            },
            subtextStyle:{
                color:'#FFFFFF'
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        color:['#0493F4','#5FB6A3'],
        legend: {
            data:[
                {
                    name: "水平",
                    textStyle: {
                        color: '#FFFFFF'
                    }
                }, {
                    name: "垂直",
                    textStyle: {
                        color: '#FFFFFF'
                    }
                }
                ]

        },
        xAxis: {
            type: 'category',
            name:"里程",
            data: xAxis,
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        yAxis: {
            type: 'value',
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        series: [
            {
                name:'水平',
                type:'line',
                data:yHorizontalAxis,
                itemStyle : {
                    normal : {
                        lineStyle:{
                            color:'#0493F4'
                        }
                    }
                }
            },
            {
                name:'垂直',
                type:'line',
                data:yVerticalAxis,
                itemStyle : {
                    normal : {
                        lineStyle:{
                            color:'#5FB6A3'
                        }
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
}


/**
 * 初始化高度布局。
 */
function initHeight(){
    var fatherHeight = $("#father").height();

    var fillLeftHeight = (fatherHeight - $("#myCharts1").height()*2 -100) < 0 ? 0:(fatherHeight - $("#myCharts1").height()*2 -100) ;
    $("#fillLeft").height(fillLeftHeight);

    var centerWidth = $("#ringNumber").width()
    $("#myCanvas").attr("width",centerWidth);
    $("#myCanvas").attr("height",fatherHeight-350);

    //设置右上布局
    $("#rightTopDiv").height((fatherHeight -80)/2);
    $("#horizontalDeviation").attr("width",$("#rightTopDiv").width()-40);
    $("#horizontalDeviation").attr("height",$("#rightTopDiv").height() - 50);
    var rightMarginTop = (($("#horizontalDeviation").height() - 100)/2) > 40 ? (($("#horizontalDeviation").height() - 100)/2):40;
    rightMarginTop = rightMarginTop > 100 ? 100 : rightMarginTop;
    $("#rightTopText").css("marginTop",rightMarginTop+"px");
    //$("#rightTopLineDiv").css("marginTop",rightMarginTop+"px");

    //设置右下布局
    $("#rightBottomDiv").height((fatherHeight -80)/2);
    $("#verticalDeviation").attr("width",$("#rightBottomDiv").width()-40);
    $("#verticalDeviation").attr("height",$("#rightBottomDiv").height() - 50);
    var rightBottomText = ($("#verticalDeviation").height()/2 -50) > 20 ? ($("#verticalDeviation").height()/2 -50) :20;
    rightBottomText = rightBottomText > 100 ? 100 : rightBottomText;
    $("#rightBottomText").css("marginTop",rightBottomText+"px");
    //$("#rightBottomLineDiv").css("marginTop",rightMarginTop+"px");
}

/**
 * 画水平与垂直偏差
 */
function drawDeviation(){
    var c1 = document.getElementById("myCanvas");
    var context = c1.getContext("2d");
    var incisionArr = coordinateTransform(info.incisionHorizontalDeviation,info.incisionVerticalDeviation);
    var tailArr = coordinateTransform(info.shieldTailHorizontalDeviation,info.shieldVerticalDeviation);
    //绘制盾尾图标
    drawSolid(context,tailArr[0]-10,tailArr[1],tailArr[0]+10,tailArr[1],2,'#99FF99');//水平线
    drawSolid(context,tailArr[0],tailArr[1]-10,tailArr[0],tailArr[1]+10,1,'#99FF99');//垂直线
    //绘制切口图标
    drawTriangle(context,incisionArr[0],incisionArr[1]-14,"#FFE599","fill");
    drawDashed(context,tailArr[0],tailArr[1],incisionArr[0],incisionArr[1],2,'#C79032');//#C79032

}

/**
 * 坐标转换
 */
function coordinateTransform(x,y){
    var width1 = $("#ringNumber").width();
    var coordinate = [];

    var xOfCanvas = width1/2 +  2*x;
    var yOfCanvas = 250 + (0 - 2*y);
    coordinate.push(xOfCanvas);
    coordinate.push(yOfCanvas);
    return coordinate;
}

/**
 * 画基础线
 */
function drawBaseLine(){
    //绘制中间圆图基线
    var c1 = document.getElementById("myCanvas");
    var context1 = c1.getContext("2d");
    var width1 = $("#ringNumber").width();
    //绘制x轴
    var xAxisLefttArr = coordinateTransform((0-width1/2),0);
    var xAxisRightArr = coordinateTransform((width1/4),0);
    drawSolid(context1, xAxisLefttArr[0], xAxisLefttArr[1], xAxisRightArr[0],xAxisRightArr[1],1,'#FFFFFF');
    drawArrow(context1, xAxisLefttArr[0], xAxisLefttArr[1], xAxisRightArr[0],xAxisRightArr[1],45,15,1,'#FFFFFF');
    //绘制y轴
    var yAxisToptArr = coordinateTransform(0,120);
    var yAxisBottomArr = coordinateTransform(0,-120);
    drawSolid(context1, yAxisBottomArr[0], yAxisBottomArr[1], yAxisToptArr[0],yAxisToptArr[1],1,'#FFFFFF');
    drawArrow(context1, yAxisBottomArr[0], yAxisBottomArr[1], yAxisToptArr[0],yAxisToptArr[1],45,15,1,'#FFFFFF');
    //绘制矩形,圆形,坐标
    var rectAngle = coordinateTransform(-50,50);
    drawRectangle(context1,rectAngle[0],rectAngle[1],200,200);
    var center = coordinateTransform(0,0);
    drawCircle(context1,center[0],center[1],200);
    drawText(context1);

    //绘制水平偏差基线
    var c2 = document.getElementById("horizontalDeviation");
    var context2 = c2.getContext("2d");
    var width2 = $("#horizontalDeviation").parent().width();
    var horizontalHeight = $('#rightTopText').outerHeight(true) - $('#rightTopText').outerHeight() + 50;//基线y值
    drawDashed(context2, 0, horizontalHeight, width2-30,horizontalHeight,1,'#99FF99');
    drawArrow(context2, 0, horizontalHeight, width2-30,horizontalHeight,30,15,1,'#99FF99');
    //绘制垂直偏差基线
    var c3 = document.getElementById("verticalDeviation");
    var context3 = c3.getContext("2d");
    var width3 = $("#verticalDeviation").parent().width();
    var verticalHeight = $('#rightBottomText').outerHeight(true) - $('#rightBottomText').outerHeight() + 50;//基线y值
    drawDashed(context3, 0, verticalHeight, width3-30,verticalHeight,1,'#99FF99');
    drawArrow(context3, 0, verticalHeight, width3-30,verticalHeight,30,15,1,'#99FF99');
}

/**
 * 绘制水平偏差线
 */
function drawHorizontalDeviation(){
    var c = document.getElementById("horizontalDeviation");
    var context = c.getContext("2d");
    var width = $("#horizontalDeviation").parent().width();
    //绘制水平偏差线
    var baseY = $('#rightTopText').outerHeight(true) - $('#rightTopText').outerHeight() + 50;//基线y值
    var horizonFromY = baseY +  Number(info.shieldVerticalDeviation)/2;
    var horizonToY = baseY+ Number(info.incisionHorizontalDeviation)/2;
    drawSolid(context,30,horizonFromY,width-70,horizonToY,1,'#F1D535');//#5FB6A3
    drawArrow(context,30,horizonFromY,width-70,horizonToY,30,15,1,'#F1D535');
}

/**
 * 绘制垂直偏差线
 */
function drawVertical(){
    var c = document.getElementById("verticalDeviation");
    var context = c.getContext("2d");
    var width = $("#verticalDeviation").parent().width();
    //绘制水平偏差线
    var baseY = $('#rightBottomText').outerHeight(true) - $('#rightBottomText').outerHeight() + 50;//基线y值
    var horizonFromY = baseY +  Number(info.shieldVerticalDeviation)/2;//+100是基线的y。
    var horizonToY = baseY + Number(info.incisionVerticalDeviation)/2;
    drawSolid(context,30,horizonFromY,width-70,horizonToY,1,'#F1D535');
    drawArrow(context,30,horizonFromY,width-70,horizonToY,30,15,1,'#F1D535');
}

/**
 * 绘制箭头
 * @param ctx
 * @param fromX
 * @param fromY
 * @param toX
 * @param toY
 * @param theta 角度15 ==》 15°
 * @param headlen 箭头长度
 * @param width 箭头宽度
 * @param color  线条颜色
 */
function drawArrow(ctx, fromX, fromY, toX, toY,theta,headlen,width,color) {
    theta = typeof(theta) != 'undefined' ? theta : 30;
    headlen = typeof(theta) != 'undefined' ? headlen : 10;
    width = typeof(width) != 'undefined' ? width : 1;
    color = typeof(color) != 'color' ? color : '#000';
    // 计算各角度和对应的P2,P3坐标
    var angle = Math.atan2(fromY - toY, fromX - toX) * 180 / Math.PI
        , angle1 = (angle + theta) * Math.PI / 180
        , angle2 = (angle - theta) * Math.PI / 180
        , topX = headlen * Math.cos(angle1)
        , topY = headlen * Math.sin(angle1)
        , botX = headlen * Math.cos(angle2)
        , botY = headlen * Math.sin(angle2);
    ctx.save();
    ctx.beginPath();

    arrowX = toX + topX;
    arrowY = toY + topY;
    ctx.moveTo(arrowX, arrowY);
    ctx.lineTo(toX, toY);//绘制箭头上部分
    arrowX = toX + botX;
    arrowY = toY + botY;
    ctx.lineTo(arrowX, arrowY);//绘制箭头下部分
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.stroke();
    ctx.restore();
}

/**
 *
 * 绘制实线
 * @param ctx
 * @param fromX
 * @param fromY
 * @param toX
 * @param toY
 * @param width
 * @param color
 */
function drawSolid(ctx, fromX, fromY, toX, toY,width,color){
    ctx.save();
    ctx.beginPath();
    ctx.moveTo(fromX, fromY);
    ctx.lineTo(toX, toY);
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.stroke();
    ctx.restore();
}

/**
 * 绘制虚线
 * @param ctx
 * @param fromX
 * @param fromY
 * @param toX
 * @param toY
 * @param width
 * @param color
 */
function drawDashed(ctx, fromX, fromY, toX, toY,width,color){
    ctx.save();
    ctx.beginPath();
    ctx.setLineDash([5,5]);
    ctx.moveTo(fromX, fromY);
    ctx.lineTo(toX, toY);
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.stroke();
    ctx.restore();
}

function drawCircle(ctx,x,y,r){
    ctx.save();
    ctx.beginPath();
    ctx.arc(x,y,r,0,2*Math.PI);
    ctx.strokeStyle = "#FFFFFF";
    ctx.stroke();
    ctx.restore();
}

function drawRectangle(ctx,x,y,width,height){
    ctx.save();
    ctx.beginPath();
    ctx.strokeStyle = "#FFFFFF";
    ctx.strokeRect(x,y,width,height);
    ctx.stroke();
    ctx.restore();
}

function drawText(ctx){
    ctx.save();
    ctx.beginPath();
    ctx.strokeStyle = "#FFFFFF";
    ctx.font="200 14px Open Sans ";
    var arr1 = coordinateTransform(0,100);
    var arr2 = coordinateTransform(100,0);
    var arr3 = coordinateTransform(0,-100);
    var arr4 = coordinateTransform(-100,0);
    ctx.strokeText('100',arr1[0],arr1[1]);
    ctx.strokeText('100',arr2[0],arr2[1]);
    ctx.strokeText('100',arr3[0],arr3[1]);
    ctx.strokeText('100',arr4[0],arr4[1]);

    var rectangle1 = coordinateTransform(0,50);
    var rectangle2 = coordinateTransform(50,0);
    var rectangle3 = coordinateTransform(0,-50);
    var rectangle4 = coordinateTransform(-50,0);
    ctx.strokeText('50',rectangle1[0],rectangle1[1]);
    ctx.strokeText('50',rectangle2[0],rectangle2[1]);
    ctx.strokeText('50',rectangle3[0],rectangle3[1]);
    ctx.strokeText('50',rectangle4[0],rectangle4[1]);
    ctx.stroke();
    ctx.restore();
}



function drawTriangle(ctx,x1, y1, color, type) {
    ctx.save();
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x1-10, y1+15);
    ctx.lineTo(x1+10, y1+15);
    ctx[type + 'Style'] = color;
    ctx.closePath();
    ctx[type]();
    ctx.stroke();
    ctx.restore();
}








