$(function(){
    $("#find-id-bt").click(function(){
        findId();
    });
});
function findId(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $.ajax({
                url:"/findId",
                type:"post",
                dataType:"text",
                data:{"email" : $("#email").val()},
                beforeSend : function(xhr){
                            xhr.setRequestHeader(header, token);
                },
                success:function(data){
                    alert(data);
                    location.href="/member/signIn";

                },
                error:function(){
                    alert("이메일 발송실패");
                }
    });

}