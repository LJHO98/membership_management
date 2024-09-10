$(function(){
    $("#find-id-bt").click(function(){
        alert("아이디 찾기");
        findId();
    });
});
function findId(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $.ajax({
                url:"/findId",
                type:"post",
                dataType:"json",
                data:{"email" : $("#email").val()},
                beforeSend : function(xhr){
                            xhr.setRequestHeader(header, token);
                },
                success:function(data){
                    alert("해당 이메일로 아이디 전송");
                    location.href="/member/signIn";

                },
                error:function(){
                    alert("이메일 발송실패");
                }
    });

}