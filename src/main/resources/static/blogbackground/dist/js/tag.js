$(function () {
    $("#jqGrid").jqGrid({
        url: '/user/allTags',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'tagId', index: 'tagId', width: 50, key: true, hidden: true},
            {label: '标签名称', name: 'tagName', index: 'tagName', width: 240},
            {label: '标签访问次数', name: 'tagViews', index: 'tagViews', width: 150},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 120}
        ],
        height: "auto",
        rowNum: 5,
        rowList: [5, 10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.details",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function tagAdd() {
    var tagName = $("#tagName").val();
    if (!validCN_ENString2_18(tagName)) {
        swal("标签名称只能2-18中英文字符", {
            icon: "error",
        });
    } else {
        var url = '/user/insertNewTags';
        var tagDetail = {}
        tagDetail['tagName'] = tagName
        $.ajax({
            type: 'POST',//方法类型
            url: url,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(tagDetail),
            success: function (res) {
                if (res!=null && res.resultCode == 200) {
                    $("#tagName").val('')
                    swal(res.resultMessage, {
                        icon: "success",
                    });
                    reload();
                }
                else {
                    $("#tagName").val('')
                    swal(res.resultMessage, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    }
}

function deleteTag() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/user/deleteOldTags",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (res) {
                        if (res!=null && res.resultCode == 200) {
                            swal(res.resultMessage, {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(res.resultMessage, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}
