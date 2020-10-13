$(function () {
    if($('#shiro-principal').text() != ''){
        window.location.href="../user/background";
    }
})

$("#lg_submit").click(function () {
    window.location.href="/page/login";
})

$("#rgt_submit").click(function () {

    if (validUserMessageForUpdate()){
        alert(1111)
        $("#rgt_submit").attr("disabled","true")
        var userAccountVo = {}
        userAccountVo['userId']  = $('#userId').val();
        userAccountVo['userName']  = $('#userName').val();
        userAccountVo['userPassword']  = $('#userPassword').val();
        userAccountVo['verifyCode']  = $('#verifyCode').val();
        $.ajax({
            type: "POST",
            url: "/user/insertNewUserAccount",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(userAccountVo),
            success: function(res){
                if(res != null && res.resultCode == 200){
                    swal({
                        title: '注册成功！',
                        text: res.resultMessage,
                        icon: "success",
                        buttons: {
                            confirm: {	//确认按钮
                                text: '确认',
                                value: true,
                                visible: true
                            }
                        },
                    }).then(function(res){
                        if(res){
                            $("#rgt_submit").removeAttr("disabled")
                            window.location.href = "/page/login" ;
                        }
                    })
                }else if(res != null && res.resultCode == 500){
                    $("#verifyResult").text(res.resultMessage)
                    $("#rgt_submit").removeAttr("disabled")
                }else {
                    $("#verifyResult").text("产生未知错误，请联系管理员或者开发者")
                    $("#rgt_submit").removeAttr("disabled")
                }
                var img = document.getElementById("code");
                img.src = "/page/getVerifyCode?rnd=" + Math.random();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
                alert(XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
                var img = document.getElementById("code");
                img.src = "/page/getVerifyCode?rnd=" + Math.random();
            }
        })
    }else{
        var img = document.getElementById("code");
        img.src = "/page/getVerifyCode?rnd=" + Math.random();
    }
})

function validUserMessageForUpdate() {
    var userId = $('#userId').val();
    var userName = $('#userName').val();
    var userPassword = $('#userPassword').val();
    var userPasswordVf = $('#userPasswordVf').val();
    var verifyCode = $('#verifyCode').val();
    if ((isNull(userId) || userId.trim().length < 1)
        || (isNull(userName) || userName.trim().length < 1)
        || (isNull(userPassword) || userPassword.trim().length < 1)
        || (isNull(userPasswordVf) || userPasswordVf.trim().length < 1)
        || (isNull(verifyCode) || verifyCode.trim().length < 1)) {
        $("#verifyResult").text('以上信息必须全部填写')
        return false;
    }
    if (!validUserId(userId)) {
        $("#verifyResult").text('6到16位的字母和数字账号！')
        return false;
    }
    if (!validCN_ENString2_18(userName)) {
        $("#verifyResult").text('2-18位的中英文昵称！')
        return false;
    }
    if (!validPassword(userPassword)) {
        $('#verifyResult').css("display", "block");
        $('#verifyResult').html("密码最少6位，最多20位字母或数字、特殊字符的组合");
        return false;
    }
    if (userPassword != userPasswordVf){
        $("#verifyResult").text('确认两次密码必须相同！')
        return false;
    }
    return true;
}