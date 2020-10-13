$(function () {
    $("#jqGrid").jqGrid({
        url: '/user/allCategories',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'blogcategoryId', index: 'blogcategoryId', width: 50, key: true, hidden: true},
            {label: '分类名称', name: 'blogcategoryName', index: 'blogcategoryName', width: 240},
            {label: '分类访问数', name: 'blogcategoryViews', index: 'blogcategoryViews', width: 150},
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

function categoryAdd() {
    reset();
    $('.modal-title').html('分类添加');
    $('#categoryModal').modal('show');
}
$("#cacelButton").click(function (){
        $("#categoryId").val('');
        $("#categoryName").val('');
    }
)
//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var categoryName = $("#categoryName").val();
    if (!validCN_ENString2_18(categoryName)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入2-18位的中英文字符！");
    } else {
        var url = '/user/saveBlogCategory';
        var id = $("#categoryId").val();
        var category = {}
        category['blogcategoryName'] = categoryName;
        if (id != '') {
            category['blogcategoryId'] = id;
            url = '/user/updateBlogCategory';
        }
        $.ajax({
            type: 'POST',//方法类型
            url: url,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(category),
            success: function (res) {
                if (res!=null && res.resultCode == 200) {
                    $('#categoryModal').modal('hide');
                    swal(res.resultMessage, {
                        icon: "success",
                    });
                    $('#categoryModal').modal('hide');
                    reload();
                    $("#categoryId").val('');
                } else {
                    swal(res.resultMessage, {
                        icon: "error",
                    });
                }
            },
            // error: function () {
            //     swal("操作失败", {
            //         icon: "error",
            //     });
            //     $("#categoryId").val('');
            // }
        });
    }
});

function categoryEdit() {
    reset();
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    if (id == 1){
        swal("默认分类不可以修改", {
            icon: "error",
        });
        return;
    }
    var rowid=$("#jqGrid").jqGrid("getGridParam","selrow");
    var rowData=$("#jqGrid").jqGrid("getRowData",rowid);
    $('.modal-title').html('分类编辑');
    $('#categoryModal').modal('show');
    $("#categoryId").val(id);
    $("#categoryName").val(rowData['blogcategoryName']);
}

function deleteCagegory() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    if (ids[0] == 1) {
        swal("默认分类不可以删除", {
            icon: "error",
        });
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
                    url: "/user/deleteBlogCategory",
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
    $("#categoryName").val('');
    $("#categoryIcon option:first").prop("selected", 'selected');
}