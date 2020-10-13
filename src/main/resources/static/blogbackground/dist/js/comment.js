$(function () {
    $("#jqGrid").jqGrid({
        url: '/user/allCommentDetail',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'commentId', index: 'commentId', width: 50, key: true, hidden: true},
            {label: '评论者id', name: 'userId', index: 'userId', width: 50, hidden: true},
            {label: 'pid', name: 'pid', index: 'pid', width: 50,  hidden: true},
            {label: '评论内容', name: 'commentContent', index: 'commentContent', width: 120},
            {label: '评论者', name: 'userName', index: 'userName', width: 60},
            {label: '评论时间', name: 'commentTime', index: 'commentTime', width: 60}
            // {label: '回复对象', name: 'replyUserName', index: 'replyUserName', width: 90},
        ],
        // editable : true,
        // edittype : 'select',
        // editoptions: {
        //     dataUrl:'${ctx}/guru/findAll'
        // },
        // formatter:function (value, options, row) {
        //     return row.guru.dharma;
        // },
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
        postData: {
            blogid: blogid
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

/**
 * 批量删除
 */
function deleteComments() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认删除这些评论吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: '/user/deleteCommentDetail',
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (res) {
                        if (res != null && res.resultCode == 200) {
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


function reply() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var rowData = $("#jqGrid").jqGrid('getRowData', id);
    $("#replyBody").val('');
    $('#replyModal').modal('show');
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var replyBody = $("#replyBody").val();
    if (!validCN_ENString2_100(replyBody)) {
        swal("只能输入2-100位的中英文字符串!", {
            icon: "warning",
        });
        return;
    } else {
        var url = '/admin/comments/reply';
        var grid = $("#jqGrid");
        var rowid=$("#jqGrid").jqGrid("getGridParam","selrow");
        var rowData=$("#jqGrid").jqGrid("getRowData",rowid);
        var commentDetailVo = {}
        commentDetailVo['userId'] = GetCookie("userId");
        commentDetailVo['blogId'] = blogid;
        commentDetailVo['commentContent'] = replyBody;
        commentDetailVo['replyUserId'] = rowData["userId"];
        if(rowData['pid']!=0){
            commentDetailVo['pid'] = rowData['pid'];
        }else{
            commentDetailVo['pid'] = rowData['commentId'];
        }
        $.ajax({
            type:"POST",
            url:"../user/comment",
            dataType:"json",
            contentType: "application/json",
            data:JSON.stringify(commentDetailVo),
            success:function (res) {
                if(res!=null && res == 2){
                    swal("回复成功", {
                        icon: "success",
                    });
                    $('#replyModal').modal('hide');
                    reload();
                }else{
                    swal("回复失败，请刷新页面重新尝试", {
                        icon: "error",
                    });
                }
            }

        })
    }
});
function blogTitleChange(){
    blogid = $('#blogTitle option:selected').val();
    $('#jqGrid').jqGrid('clearGridData');
    var postData = $('#jqGrid').jqGrid("getGridParam", "postData");
    $.each(postData, function (k, v) {
        delete postData[k];
    });
    $('#jqGrid').setGridParam({postData:{blogid:$('#blogTitle option:selected').val()}}).trigger("reloadGrid");
}
