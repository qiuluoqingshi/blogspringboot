$(function () {
    //修改个人信息
    $('#updateUserMessageButton').click(function () {
        $("#updateUserMessageButton").attr("disabled",true);
        if (validUserMessageForUpdate()) {
            //ajax提交数据
            var userDetail = {}
            userDetail['userName'] = $('#userName').val();;
            userDetail['userSignature'] = $('#userSignature').val();;
            userDetail['userSex'] = $('#userSex').val();;
            userDetail['userBirth'] = $('#userBirth').val();;
            userDetail['userWeixin'] = $('#userWeixin').val();;
            userDetail['userQq'] = $('#userQq').val();;
            userDetail['userSchool'] = $('#userSchool').val();;
            userDetail['userCompany'] = $('#userCompany').val();;
            userDetail['userPhone'] = $('#userPhone').val();;
            $.ajax({
                type: "POST",
                url: "/user/updateUserMessage",
                dataType:"json",
                contentType:"application/json",
                data: JSON.stringify(userDetail),
                success: function (res) {
                    if (res != null && res.resultCode == 200) {
                        swal(res.resultMessage, {
                            icon: "success",
                        });
                        if (userDetail['userName'] != GetCookie("userName")) {
                            var judge =document.cookie.indexOf("expire=")
                            deleteCookie("userName",GetCookie("userName"))
                            if (judge == -1){
                                setCookie("userName", userDetail['userName'])
                            }else {
                                setCookie("userName", userDetail['userName'], GetCookie("expire"))
                            }
                        }
                    } else {
                        swal(res.resultMessage, {
                            icon: "error",
                        });
                    }
                }
            });
        } else {
            $("#updateUserMessageButton").prop("disabled",false);
        }
    });
    //修改密码
    $('#updatePasswordButton').click(function () {
        $("#updatePasswordButton").attr("disabled",true);
        var oldPassword = $('#oldPassword').val();
        var newPassword = $('#newPassword').val();
        var newPasswordAgain = $('#newPasswordAgain').val();
        if (validPasswordForUpdate(oldPassword, newPassword, newPasswordAgain)) {
            var params = $("#userPasswordForm").serialize();
            $.ajax({
                type: "POST",
                url: "/user/updateUserPassword",
                data: params,
                success: function (res) {
                    if (res != null && res.resultCode == 200) {
                        swal(res.resultMessage, {
                            icon: "success",
                        });
                        window.location.href="/logout";
                    } else {
                        swal(res.resultMessage, {
                            icon: "error",
                        });
                    }
                }
            });
        } else {
            $("#updatePasswordButton").attr("disabled",false);
        }
    });
    $("#updateUserAvatarButton").click(function () {
        var formData = new FormData();
        var file = document.getElementById('avatar_file').files[0];
        formData.append("userAvatar-image-file", file);
        $.ajax({
            url:'/user/updateUserAvatar',
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
                    var judge =document.cookie.indexOf("expire=")
                    deleteCookie("userAvatar",GetCookie("userAvatar"))
                    if (judge == -1){
                        setCookie("userAvatar", res.data)
                    }else {
                        setCookie("userAvatar", res.data, GetCookie("expire"))
                    }
                    $("#sidebar-userAvatar").attr("src",res.data)
                } else {
                    swal(res.resultMessage, {
                        icon: "error",
                    });
                    $("#avatar_img").attr("src", GetCookie("userAvatar"));
                    $("#updateUserAvatarButton").attr("disabled","disabled")
                }
            }
        })
    })
})
function userMessageChange(){
    $("#updateUserMessageButton").removeAttr("disabled")
}

/**
 * 信息验证
 */
function validUserMessageForUpdate() {
    var userName = $('#userName').val();
    var userQq = $('#userQq').val();
    var userPhone = $('#userPhone').val();

    if (isNull(userName) || userName.trim().length < 1) {
        $('#updateUserMessage-info').css("display", "block");
        $('#updateUserMessage-info').html("昵称不能为空！");
        return false;
    }
    if (!validCN_ENString2_18(userName)) {
        $('#updateUserMessage-info').css("display", "block");
        $('#updateUserMessage-info').html("2-18位的中英文昵称！");
        return false;
    }
    if (!isNull(userQq) && !isNumber(userQq)) {
        $('#updateUserMessage-info').css("display", "block");
        $('#updateUserMessage-info').html("请输入正确的QQ！");
        return false;
    }
    if (!isNull(userPhone) && !isNumber(userPhone)) {
        $('#updateUserMessage-info').css("display", "block");
        $('#updateUserMessage-info').html("请输入正确的手机号码！");
        return false;
    }
    if (!isNull(userPhone) && userPhone.trim().length != 11) {
        $('#updateUserMessage-info').css("display", "block");
        $('#updateUserMessage-info').html("请输入正确的手机号码！");
        return false;
    }
    return true;
}

/**
 * 密码验证
 */
function validPasswordForUpdate(oldPassword, newPassword, newPasswordAgain) {
    if (isNull(oldPassword) || oldPassword.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("请输入原密码！");
        return false;
    }

    if (isNull(newPassword) || newPassword.trim().length < 1 || isNull(newPasswordAgain) || newPasswordAgain.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("新密码不能为空！");
        return false;
    }
    if (oldPassword == newPassword) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("新旧密码不能一致！");
        return false;
    }
    if (newPassword != newPasswordAgain){
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("新密码必须相同！");
        return false;
    }
    if (!validPassword(newPassword)) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("密码最少6位，最多20位字母或数字、特殊字符的组合");
        return false;
    }
    return true;
}
