var extraImageCount=0;
dropdownBrands =$("#brands");
dropdownCategories=$("#category");

$(document).ready(function (){

    $("#shortDescription").richText();
    $("#longDescription").richText();

    $("#brands").change(function(){
        dropdownCategories.empty();
        getCategories();
    });

    getCategories();
});

function getCategories(){
    brandId=dropdownBrands.val();

    url=brandsmoduleURL + "/" + brandId +"/categories";

    $.get(url,function(responJson){
        $.each(responJson, function (index, category) { 
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        });
    });
}