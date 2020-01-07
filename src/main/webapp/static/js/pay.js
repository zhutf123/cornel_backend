(function(){
    // $(".radio").change(function(){
    //     $('.pay-type ul li').removeClass("payon");
    //     $(":checked").parent().addClass("payon");
    // });
    $('.pay-type ul li').click(function(){
        $('.pay-type ul li').removeClass("payon");
        $(this).addClass("payon");
        $(this).find('input').prop('checked', true);
    })
})()