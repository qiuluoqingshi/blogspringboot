$(function () {
    //修改站点信息
    $('#updateWebButton').click(function () {
        if (validWebMessageForUpdate()){
            $("#updateWebButton").attr("disabled", true);
            var configurationDetail = {}
            configurationDetail['webName'] = $('#webName').val();
            configurationDetail['webDescribe'] = $('#webDescribe').val();
            configurationDetail['webIcp'] = $('#webIcp').val();
            $.ajax({
                type: "POST",
                url: "/user/updateWebMessage",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(configurationDetail),
                success: function (res) {
                    if (res != null && res.resultCode == 200) {
                        swal(res.resultMessage, {
                            icon: "success",
                        });
                    }
                    else {
                        swal(res.resultMessage, {
                            icon: "error",
                        });
                    }
                    $("#updateWebButton").removeAttr("disabled")
                },
                error: function () {
                    swal("操作失败", {
                        icon: "error",
                    });
                    $("#updateWebButton").removeAttr("disabled")
                }
            });
        }

    });
    //个人信息
    $('#updateUserInfoButton').click(function () {
        if (validUserInfoForUpdate()){
            $("#updateUserInfoButton").attr("disabled", true);
            var configurationDetail = {}
            configurationDetail['webByAuthor']  = $('#webByAuthor').val();
            configurationDetail['authorContact'] = $('#authorContact').val();
            configurationDetail['authorEmail'] = $('#authorEmail').val();
            $.ajax({
                type: "POST",
                url: "/user/updateUserInfo",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(configurationDetail),
                success: function (res) {
                    if (res != null && res.resultCode == 200) {
                        swal(res.resultMessage, {
                            icon: "success",
                        });
                    }
                    else {
                        swal(res.resultMessage, {
                            icon: "error",
                        });
                    }
                    $("#updateUserInfoButton").removeAttr("disabled")
                },
                error: function () {
                    swal("操作失败", {
                        icon: "error",
                    });
                    $("#updateUserInfoButton").removeAttr("disabled")
                }
            });
        }
    });

    $("#updateWebIcon").click(function () {
        var formData = new FormData();
        var file = document.getElementById('webIcon_file').files[0];
        formData.append("webIcon-image-file", file);
        $.ajax({
            url:'/user/updateWebIcon',
            type: "post",
            data: formData,
            dataType: "json",
            cache: false,//上传文件无需缓存
            processData: false,//用于对data参数进行序列化处理 这里必须false
            contentType: false, //必须*/
            success: function (res) {
                if (res != null && res.resultCode == 200) {
                    swal(res.resultMessage, {
                        icon: "success",
                    });
                    $("#updateWebIcon").attr("disabled","disabled")
                } else {
                    swal(res.resultMessage, {
                        icon: "error",
                    });
                }
            }
        })
    })
})



/**
 * 信息验证
 */
function validWebMessageForUpdate() {
    var webName = $('#webName').val();
    var webDescribe = $('#webDescribe').val();
    var webIcp = $('#webIcp').val();
    if (isNull(webName) && webName.trim().length < 1) {
        $('#websiteForm-info').css("display", "block");
        $('#websiteForm-info').html("网站名不能为空！");
        return false;
    }
    if (!validCN_ENString2_18(webName)) {
        $('#websiteForm-info').css("display", "block");
        $('#websiteForm-info').html("2-18位的中英文网站名称！");
        return false;
    }
    if (isNull(webDescribe) && webDescribe.trim().length < 1) {
        $('#websiteForm-info').css("display", "block");
        $('#websiteForm-info').html("网站描述不能为空！");
        return false;
    }
    if (!validCN_ENString2_18(webDescribe)) {
        $('#websiteForm-info').css("display", "block");
        $('#websiteForm-info').html("2-18位的网站描述！");
        return false;
    }

    if (isNull(webIcp) && webIcp.trim().length < 1) {
        $('#websiteForm-info').css("display", "block");
        $('#websiteForm-info').html("请输入正确的备案号！");
        return false;
    }
    return true;
}
function validUserInfoForUpdate() {
    var webByAuthor = $('#webByAuthor').val();
    var authorContact = $('#authorContact').val();
    var authorEmail = $('#authorEmail').val();
    if (isNull(webByAuthor) && webByAuthor.trim().length < 1) {
        $('#userInfoForm-info').css("display", "block");
        $('#userInfoForm-info').html("个人名字不能为空！");
        return false;
    }
    if (!validCN_ENString2_18(webByAuthor)) {
        $('#userInfoForm-info').css("display", "block");
        $('#userInfoForm-info').html("2-18位的中英文个人名字！");
        return false;
    }
    if (isNull(authorContact) || !isNumber(authorContact)) {
        $('#userInfoForm-info').css("display", "block");
        $('#userInfoForm-info').html("请输入正确的QQ！");
        return false;
    }
    if (isNull(authorEmail) && authorEmail.trim().length < 1) {
        $('#userInfoForm-info').css("display", "block");
        $('#userInfoForm-info').html("邮箱不能为空！");
        return false;
    }
    return true;
}