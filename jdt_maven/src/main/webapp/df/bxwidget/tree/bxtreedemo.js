var dataObj;
$(document).ready(function() {
    baosightRequire.requireFunct(["bxtree","bxcomboboxtree"], function () {
        showTree1();
        showTree2();
        showTreeOther();
        showSelectTree();
    });
});

function showTree1() {

    var handleCheckFunct = function(option){
        var children = option.core.data.children;
        if(children.length >　0){
            var oneChild = children[0];
            oneChild.state.checked = true;
        }
    };

    var option = {
        checkbox: {
            keep_selected_style: false,
            tie_selection: false,
            three_state: false,
            cascade: 'undetermined'
        },
        "plugins" : ["themes","wholerow"]
    };

    $("#bxtree1").bxtree({
        ccsId: "bxmenu",
        showText : '所有工序',
        option: option,
        dataPattern: 'ccs',
        preHandlerFunct: handleCheckFunct
    });
}


function showTree2() {

    dataObj = [
        {	'value' : 'node_1',
            'label' : '数节点1',
            'state' : { 'opened' : true },
            'children' : [ { 'label' : '叶子1','state' : { 'opened' : true, 'checked' : true },'children' : [ {'label' :'末端'}]}, {'label' :'叶子2'}]},
        {
            'value' : 'node_2',
            'label' : '数节点2',
            'state' : { 'opened' : true, 'checked' : true },
            'children' : [  {'label' :'叶子3'}]
        }
    ];

    $("#bxtree2").bxtree({
        dataPattern : 'local',
        showText : null,
        data : dataObj

    });
}

function showTreeOther(){
    for(var i=3;i<7;i++){
        $("#bxtree"+i).bxtree({
            dataPattern : 'local',
            showText : '树根',
            data : dataObj

        });
    }

}

function showSelectTree(){
    var dataObj = [
        {	'value' : 'node_1',
            'label' : '数节点1',
            'state' : { 'opened' : true },
            'children' : [ { 'label' : '叶子1','state' : { 'opened' : true, 'checked' : true },'children' : [ {'label' :'末端'}]}, {'label' :'叶子2'}]},
        {
            'value' : 'node_2',
            'label' : '数节点2',
            'state' : { 'opened' : true, 'checked' : true },
            'children' : [  {'label' :'叶子3'}]
        }
    ];
    var option = {
        checkbox: {
            keep_selected_style: false,
            tie_selection: false,
            three_state: false,
            cascade: 'undetermined'
        },
        "plugins" : ["themes","wholerow","search"]
    };

    $("#bxtree7").bxcomboboxtree({
        dataPattern : 'local',
        option: option,
        showText : '树根',
        data : dataObj

    });

}
