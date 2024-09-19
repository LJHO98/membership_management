$(function(){
    $("#sendBtn").click(function(e){
            if($("#email").val() != ''){
                sendNumber();
                // "인증번호 발급" 버튼 클릭 이벤트 핸들러
                e.preventDefault();

                clearInterval(countdown);
                seconds = 60; // 3분(180초)

                updateCountdown();
                // 1초마다 카운트다운 업데이트
                countdown = setInterval(updateCountdown, 1000);


            }else{
                alert("이메일을 입력해주세요");
            }

    });
});

const showAlert = (message) => {
    setTimeout(function(){
        alert(message);
    }, 0);
}



//인증 번호 전송
function sendNumber(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
        $.ajax({
            url:"/check/mail",
            type:"post",
            dataType:"text",
            data:{"email" : $("#email").val()},
            beforeSend : function(xhr){
                        xhr.setRequestHeader(header, token);
            },
            success:function(data){
                showAlert(data);
                //인증번호 확인버튼 활성화
                $("#confirmBtn").click(function(){
                confirmCode();
                });
            },
            error:function(xhr, status, error){
                alert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
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

        //인증코드 유효기간

    let seconds; // 남은 시간 변수
    let countdown; // 카운트다운을 관리하는 변수

    // 시간을 업데이트하고 화면에 표시하는 함수
    const updateCountdown = function() {
        if (seconds >= 0) {
            const minutes = Math.floor(seconds / 60);
            const remainingSeconds = seconds % 60;
            $(".time").text(`${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`);
            seconds--;
        } else {
            clearInterval(countdown);
            alert('인증번호 유효시간이 만료되었습니다.');
        }
    };




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