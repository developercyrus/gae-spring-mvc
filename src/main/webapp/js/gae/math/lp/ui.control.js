$(document).ready(function() {
    var columnNo;
    var constraintID = 0;
    var jsonData;
    
    $("#example-link").click(function() {
        $('.example').toggle();
    });
    
    $("#varNo").change(function(){
        columnNo = $("#varNo option:selected").attr("value");
        var $tbl = $("<table>").attr("id", "basicTable");  

        var $objFunction = $("<tr id='objFunction'>");
        $objFunction.append(
            $("<td>").append(
                $("<select>").attr("name", "function.goal").append(
                    $("<option>").attr("value", "MINIMIZE").text("MINIMIZE")
                ).append(
                    $("<option>").attr("value", "MAXIMIZE").text("MAXIMIZE")
                )
            )
        );
        for (var i = 0; i < columnNo; i++) {
            $objFunction.append(
                $("<td>").append(
                    $("<input type='text'>").attr("name", "function.variables["+i+"]")
                ).append(
                    $("<label>").text("x").append($("<sub>").text(i))                           
                ).append(
                    $("<span>").text("+")                                
                )
            );
        }
        
        $objFunction.append(
            $("<td>").append(
                $("<input type='text'>").attr("name", "function.constant")
            )
        );

        var $constraint = $("<tr>");
        $constraint.attr("id", "constraint"+constraintID);
        $constraint.append(
            $("<td>").append(
                $("<span>").text("subject to")
            )
        )
        for (var i = 0; i < columnNo; i++) {
            if (i != columnNo - 1) {
                $constraint.append(
                    $("<td>").append(
                        $("<input type='text'>").attr("name", "constraints["+constraintID+"].variables["+i+"]")
                    ).append(
                        $("<label>").text("x").append($("<sub>").text(i))                           
                    ).append(
                        $("<span>").text("+")                                
                    )
                );
            }
            else {
                $constraint.append(
                    $("<td>").append(
                        $("<input type='text'>").attr("name", "constraints["+constraintID+"].variables["+i+"]")
                    ).append(
                        $("<label>").text("x").append($("<sub>").text(i))                                   
                    )
                );
            }
        }
        
        $constraint.append(
            $("<td>").append(
                $("<select>").attr("name", "constraints["+constraintID+"].relationship").append(
                    $("<option>").attr("value", "EQ").text("=")
                ).append(
                    $("<option>").attr("value", "LEQ").text("\u2264")
                ).append(
                    $("<option>").attr("value", "GEQ").text("\u2265")
                )
            )
        );
        
        $constraint.append(
            $("<td>").append(
                $("<input type='text'>").attr("name", "constraints["+constraintID+"].constant")
            )
        );
        
        $("#equation").empty();
        $("#returned").empty();
        $("#solDetail").empty();
        
        $("#equation").append($tbl.append($objFunction).append($constraint));
        
        
        $("#control").show();
    });
    
    
    $("#addMore").click(function() {   
        constraintID++;
        
        var $tbl = $("#basicTable");
        
        var $constraint = $("<tr>");
        $constraint.attr("id", "constraint"+constraintID);
        $constraint.append(
            $("<td>").append(
                $("<span>").text("")
            )
        )
        for (var i = 0; i < columnNo; i++) {
            if (i != columnNo - 1) {
                $constraint.append(
                    $("<td>").append(
                        $("<input type='text'>").attr("name", "constraints["+constraintID+"].variables["+i+"]")
                    ).append(
                        $("<label>").text("x").append($("<sub>").text(i))                           
                    ).append(
                        $("<span>").text("+")                                
                    )
                );
            }
            else {
                $constraint.append(
                    $("<td>").append(
                        $("<input type='text'>").attr("name", "constraints["+constraintID+"].variables["+i+"]")
                    ).append(
                        $("<label>").text("x").append($("<sub>").text(i))                                   
                    )
                );
            }
        }
        
        $constraint.append(
            $("<td>").append(
                $("<select>").attr("name", "constraints["+constraintID+"].relationship").append(
                    $("<option>").attr("value", "EQ").text("=")
                ).append(
                    $("<option>").attr("value", "LEQ").text("\u2264")
                ).append(
                    $("<option>").attr("value", "GEQ").text("\u2265")
                )
            )
        );
        
        $constraint.append(
            $("<td>").append(
                $("<input type='text'>").attr("name", "constraints["+constraintID+"].constant")
            )
        );
        
        $constraint.append(
            $("<td>").append(
                $("<a class='constraint-link' onClick=\"remove('constraint"+constraintID+"')\" href='#'>[x]</a>")                       
            )
        );
        
        $tbl.append($constraint);                
    });
    
    $(document).ajaxStart(function() {
        $("#returned").html("");
        $("#solDetail").html("");
        $.blockUI({ message: '<img style="vertical-align:middle;" src="pic/gae/math/lp/ajax-loader.gif" /><div style="padding-left:5px; font-family:Arial; font-size: 12px">Loading ...</div>' }); 
    });
    
    $(document).ajaxSuccess(function() {
        
    });

    $(document).ajaxComplete(
        $.unblockUI
    );

    $("#calculate").click(function() {
        var formData = form2object("param");  
        var jsonData = JSON.stringify(formData, null, "\t");   
        //alert(jsonData);

        jQuery.ajax({  
            type : 'POST',  
            contentType : 'application/json',  
            url : 'lpout.do',   
            data : jsonData,  
            dataType : 'json',  
            success : function(data){  
                //$("#returned").html(data);
                $.each(data.solutions, function(i, item) {
                    $("#solDetail").append(
                        $("<label>").text("x").append($("<sub>").text(i))                           
                    ).append(
                        $("<span>").text("="+item)                              
                    ).append(
                        $("<br/>")    		
                    )
                    //alert(item);
                    
                });
            },  
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
                alert(XMLHttpRequest.responseText);
                alert(errorThrown);
            }
        });  
    });
});

function remove(constraintIDr) {
    //alert("#"+constraintIDr);
    $("#"+constraintIDr).remove()
}