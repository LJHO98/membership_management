$(function(){

    // 모든 동의 체크
    $("#chk_all").click(function(){
        if( $(this).prop('checked') ){ //체크 상태
            $("#chk1").prop('checked',true);
            $("#chk2").prop('checked',true);
            $("#chk3").prop('checked',true);
        } else{
             $("#chk1").prop('checked',false);
             $("#chk2").prop('checked',false);
             $("#chk3").prop('checked',false);
        }
    });

    // 각 동의 체크 해제 또는 체크 표시에 따른 모든 동의 체크 상태

    $(".chk").click(function(){
        if( $(this).prop('checked') ){
            var isAll = true;
            $.each( $(".chk"), function(){
                if( !$(this).prop('checked') )
                    isAll = false;
            });
            if( isAll ) $("#chk_all").prop('checked',true);
        }else{
            $("#chk_all").prop('checked',false);
        }
    });

    let agree_pass=false;
    $.each( $(".fieldError"), function(){
        if( $(this).text() != '') agree_pass=true
    });
    if(agree_pass) joinShow();

    // 동의 버튼 클릭시 - 회원가입으로 이동 단, 필수는 모두 체크 되어야 한다.
    $(".btn_agree").click(function(){
        if( $("#chk1").prop('checked') && $("#chk2").prop('checked') ){
            joinShow();
        }else{
            alert("필수 항목을 동의 하셔야 합니다.")
        }
    });

    $(".btn-zipCode").click(function(){ // 주소 검색 클릭
        addrSearch();
    });

    $("#sendBtn").click(function(){
            //이메일 정규 표현식 정의
            var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
            //이메일 input 태그의 value값이 이메일 형식과 일치할 경우
            if(regex.test($("#email").val())){
                $(".emailCodeField").css('display', 'block');
                $("#sendBtn").prop('disabled',true);
                sendNumber();
            }else{
                alert("이메일을 입력해주세요");
            }

    });

});

function joinShow(){
            $(".stage_arrow").fadeIn(1500);
            $(".stage_b").fadeIn(1500);
            $("#terms").fadeOut(1000);
            $("#joinForm").fadeIn(2000);

}

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
                $("#btn-join").attr("type", "submit");
            },
            error: function(xhr, status, error) {
            // 오류 발생 시 상세 정보 출력
                alert(xhr.responseText + "\n상태: " + status + "\n에러: " + error);
                $("#sendBtn").prop('disabled',false);
            }


        });
}

