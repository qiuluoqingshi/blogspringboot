$(function () {
    if($('#shiro-principal').text() != ''){
        window.location.href="../user/background";
    }
})
$("#btn_submit").click(function () {
    var userAccountVo = {}
    var userId =  $("#userId").val()
    var userPassword = $("#userPassword").val()
    var verifyCode = $("#verifyCode").val()
    if(userId==''&&userPassword==''&&verifyCode=='')
    {
        $("#verifyResult").text('输入不能留空')
        return
    }
    var rememberMe = $("#rememberMe").is(':checked')
    userAccountVo['userId'] = userId
    userAccountVo['userPassword'] = userPassword
    userAccountVo['rememberMe'] = rememberMe
    userAccountVo['verifyCode'] = verifyCode
    $.ajax({
        type:"POST",
        url:"../login",
        dataType:"json",
        contentType:"application/json", // 指定这个协议很重要
        data:JSON.stringify(userAccountVo),
        success:function (res) {
            if(res == 1) {
                self.location=document.referrer
            }
            else if (res == 0){
                $("#verifyResult").text('验证码错误')
            }else if (res == -1){
                $("#verifyResult").text('账号不存在或者错误')
            }else if (res == -2){
                $("#verifyResult").text('密码错误')
            } else if (res == -3){
                $("#verifyResult").text('该账号已被封禁，请联系开发者解禁')
            }
            var img = document.getElementById("code");
            img.src = "/page/getVerifyCode?rnd=" + Math.random();
        }
    })
})
$("#rgt_submit").click(function () {
    window.location.href="/page/register";
})
function freshVerifyCode() {
    var img = document.getElementById("code");
    img.src = "/page/getVerifyCode?rnd=" + Math.random();
}