var TabLoader = (function () {
    var $configview;

    var load = function () {
        var paths = {
            tabHtml: "interaction/tab.html",
        };
        ContentLoader.loadHtml(paths.tabHtml).done(function (data) {
            var $div = $(data);
            $configview = $div.find(".configView");
        });
    };

    var showConfigView = function (target, $dest) {
        var $configviewClone = $configview.clone(true);
        $dest.empty();
        $dest.append($configviewClone);
        bindConfigViewAction($(target), $configviewClone);
    };

    var bindConfigViewAction = function ($target, $configview) {
        //tab标题
        var $tab_title = $configview.find("#tab_title");
        var thisTitle = $target.text();
        $tab_title.val(thisTitle);
        $tab_title.change(function (e) {
            $target.text($tab_title.val());
        });

    };

    return {
        load: load,
        showConfigView: showConfigView,
    }
})();