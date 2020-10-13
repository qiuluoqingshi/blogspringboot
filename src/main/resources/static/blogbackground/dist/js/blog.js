$(function () {
    $("#jqGrid").jqGrid({
        url: '../user/allBlogDetail',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true, align: 'center'},
            {label: '标题', name: 'blogTitle', index: 'blogTitle', width: 50, align: 'center'},
            {label: '预览图', name: 'blogCoverImage', index: 'blogCoverImage', width: 120, formatter: coverImageFormatter, align: 'center'},
            {label: '浏览量', name: 'blogViews', index: 'blogViews', width: 50, align: 'center'},
            {label: '状态', name: 'blogStatus', index: 'blogStatus', width: 50, formatter: statusFormatter, align: 'center'},
            {label: '博客分类', name: 'blogCategory', index: 'blogCategory', width: 80, align: 'center'},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 140, align: 'center'}
        ],
        height: "auto",
        rowNum: 10,
        rowList: [5, 10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "details",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
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

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"100\" width=\"100\" alt='coverImage'/>";
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" >草稿</button>";
        }
        else if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" >发布</button>";
        }
    }

});

/**
 * 搜索功能
 */
function search() {
    //标题关键字
    var keyword = $('#keyword').val();
    if (!validLength(keyword, 20)) {
        swal("搜索字段长度过大!", {
            icon: "error",
        });
        return false;
    }
    //数据封装
    var searchData = {keyword: keyword};
    //传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    //点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    //提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: '/user/allBlogDetail'}).trigger("reloadGrid");
}

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function addBlog() {
    window.location.href = "/user/blogsEdit";
}

function editBlog() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.location.href = "/user/blogsEdit/" + id;
}

function deleteBlog() {
    var blogDetailsId = getSelectedRows();
    if (blogDetailsId == null) {
        return;
    }
    alert(blogDetailsId)
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
                    url: "/user/oldBlogsDelete",
                    contentType: "application/json",
                    data: JSON.stringify(blogDetailsId),
                    success: function (res) {
                        if (res!=null && res == 1) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal("删除失败", {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}