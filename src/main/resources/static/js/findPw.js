$(function(){
    $("#sendBtn").click(function(){
        //이메일 정규 표현식 정의
        var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        //이메일 input 태그의 value값이 이메일 형식과 일치할 경우
        if(regex.test($("#email").val())){
            sendNumber();

        } else {
            showAlert("이메일을 입력해주세요"); // alert 대신 showAlert 호출
        }
    });
});

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
                // "인증번호 발급" 버튼 클릭 이벤트 핸들러
                clearInterval(countdown);
                seconds = 180; // 3분

                updateCountdown();
                // 1초마다 카운트다운 업데이트
                countdown = setInterval(updateCountdown, 1000);

            },
            error:function(xhr, status, error){
                showAlert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);

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
                showAlert(data);
                $("#find-pw-bt").on('click', function(){
                    findPw();
                });
            },
            error: function(xhr, status, error) {
            // 오류 발생 시 상세 정보 출력
                showAlert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                $("#find-pw-bt").off('click');
            }


        });
}




    //인증코드 유효시간 표시
    // 시간을 업데이트하고 화면에 표시하는 함수
    let seconds; // 남은 시간 변수
    let countdown; // 카운트다운을 관리하는 변수

    const updateCountdown = function() {


        if (seconds >= 0) {
            const minutes = Math.floor(seconds / 60);
            const remainingSeconds = seconds % 60;
            $(".time").text(`${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`);
            seconds--;
        } else {
            clearInterval(countdown);
            showAlert('인증번호 유효시간이 만료되었습니다.');
        }
    };

const showAlert = (message) => {
    const alertBox = $("#alert-box");
    alertBox.text(message);
    alertBox.show();

    setTimeout(function(){
        alertBox.fadeOut();
    }, 3000); // 3초 후에 메시지 사라짐
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
                    showAlert(data);
                    location.href="/member/signIn";

                },
                error:function(xhr, status, error){
                    showAlert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                }
    });

}