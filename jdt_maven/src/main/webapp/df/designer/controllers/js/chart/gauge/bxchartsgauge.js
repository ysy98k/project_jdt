var BxChartGauge = (function () {

    function BxChartGauge(type, actions,newSerieOpt) {

        function BxChartGauge() {

        };

        BxChartGauge.prototype = new BxChartCommon(type, actions);

        BxChartGauge.prototype.defaultInitOption = function(){
        	var defaultInitOption = {
                isOvertime: true,
        	};
        	return defaultInitOption;
        };

        BxChartGauge.prototype.showPreviewCore = function () {//设计
	        var id = this.getPreviewId();
	        var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);
            $("#" + id).bxchartsgauge(option);
	    };

	    BxChartGauge.prototype.showCore = function () {//预览
            var id = this.getId();
	        var option = this.prepareOption();
			$("#" + id).bxchartsgauge(option);
	    };

	    BxChartGauge.prototype.getConfigViewCore = function ($configView) {//属性框
            var bxcharts = this;
	        var chartOpt = bxcharts.getOptions()["chartOption"];
	        bxcharts.getConfigViewsControl("chartId", $configView).text(bxcharts.getId());
	        bxcharts.setWidgetSize($configView);
	        var allAtttibute = bxcharts.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
	        $.each(allAtttibute, function (index, item) {
	        	bxcharts.setAttributeFromOption(chartOpt,$(this));
	            $(this).change(function () {
		        	bxcharts.setOptionValue(chartOpt,$(this));
	                bxcharts.setOption("option", chartOpt);
	                bxcharts.refresh();
	            });
	        });
	    };

	    BxChartGauge.prototype.getDataConfigViewCore = function ($dataConfigView) {//后台数据源
            var bxcharts = this;
            // 获取图表数据绑定信息
            bxcharts.initDataConfigView($dataConfigView, newSerieOpt);
	    };

		BxChartGauge.prototype.prepareOption = function () {
			var dataSource = this.getOption("dataSource");
			var chartOptionData = this.getOption("chartOption").series[0].data[0];
			chartOptionData.value = DesignerUtils.queryViaComboTree(dataSource.polarData.value).split(",")[0];
			chartOptionData.name = dataSource.polarData.name;
			return this.getOptions();
		};

        return new BxChartGauge();
    }

    return BxChartGauge;
})();

