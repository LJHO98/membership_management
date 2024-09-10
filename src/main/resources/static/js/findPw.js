$(function(){
    $("#sendBtn").click(function(){
        if($("#email").val() != ''){
            sendNumber();
        }else{
            alert("이메일을 입력해주세요");
        }
    });
});

//인증코드 전송
function sendNumber(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $("#mail_number").css("display", "block");
        $.ajax({
            url:"/mail",
            type:"post",
            dataType:"json",
            data:{"mail" : $("#email").val()},
            beforeSend : function(xhr){
                        xhr.setRequestHeader(header, token);
            },
            success:function(data){
                alert("인증번호 발송");
                //인증번호 확인버튼 활성화
                $("#confirmBtn").click(function(){
                        confirmNumber();
                });
                $("#Confirm").attr("value", data);
            },
            error:function(){
                alert("인증번호 발송실패");
                console.log($("#email").val());
            }

        });
}

//인증번호 확인
function confirmNumber(){
    var number1 = $("#emailCode").val();
    var number2 = $("#Confirm").val();

    if(number1 == number2 && number1 != '' && number2 != ''){
        alert("인증성공");
        $("#find-pw-bt").on('click', function(){
            alert("임시 비밀번호 발급");
            findPw();
        });
    }else{
        alert("인증실패");
        $("#find-pw-bt").off('click');
    }
}

//비밀번호 찾기
function findPw(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
    $.ajax({
                url:"/findPw",
                type:"post",
                dataType:"json",
                data:{"email" : $("#email").val()},
                beforeSend : function(xhr){
                            xhr.setRequestHeader(header, token);
                },
                success:function(data){
                    alert("해당 이메일로 임시 비밀번호 전송");
                    location.href="/member/signIn";

                },
                error:function(){
                    alert("이메일 발송실패");
                }
    });

}