function addNextDetailSection(){
    allDivDetails= $("[id^='divDetail']");
    divDetailsCount=allDivDetails.length;

    htmlDetailSection =`
        <div class="form-inline" id="divDetails${divDetailsCount}">
            <label class="m-3">Name: </label>
            <input type="text" class="form-control w-25" name="detailNames" maxlength="255" />

            <label class="m-3">Value: </label>
            <input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
        </div>
    `;

    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection =allDivDetails.last();
    previousDivDetailSection = previousDivDetailSection.attr("id");
    
    htmlLinkRemove=`
        <a class="btn fas fa-times-circle fa-2x icon-dark"
        href="javascript:removeDetailSectionById('${previousDivDetailSection}')"></a>
    `;
    
    previousDivDetailSection.append(htmlLinkRemove);
}

function removeDetailSectionById(id){
    alert(id);
    $("#" +id).remove();
}