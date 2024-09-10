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


});

function joinShow(){
            $(".stage_arrow").fadeIn(1500);
            $(".stage_b").fadeIn(1500);
            $("#terms").fadeOut(1000);
            $("#joinForm").fadeIn(2000);

}

$(function(){
    $("#sendBtn").click(function(){
        if($("#email").val() != ''){
            sendNumber();
        }else{
            alert("이메일을 입력해주세요");
        }
    });
});

function sendNumber(){
    var token = $("meta[name=_csrf]").attr("content");
    var header = $("meta[name=_csrf_header]").attr("content");
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
            }

        });
}

//인증번호 확인
function confirmNumber(){
    var number1 = $("#emailCode").val();
    var number2 = $("#Confirm").val();

    if(number1 == number2 && number1 != '' && number2 != ''){
        alert("인증성공");
        $("#btn-join").attr("type", "submit");
    }else{
        alert("인증실패");
    }
}
