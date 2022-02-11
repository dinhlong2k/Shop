    var extraImageCount=0;
    dropdownBrands =$("#brands");
    dropdownCategories=$("#category");

    $(document).ready(function (){

        $("#shortDescription").richText();
        $("#longDescription").richText();
        
        $("#buttonCancel").on("click",function(){
            window.location ="[[@{/product}]]";
        });

        $("#logoutLink").on("click",function (e){
            e.preventDefault();
            document.logoutForm.submit();
        });

        $("#brands").change(function(){
            dropdownCategories.empty();
            getCategories();
        });

        $("#fileImage").change(function (){
            fileSize=this.files[0].size;
            if(fileSize > 5048576){
                this.setCustomValidity("You must choose an image less than 1MB");
                this.reportValidity();
            }else{
                this.setCustomValidity("");
                showImageThumbnail(this);
            }

        });

        $("input[name='extraImage']").each(function(index){
            extraImageCount++;
            $(this).change(function(){
                showExtraImage(this,index);
            });
        });

        getCategories();
    });

    function showImageThumbnail(fileInput){
        var file= fileInput.files[0];
        var reader=new FileReader();
        reader.onload =function (e){
            $("#thumbnail").attr("src",e.target.result);
        };
        reader.readAsDataURL(file);
    }

    function showExtraImage(fileInput,index){
        var file= fileInput.files[0];
        var reader=new FileReader();
        reader.onload =function (e){
            $("#extraThumbnail" +index).attr("src",e.target.result);
        };
        reader.readAsDataURL(file);

        if(index>= extraImageCount -1){
            addExtraImageSection(index +1);
        }
    }

    function addExtraImageSection(index){
        html =`
        <div class="col border m-3 p2" id="divExtraImage${index}">
            <div id="extraImageHeader${index}"><label>Extra image #${index +1}: </label></div>
            <div class="m-2">
                <img id="extraThumbnail${index}" alt="Extra image preview" class="img-fluid" src="${defaultImageThumbnailSrc}" width="250px" height="250px">
            </div>
            <div>
                <input type="file" name="extraImage"
                onchange="showExtraImage(this,${index})"
                accept="image/png, image/jpeg">
            </div>
        </div>
        `;

        htmlLinkRemove=`
            <a class="btn fas fa-times-circle fa-2x icon-dark float-right"
            href="javascript:removeExtraImage(${index-1})"></a>
        `;

        $("#divProductImages").append(html);

        $("#extraImageHeader" + (index -1)).append(htmlLinkRemove);

        extraImageCount++;
    }

    function removeExtraImage(index){
        $("#divExtraImage" +index).remove();
        extraImageCount --;
    }

    function getCategories(){
        brandId=dropdownBrands.val();

        url=brandsmoduleURL + "/" + brandId +"/categories";

        $.get(url,function(responJson){
            $.each(responJson, function (index, category) { 
                $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
            });
        });
    }

    function checkName(form){
        url ="[[@{/product/checkUniqueNew}]]";

        nameProduct =$("#name").val();
        csrfValue =$("input[name='_csrf']").val();
        // params={id:userId, email: userEmail, _csrf: csrfValue};
        params={ name: nameProduct, _csrf: csrfValue};

        $("#modalDialog").modal();

        $.post(url,params,function (response){
        if(response == "OK"){
            form.submit();
        }else if(response =="Duplicated"){
            showModalDiaglog("Warning", "There is another product having the name");
        }else{
                showModalDiaglog("Error", "Unknown response from server         ");
           }
        }).fail(function(){
                showModalDiaglog("Error","Could not connect to the server");
        });
        return false;
    }

    function showModalDiaglog(title,message){
        $("#modalTitle").text(title);
        $("#modalBody").text(message);
        $("#modalDialog").modal();
    }
