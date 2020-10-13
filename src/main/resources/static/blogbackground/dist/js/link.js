$(function () {
    $("#jqGrid").jqGrid({
        url: '/user/allLinks',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'linkId', index: 'linkId', width: 50, key: true, hidden: true},
            {label: '网站名称', name: 'linkName', index: 'linkName', width: 100},
            {label: '网站链接', name: 'linkHref', index: 'linkHref', width: 120},
            {label: '网站归属', name: 'linkAuthor', index: 'linkAuthor', width: 120},
            {label: '网站描述', name: 'linkDescription', index: 'linkDescription', width: 120},
            {label: '网站类型', name: 'linkType', index: 'linkType', formatter: linkTypeFormatter, width: 120},
            {label: '排序值', name: 'linkRank', index: 'linkRank', width: 70},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 100}
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
    function linkTypeFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<p>友链</p>";
        } else if (cellvalue == 1) {
            return "<p>推荐网站</p>";
        } else if (cellvalue == 2) {
            return "<p>个人链接</p>";
        }
    }
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

function linkAdd() {
    reset();
    $('.modal-title').html('友链添加');
    $('#linkModal').modal('show');
    $("#saveButton").removeAttr("disabled")
}

$('#cancelButton').click(function () {
    $("#saveButton").attr("disabled","disabled")
});

$('#linkModal').on('hidden', function () {
    $("#saveButton").attr("disabled","disabled")
});

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var linkId = $("#linkId").val();
    var linkType = $('#linkType option:selected').val();
    var linkName = $("#linkName").val();
    var linkHref = $("#linkHref").val();
    var linkAuthor = $("#linkAuthor").val();
    var linkDescription = $("#linkDescription").val();
    var linkRank = $("#linkRank").val();
    if (!validCN_ENString2_18(linkName)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的名称！");
        return;
    }
    if (!isURL(linkHref)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的网址！");
        return;
    }
    if (!validCN_ENString2_18(linkAuthor)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的归属者！");
        return;
    }
    if (!validCN_ENString2_100(linkDescription)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的描述！");
        return;
    }
    if (isNull(linkRank) || linkRank < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的排序值！");
        return;
    }
    var linkDetail = {}
    linkDetail['linkId'] = linkId;
    linkDetail['linkName'] = linkName;
    linkDetail['linkHref'] = linkHref;
    linkDetail['linkAuthor'] = linkAuthor;
    linkDetail['linkDescription'] = linkDescription;
    linkDetail['linkRank'] = linkRank;
    linkDetail['linkType'] = linkType;
    var url = '/user/insertNewLink';
    if (linkId != null && linkId > 0) {
        url = '/user/updateOldLink/';
    }
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(linkDetail),
        success: function (res) {
            if (res!=null && res.resultCode == 200) {
                $('#linkModal').modal('hide');
                swal(res.resultMessage, {
                    icon: "success",
                });
                reload();
            }
            else {
                $('#linkModal').modal('hide');
                swal(res.resultMessage, {
                    icon: "error",
                });
            };
            $("#saveButton").attr("disabled","disabled")
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });

});

function linkEdit() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    reset();
    var rowid=$("#jqGrid").jqGrid("getGridParam","selrow");
    var rowData=$("#jqGrid").jqGrid("getRowData",rowid);
    //请求数据
    $("#linkId").val(rowData['linkId'])
    $("#linkName").val(rowData['linkName']);
    $("#linkHref").val(rowData['linkHref']);
    $("#linkAuthor").val(rowData['linkAuthor']);
    $("#linkDescription").val(rowData['linkDescription']);
    $("#linkRank").val(rowData['linkRank']);
    var linkType = rowData['linkType']
    //根据原linkType值设置select选择器为选中状态
    if (linkType == 1) {
        $("#linkType option:eq(1)").prop("selected", 'selected');
    } else if (linkType == 2) {
        $("#linkType option:eq(2)").prop("selected", 'selected');
    }
    $('.modal-title').html('友链修改');
    $('#linkModal').modal('show');
}

function deleteLink() {
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
                    url: "/user/deleteOldLink",
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

function reset() {
    $("#linkId").val(0)
    $("#linkName").val('');
    $("#linkHref").val('');
    $("#linkAuthor").val('');
    $("#linkDescription").val('');
    $("#linkRank").val(0);
    $('#edit-error-msg').css("display", "none");
    $("#linkType option:first").prop("selected", 'selected');
}