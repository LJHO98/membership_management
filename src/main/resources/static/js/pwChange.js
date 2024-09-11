$(function(){
    $(".btn-zipCode").click(function(){ // 주소 검색 클릭
            addrSearch();
    });

    $("#sendBtn").click(function(){
        $(".emailCodeField").css('display', 'block');
    });


});
}