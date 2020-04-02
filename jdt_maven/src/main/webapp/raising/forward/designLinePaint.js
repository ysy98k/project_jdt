// points格式：[{mileage: 1, x: 1, y: 1, z: 1 }, {mileage: 1, x: 1, y: 1, z: 1 }, {mileage: 1, x: 1, y: 1, z: 1 }...]
// type: 0 表示绘制平曲线，1表示绘制竖曲线
var DesignLine = function (id, points, type, color) {
    this.id = id;
    this.points = points;
    this.type = type;  // 0 表示绘制平曲线，1表示绘制竖曲线
    var paperdiv = $("#" + id)
    this.w = paperdiv.width();
    this.h = paperdiv.height();
    // 纵向放大倍率
    this.rate = 20;
    // 鼠标是否按下
    this.isMouseDown = 0;
    // 鼠标是否在绘图范围
    this.isMouseEnter = 0;
    // 当前坐标
    this._mouse_x = 0;
    this._mouse_y = 0;
    var self = this;
    // 视口参数
    this._x = 0;
    this._y = 0;
    this._scale = 1;
    // 创建paper对象
    this.paper = Raphael(id, this.w, this.h);
    paperdiv.bind("mousewheel", function (event, delta) {
        if (delta > 0) {
            self.zoomIn();
        }
        else {
            self.zoomOut();
        }
    }).bind("dblclick", function () {
        self.fullExtent();
    }).bind("mousedown", function (e) {
        self.isMouseDown = 1;
        self._mouse_x = e.pageX;
        self._mouse_y = e.pageY;
    }).bind("mouseup", function () {
        self.isMouseDown = 0;
    }).bind("mousemove", function (e) {
        if (self.isMouseDown == 1) {
            self.pan(self._mouse_x - e.pageX, self._mouse_y - e.pageY);
        }
        self._mouse_x = e.pageX;
        self._mouse_y = e.pageY;
    }).bind("mouseenter", function () {
        self.isMouseEnter = 1;
    }).bind("mouseleave", function () {
        self.isMouseDown = 0;
        self.isMouseEnter = 0;
    });
    var path = this.coorlistToPath(this.points);
    var line = this.paper.path(path).attr({
        stroke: color || Raphael.getColor(),
        "stroke-width": 4,
        "stroke-linecap": "round"
    });
    // 拦截鼠标滚轮效果
    if (document.addEventListener) {
        document.addEventListener('DOMMouseScroll', scrollFunc, false);
    }//W3C  
    window.onmousewheel = document.onmousewheel = scrollFunc;//IE/Opera/Chrome  
    function scrollFunc(evt) {
        if (true) {
            evt = evt || window.event;
            if (evt.preventDefault) {
                // Firefox  
                evt.preventDefault();
                evt.stopPropagation();
            } else {
                // IE  
                evt.cancelBubble = true;
                evt.returnValue = false;
            }
            return false;
        }
        return true;
    }
}
DesignLine.prototype = {
    constructor: DesignLine,
    zoomIn: function () {
        this._scale *= 0.5;
        this._x += this.w * this._scale * 0.5;
        this._y += this.h * this._scale * 0.5;
        this.update();
    },
    zoomOut: function () {
        this._scale *= 2;
        this._x -= this.w * this._scale * 0.25;
        this._y -= this.h * this._scale * 0.25;
        this.update();
    },
    pan: function (x, y) {
        this._x += x * this._scale;
        this._y += y * this._scale;
        this.update();
    },
    fullExtent: function () {
        this._x = 0;
        this._y = 0;
        this._scale = 1;
        this.update();
    },
    update: function () {
        this.paper.setViewBox(this._x, this._y, this.w * this._scale, this.h * this._scale, true);
    },
    disabledMouseWheel: function () {
        this.isMouseEnter = 0;
    },
    enabledMouseWheel: function () {
        this.isMouseEnter = 1;
    },
    coorlistToPath: function (points) {
        // 获取线路范围
        var xmin = 100000000000.0, ymin = 100000000000.0, xmax = -100000000000.0, ymax = -100000000000.0;
        var extentW = 0.0, extentH = 0.0, scale = 0.0, panX = 0.0, panY = 0.0;

        for (var i = 0; i < points.length; i++) {
            var x = 0, y = 0;
            if (this.type == 0) {
                x = points[i].y;
                y = points[i].x;
            }
            else {
                x = points[i].designmileage;
                y = points[i].z * this.rate;
            }
            if (x < xmin) xmin = x;
            if (x > xmax) xmax = x;
            if (y < ymin) ymin = y;
            if (y > ymax) ymax = y;
        }
        extentW = xmax - xmin;
        extentH = ymax - ymin;
        // 计算转换参数
        if (extentW / extentH > this.w / this.h * 1.0) {
            scale = this.w / extentW;
            panX = 0 - scale * xmin;
            panY = this.h - 0.5 * (this.h - scale * (ymax - ymin)) + scale * ymin;
        }
        else {
            scale = this.h / extentH;
            panX = 0.5 * (this.w - scale * (xmax - xmin)) - scale * xmin;
            panY = this.h + scale * ymin;
        }
        var path = "";
        for (i = 0; i < this.points.length; i++) {
            var x = 0, y = 0;
            if (this.type == 0) {
                x = points[i].y;
                y = points[i].x;
            }
            else {
                x = points[i].designmileage;
                y = points[i].z * this.rate;
            }
            var screenX = scale * x + panX;
            var screenY = -scale * y + panY;
            if (i == 0) {
                path += "M" + screenX + " " + screenY;
            }
            else {
                path += "L" + screenX + " " + screenY;
            }
        }
        return path;
    }
}