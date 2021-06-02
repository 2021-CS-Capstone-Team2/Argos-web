/**
 * 대시보드 실시간 데이터 JS
 * 
 * @since 2021.05.03
 * @author swkim
 */

$(document).ready(function(){
	getEventTimeLine();
}); 

function getEventTimeLine() {
	var dto = [[(${dto})]];
	console.log(dto)
	$.ajax({
		url: "/dashboard/getEventTimeLine",
		data: "",
		type: "POST",
		dataType: "json",
		success: function(json) {
			console.log(json)
		}
	});
	
	timer = setInterval(function() {
		$.ajax({
			url: "/dashboard/getEventTimeLine",
			data: "",
			type: "POST",
			dataType: "json",
			success: function(json) {
				console.log(json)
			}
		});
	}, 2000); //2초마다 반영 	

}	
/*	
    $.ajax({
        url: "/exam/editExam",
        data: $("#editExamForm").serialize(),
        type: "patch",
        dataType: "json",
		success : function(data) {
			if(data.result == "SUCCESS"){
				common.alert([[(${#messages.msg('common.message.saveSuccess')})]]);
			} else if(data.result == "DIFFERENT"){
				common.alert([[(${#messages.msg('common.message.againLogin')})]]);
			} else if(data.result == "EMPTY"){
				common.alert([[(${#messages.msg('common.message.notExistsClass')})]]);
			} else{
				common.alert([[(${#messages.msg('common.message.contactManager')})]]);
			}
			document.location.href = "/exam/getExamMgmt";
		},
		error : function(e) {
			common.alert([[(${#messages.msg('common.message.contactManager')})]]);
		}
    });
	
$.ajax({
		url: "/dashboard/getLoginQueueCount",
	type: "GET",
	dataType: "json",
	success: function(count){
	$('#queueCount').html(count);
	}
})
*/
