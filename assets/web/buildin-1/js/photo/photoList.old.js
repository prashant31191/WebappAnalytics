var container = $('#setsContainer'),
columnWidth = 240,
waterfall = new $.Waterfall({
    container: container,
    columnWidth : columnWidth
}),
tmpl = function(data){
    var width = 230,
        height = width * (data.height_o / data.width_o);
    return '<div class="pin">'+
            '<a href="'+data.link+'" title="'+data.title+'" class="pin-content">'+
            '<img src="'+data.src+'" style="width:'+width+'px; height:'+height+'px;" onload="$(this).addClass(\'loaded\')" />'+
            '<div class="pin-desc">'+
                '<span>'+data.title+'</span>'+
            '</div>'+
            '</a>'+
            '</div>';
};
if(setsInfo){
    $.each(setsInfo, function(id, obj){
        var item = $(tmpl(obj));
        waterfall.append(item);
    });
}

$(window).on('DOMContentLoaded orientationchange resize', function(){
    waterfall.resize();
});
$('#wrapper').on('dragstart', function(e){
    e.preventDefault();
});