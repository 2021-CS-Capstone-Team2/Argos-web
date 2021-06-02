    $.ajax({
        url: "/exam/getExamCalendar",
        type: "post",
        dataType: "json",
		success : function(data) {
			! function(l) {
			    "use strict";
			    
			    var check = $("#lang").val();
			    function e() {
			        this.$body = l("body"), this.$modal = l("#event-modal"), this.$calendar = l("#calendar"), this.$formEvent = l("#form-event"), this.$btnNewEvent = l("#btn-new-event"), this.$btnDeleteEvent = l("#btn-delete-event"), this.$btnSaveEvent = l("#btn-save-event"), this.$modalTitle = l("#modal-title"), this.$calendarObj = null, this.$selectedEvent = null, this.$newEventData = null
			    }
			    e.prototype.onEventClick = function(e) {
			    	var examDate = new Date(e.event.start);
			    	var examTime = examDate.toLocaleString();
			        this.$formEvent[0].reset(), this.$formEvent.removeClass("was-validated"), this.$newEventData = null, this.$btnDeleteEvent.show(), this.$modalTitle.text("Edit Event"), this.$modal.modal({
			            backdrop: "static"
			        }), this.$selectedEvent = e.event, l("#event-title").text(this.$selectedEvent.title), l("#event-category").text(examTime)
			    }, e.prototype.init = function() {
			        var e = new Date(l.now());
			        var t = [{
			        	
			        }], a = this;
			        var type;
			        var classNm;
			        var title;
			        for(var ct = 0; ct < data.length; ct++){
			        	type = data[ct].examEntity.type; 
			        	
			        	if(type === "MIDTERM"){
			        		classNm = "bg-warning";
			        	} else if(type === "FINAL") {
			        		classNm = "bg-success";
			        	} else {
			        		classNm = "bg-info";
			        	}
			        	if(check == 'ko') {
			        		title = data[ct].classEntity.nameKo + " (" + type + ")";
			        	} else {
			        		title = data[ct].classEntity.nameEn + " (" + type + ")";			        		
			        	}
			        	
			        	var dt = {
				                title: title,
				                start: new Date(data[ct].examEntity.examDate),
				                end: new Date(data[ct].examEntity.examDate),
				                className: classNm
				        }
			        	t.push(dt);
			        }
			        
			        a.$calendarObj = new FullCalendar.Calendar(a.$calendar[0], {
			            slotDuration: "00:15:00",
			            slotMinTime: "08:00:00",
			            slotMaxTime: "19:00:00",
			            themeSystem: "bootstrap",
			            bootstrapFontAwesome: !1,
			            buttonText: {
			                today: "Today",
			                month: "Month",
			                week: "Week",
			                day: "Day",
			                list: "List",
			                prev: "Prev",
			                next: "Next"
			            },
			            initialView: "dayGridMonth",
			            handleWindowResize: !0,
			            height: l(window).height() - 200,
			            headerToolbar: {
			                left: "prev,next today",
			                center: "title",
			                right: "dayGridMonth,timeGridWeek,listMonth"
			            },
			            initialEvents: t,
			            editable: !1,
			            droppable: !0,
			            selectable: !0,
			            eventClick: function(e) {
			                a.onEventClick(e)
			            }
			        }), a.$calendarObj.render(), a.$formEvent.on("submit", function(e) {
			            e.preventDefault();
			            var t, n = a.$formEvent[0];
			            n.checkValidity() ? (a.$selectedEvent ? (a.$selectedEvent.setProp("title", l("#event-title").text()), a.$selectedEvent.setProp("classNames", [l("#event-category").val()])) : (t = {
			                title: l("#event-title").text(),
			                start: a.$newEventData.date,
			                allDay: a.$newEventData.allDay,
			                className: l("#event-category").val()
			            }, a.$calendarObj.addEvent(t)), a.$modal.modal("hide")) : (e.stopPropagation(), n.classList.add("was-validated"))
			        }), l(a.$btnDeleteEvent.on("click", function(e) {
			            a.$selectedEvent && (a.$selectedEvent.remove(), a.$selectedEvent = null, a.$modal.modal("hide"))
			        }))
			    }, l.CalendarApp = new e, l.CalendarApp.Constructor = e
			}(window.jQuery),
			function() {
			    "use strict";
			    window.jQuery.CalendarApp.init()
			}();
		},
		error : function(e) {
			common.alert("관리자에게 문의하세요.");
		}
    });

