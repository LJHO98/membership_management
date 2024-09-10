$(function(){
    $("#sendBtn").click(function(){
            if($("#email").val() != ''){
                sendNumber();
            }else{
                alert("이메일을 입력해주세요");
            }

    });
});

//인증 번호 전송
function sendNumber(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
        $.ajax({
            url:"/mail",
            type:"post",
            dataType:"text",
            data:{"email" : $("#email").val()},
            beforeSend : function(xhr){
                        xhr.setRequestHeader(header, token);
            },
            success:function(data){
                alert(data);
                //인증번호 확인버튼 활성화
                $("#confirmBtn").click(function(){
                confirmCode();
                });
            },
            error:function(){
                alert("인증번호 발송실패");
            }

        });
}

//인증 번호 확인
function confirmCode(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
        $.ajax({
            url:"/verifyCode",
            type:"post",
            dataType:"text",
            data:{"emailCode" : $("#emailCode").val(),
                  "email" : $("#email").val()},
            beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
            },
            success:function(data){
                alert(data);
                $("#find-pw-bt").on('click', function(){
                    findPw();
                });
            },
            error: function(xhr, status, error) {
            // 오류 발생 시 상세 정보 출력
                alert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                $("#find-pw-bt").off('click');
            }


        });
}

//임시 비밀번호 전송
function findPw(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $.ajax({
                url:"/findPw",
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
                error:function(xhr, status, error){
                    alert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                }
    });

}