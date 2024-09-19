$(function(){
    $(".btn-zipCode").click(function(){ // 주소 검색 클릭
            addrSearch();
    });

    $("#sendBtn").on('click.send', function(){
        //이메일 정규 표현식 정의
        var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        //이메일 input 태그의 value값이 이메일 형식과 일치할 경우
        if(regex.test($("#email").val())){
            $(".emailCodeField").css('display', 'block');
            $("#sendBtn").prop('disabled',true);
            sendNumber();
        }else{
            showAlert("이메일을 입력해주세요");
        }
    });



});

function addrSearch(){
    new daum.Postcode({
        oncomplete: function(data) {
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraRoadAddr !== ''){
            extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            //검색결과에서 우편번호와 도로명주소 가져와서 input에 넣기
            document.getElementById("zipCode").value=data.zonecode;
            document.getElementById("addr1").value=roadAddr;

        }
    }).open();
}

//인증 번호 전송
function sendNumber(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
        $.ajax({
            url:"/updateCheck/mail",
            type:"post",
            dataType:"text",
            data:{"email" : $("#email").val(),
                  "checkEmail" : $("#checkEmail").val()
            },
            beforeSend : function(xhr){
                        xhr.setRequestHeader(header, token);
            },
            success:function(data){
                showAlert(data);
                //인증번호 확인버튼 활성화
                $("#verifyBtn").on('click', function(){
                    confirmCode();

                });
                // "인증번호 발급" 버튼 클릭 이벤트 핸들러
                clearInterval(countdown);
                seconds = 180; // 3분

                updateCountdown();
                // 1초마다 카운트다운 업데이트
                countdown = setInterval(updateCountdown, 1000);
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 상세 정보 출력
                showAlert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                $("#sendBtn").prop('disabled',false);
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
            $("#btn-join").attr("type", "submit");
        },
        error: function(xhr, status, error) {
        // 오류 발생 시 상세 정보 출력
            showAlert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
            $("#sendBtn").prop('disabled',false);
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
            alert('인증번호 유효시간이 만료되었습니다.');
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
