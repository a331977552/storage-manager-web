function initReminder(basePath)
{

	context=basePath+"product/stockreminder".replace("\\","");
	$.ajax({
		   url:  context,
		   type: 'get',
		   success: function (result) {
			  
		 	if(result.code==200){
		 		if(result.result.length>0){		 			
		 			$("#span_reminder").addClass("label-warning").text(result.result.length+"");
		 			var html="";
		 			for(var index in result.result){
		 				var product=result.result[index];
		 				html+="<li>";		 				
		 				html+="<a href=\"#\">"
		 				html+="<i class=\"fa fa-users text-aqua\"></i> "+product.name+" has "+product.quantity+" left";
		 				html+="</a>";
		 				html+="</li>";
		 			}
		 			$("#li_title").text("You have "+result.result.length+" notifications");
		 			$("#ul_reminder_menu").html(html);
		 		}
		 	}else{
		 		$("#li_title").text("error "+result.msg);		 	
		 	}
		   },
		   error:function(error,error1,error2){
			   $("#li_title").text("error "+error);		
		   },
});

}



