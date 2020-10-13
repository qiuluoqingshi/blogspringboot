
function replybtn(obj,commentId,comment){
    $("#replyContent").remove()
    var userAvatar = GetCookie("userAvatar")
    var id = $(obj).parents("div")[1].id
    comments = comment
    pid = commentId
    $('#'+id).append("<div id='replyContent' class=\"usercommentSubmit\">\n" +
        "            <img class=\"avatar\" src=\""+userAvatar+"\"/>\n" +
        "            <textarea id=\"replyComment\" name=\"msg\" class=\"commentcontent\" placeholder=\"回复"+comment.userName+":\" rows=\"5\" cols=\"80\"></textarea>\n" +
        "            <button onclick='replyComment()' class=\"small blue commentsubmit\" type=\"submit\">回复</button>\n" +
        "        </div>")
    // $(this).parent().append("<div class='reply_textarea'><textarea name='' cols='40' rows='5'></textarea><br/><input type='submit' value='发表' /></div>");
};
function replyComment() {
    var commentcontent = $("#replyComment").val()
    if(commentcontent == ''){
        alert("回复不能为空")
        return
    }
    var commentDetailVo = {}
    commentDetailVo['userId'] = GetCookie("userId")
    commentDetailVo['userAvatar'] = GetCookie("userAvatar")
    commentDetailVo['blogId'] = blogid
    commentDetailVo['commentContent'] = commentcontent
    commentDetailVo['replyUserId'] = comments.userId
    commentDetailVo['pid'] = pid
    // alert(commentDetailVo['userId']+":"+commentDetailVo['userName']+":"+commentDetailVo['userAvatar']+":"+commentDetailVo['blogId']+":"+commentDetailVo['commentContent']+":"+commentDetailVo['replyUserName']+":"+commentDetailVo['pid'])
    $.ajax({
        type:"POST",
        url:"../user/comment",
        dataType:"json",
        contentType: "application/json",
        data:JSON.stringify(commentDetailVo),
        success:function (res) {
            if(res == 2){
                swal("回复成功", {
                    icon: "success",
                });
                $("#comments").load('../user/freshcomment?blogid='+blogid+'&pagenum='+currentPageNum)
            }else{
                swal("回复失败，请刷新页面重新尝试", {
                    icon: "error",
                });
                alert('');
            }
        }

    })

}
function usercomment(){
    var commentcontent = $("#usercommentcontent").val()
    if(commentcontent == ''){
        alert("评论不能为空")
        return
    }
    var commentDetailVo = {}
    commentDetailVo['userId'] = GetCookie("userId")
    commentDetailVo['userAvatar'] = GetCookie("userAvatar")
    commentDetailVo['blogId'] = blogid
    commentDetailVo['commentContent'] = commentcontent
    $.ajax({
        type:"POST",
        url:"../user/comment",
        dataType:"json",
        contentType:"application/json", // 指定这个协议很重要
        data:JSON.stringify(commentDetailVo),
        success:function (res) {
            if(res == 2){
                swal("评论成功", {
                    icon: "success",
                });
                $("#comments").load('../user/freshcomment?blogid='+blogid+'&pagenum=1')
                currentPageNum = 1
            }else{
                swal("评论失败", {
                    icon: "error",
                });            }
        }

    })
}
function prepage(prePageNum){
    if(prePageNum > 0){
        $("#comments").load('../user/freshcomment?blogid='+blogid+'&pagenum='+prePageNum)
        currentPageNum = prePageNum
    }
}
function nextpage(commentPageNum,nextPageNum){
    if(nextPageNum <= commentPageNum){
        $("#comments").load('../user/freshcomment?blogid='+blogid+'&pagenum='+nextPageNum)
        currentPageNum = nextPageNum
    }
}
$('#commentSubmit').click(function () {
    var blogId = $('#blogId').val();
    var verifyCode = $('#verifyCode').val();
    var commentator = $('#commentator').val();
    var email = $('#email').val();
    var websiteUrl = $('#websiteUrl').val();
    var commentBody = $('#commentBody').val();
    if (isNull(blogId)) {
        swal("参数异常", {
            icon: "warning",
        });
        return;
    }
    if (isNull(commentator)) {
        swal("请输入你的称呼", {
            icon: "warning",
        });
        return;
    }
    if (isNull(email)) {
        swal("请输入你的邮箱", {
            icon: "warning",
        });
        return;
    }
    if (isNull(verifyCode)) {
        swal("请输入验证码", {
            icon: "warning",
        });
        return;
    }
    if (!validCN_ENString2_100(commentator)) {
        swal("请输入符合规范的名称(不要输入特殊字符)", {
            icon: "warning",
        });
        return;
    }
    if (!validCN_ENString2_100(commentBody)) {
        swal("请输入符合规范的评论内容(不要输入特殊字符)", {
            icon: "warning",
        });
        return;
    }
    var data = {
        "blogId": blogId, "verifyCode": verifyCode, "commentator": commentator,
        "email": email, "websiteUrl": websiteUrl, "commentBody": commentBody
    };
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: '/blog/comment',
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                swal("评论提交成功请等待博主审核", {
                    icon: "success",
                });
                $('#commentBody').val('');
                $('#verifyCode').val('');
            }
            else {
                swal(result.message, {
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
});