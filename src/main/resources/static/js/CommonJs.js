const Common = (function() {
	const _my = {};

	// form 기본 설정
	$("form").attr("onsubmit", "return false;"); // 자동 submit
	$("form").attr("autocomplete", "off"); // 자동완성
	// 유효성 설정
	[...$("form")].forEach(el => {
		$(el).validate({
			errorPlacement: function(error, element) { }
		});
	});
	// 유효성 검사
	_my.valid = function($form, title, ruleList) {
		let formValid = $form.valid();
		let errorList = [];
		
		if (!formValid) {
			let validator = $form.validate();
			errorList = validator.errorList;
		}
		
		// custom rule
		if(_my.isEmpty(ruleList) == false) {
			ruleList.forEach(rule => {
				const result = rule.rules(rule.element);
				
				if(!result.valid) {
					errorList.push(result);
				}
			})
		}
		
		const errors = errorList.map(err => ({
			target: "#" + err.element.id
			, label: (function() {
				const $el = $(err.element);

				let labelText = err.element.name;
				if ($el.attr("validLabel")) { // validLabel을 1순위로 한다.
					labelText = $("label[for='" + $el.attr("validLabel") + "']").text();
				} else if ($el.attr("validText")) { // validText을 2순위로 한다.
					labelText = $el.attr("validText");
				} else if ($("label[for='" + $el[0].id + "']")) {
					labelText = $("label[for='" + $el[0].id + "']").text(); // 일반적인 label
				}

				return labelText;
			})()
			, message: (function() {
				var message = [];
				if (err.method == "required") {
					message.push("필수 값입니다.");
				} else {
					message.push(err.message);
				}

				return message;
			})()
		}));

		return {
			valid: errors.length == 0
			, title: title ?? $("#title").text()
			, errors: errors
		};
	}
	// 유효성 메세지 표시
	_my.showValidDialog = function(validList) {
		validList = validList.filter(function(item) {
			return item.errors.length > 0
		});

		_my.alert({
			type: "error"
			, head: "경고"
			, headIncon: "error"
			, msg: _.template($("#error-template").html())(({ validList }))
		});
	}
	
	// 서버자체에서 에러 리턴한 경우(통신 장애등..) 일반적인 ajax error handling 로직을 차용한다.
	_my.error = function(jqXHR, textStatus, errorThrown) {
		let message;
		let statusErrorMap = {
			'400': "Server understood the request but request content was invalid."
			, '401': "Unauthorised access."
			, '403': "Forbidden resouce can't be accessed"
			, '500': "Internal Server Error."
			, '503': "Service Unavailable"
		};
		//메세지 구성
		if (jqXHR.status) {
			message = statusErrorMap[jqXHR.status];

			if (!message) {
				if(jqXHR.responseText) {
					message = jqXHR.responseText;
				}else {
					message = "Unknow Error";
				}
			}
		} else if (textStatus == 'parsererror') {
			message = "Error.\nParsing JSON Request failed.";

		} else if (textStatus == 'timeout') {
			message = "Request Time out.";

		} else if (textStatus == 'abort') {
			message = "Request was aborted by the server";

		} else {
			message = "Unknow Error \n." + errorThrown;
		}

		_my.alert({
			msg: message
		});
	}

	// ajax post 통신
	_my.ajax = function(param) {
		let toastOption = {};
		if (param.toast) {
			toastOption = {
				type: "save"
				, beforeAlert: true // 통신 전 확인메세지
				, afterAlert: true // 통신 후 완료메세지
			}
			if (typeof param.toast == "string") {
				toastOption.type = param.toast
			} else {
				toastOption = $.extend(toastOption, param.toast);
			}

			if (toastOption.beforeAlert) {
				let msg;
				if (toastOption.type == "save") {
					msg = "저장 하시겠습니까?";
				} else if (toastOption.type == "delete") {
					msg = "삭제 하시겠습니까?";
				}
				toastOption.beforeAlertMsg = msg;
			}
			if (toastOption.afterAlert) {
				let msg;
				if (toastOption.type == "save") {
					msg = "저장 되었습니다."
				} else if (toastOption.type == "delete") {
					msg = "삭제 되었습니다."
				}
				toastOption.afterAlertMsg = msg;
			}
		}

		const doAjax = function() {
			$.ajax({
				type: "POST"
				, dataType: "json"
				, contentType: "application/json; charset=utf-8"
				, url: param.url
				, data: JSON.stringify($.extend({}, window.cmmVO, param.data))
				, beforeSend: function() {
				}
				, success: function(result, textStatus, jqXHR) {
					if (toastOption.afterAlert) {
						_my.alert({
							msg: toastOption.afterAlertMsg
						});
					}
					param.callback(result, textStatus, jqXHR);
				}
				, error: _my.error
			});
		}

		if (toastOption.beforeAlert) {
			_my.alert({
				msg: toastOption.beforeAlertMsg
				, confirmCallback: function() {
					doAjax();
				}
			});
		} else {
			doAjax();
		}
	};
	// fileDownload
	_my.fileDownload = function(options) {
		const a = document.createElement("a");
		a.href = window.cmmVO.CONTEXT_PATH + "/file/download?uldFileNm=" + options.ULD_FILE_NM + "&orgnlFileNm=" + options.ORGNL_FILE_NM;
		a.click();
	}

	// fileUpload
	_my.fileUpload = function(options) {
		$("#fileUpload").removeAttr("multiple");
		$("#fileUpload").removeAttr("accept");

		if (options.multi) {
			$("#fileUpload").attr("multiple", "multiple");
		}
		if (options.accept) {
			$("#fileUpload").attr("accept", options.accept);
		}

		$("#fileUpload").data("options", options);

		$("#fileUpload").val("").trigger("click");
	}
	$("#fileUpload").change(function() {
		let files = this.files;
		if (!files || files.length === 0) return;

		let formData = new FormData();
		for (let i = 0; i < files.length; i++) {
			formData.append("fileUpload", files[i]);
		}

		let $this = $(this);
		let options = $this.data("options"); // 저장된 콜백
		$this.removeData('options'); // 1회용 처리

		if (options.thumb) {
			formData.append("thumb", JSON.stringify(options.thumb));
		}
		if (options.accept) {
			formData.append("accept", options.accept);
		}

		$.ajax({
			url: window.cmmVO.CONTEXT_PATH + "/file/upload"
			, type: 'POST'
			, data: formData
			, processData: false
			, contentType: false
			, success: function(res) {
				if (!options.multi) {
					res = res[0];
				}
				if (typeof options.callback === 'function') {
					options.callback(res); // 콜백 실행
				}
			}
			, error: _my.error
		});
	});
	// 이미지 미리보기
	_my.viewImage = function(param) {
		let infoId = param.uploadInfoId;
		let boxId = param.uploadBoxId;
		let file = param.file;
		var fileInfoList = [
			{ name: "ULD_FILE_NM", value: file.ULD_FILE_NM }
			, { name: "FILE_SZ", value: file.FILE_SZ }
			, { name: "ORGNL_FILE_NM", value: file.ORGNL_FILE_NM }
			, { name: "FILE_EXTN_NM", value: file.FILE_EXTN_NM }
			, { name: "WDTH_SZ", value: file.WDTH_SZ }
			, { name: "VRTC_SZ", value: file.VRTC_SZ }
		];

		$("#" + infoId + " .fileName").text(file.ORGNL_FILE_NM);
		$("#" + infoId + " .fileSize").text(`${file.WDTH_SZ} * ${file.VRTC_SZ}px (${(file.FILE_SZ / 1024).toFixed(1)}KB)`);
		$("#" + infoId + " .imagePreview").attr("src", window.cmmVO.CONTEXT_PATH + "/file/image?path=" + file.ULD_FILE_NM + "." + file.FILE_EXTN_NM);

		// 첨부파일 등록
		fileInfoList.forEach(item => {
			$("#" + infoId).closest("form").append(
				$("<input>", {
					type: "hidden"
					, name: item.name
					, value: item.value
					, "aria-hidden": "true"
					, role: "presentation"
				})
			);

		});

		$("#" + boxId).hide();
		$("#" + infoId).show();

		// 이미지명
		$("#" + infoId + " .fileName").off().on("click", function() {
			Common.fileDownload({
				ULD_FILE_NM: file.ULD_FILE_NM
				, ORGNL_FILE_NM: file.ORGNL_FILE_NM
			});
		});
		// 이미지 삭제
		$("#" + infoId + " .btnDeleteImage").off().on("click", function() {
			$("#" + infoId + " .fileName").text("");
			$("#" + infoId + " .fileSize").text("");
			$("#" + infoId + " .imagePreview").attr("src", "");

			// 첨부파일 정보 삭제
			fileInfoList.forEach(item => {
				$("#" + infoId).closest("form").find(":input[name='" + item.name + "']").remove();
			});

			$("#" + infoId).hide();
			$("#" + boxId).show();
		});
	}
	// 값이 비어있는지 체크한다.
	_my.isEmpty = function(value) {
		if (typeof value === 'undefined' || value === null || value.length === 0) {
			return true;
		}

		value = '' + value;
		return !value.match(/\S/);
	}
	// datepicker 설정(air-datepicker)
	_my.datepicker = function($dateEl, options) {
		if (_my.isEmpty($dateEl) || $dateEl.hasClass("hasDatepicker")) return false;
		if (_my.isEmpty(options)) options = {};

		let _options = {
			autoClose: true
			, view: "days" // 시작 뷰
			, minView: "days" // 가장 낮은 뷰
			, maxView: "days" // 가장 높은 뷰
			, dateFormat: "yyyy-MM-dd" // 출력 포맷
			, position: 'bottom center'
			, keyboardNav: true // 키보드로 조작 가능
			, locale: {
				days: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일']
				, daysShort: ['일', '월', '화', '수', '목', '금', '토']
				, daysMin: ['일', '월', '화', '수', '목', '금', '토']
				, months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
				, monthsShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
				, today: '오늘'
				, clear: '초기화'
				, dateFormat: "yyyy-MM-dd"
				, timeFormat: 'HH:mm'
				, firstDay: 0
			}
			, navTitles: {
				years(dp) {
					let date = dp.viewDate;
					let start = date.getFullYear() - 7;
					let end = date.getFullYear() + 4;
					return `${start}년 - ${end}년`;
				}
				, months(dp) {
					let date = dp.viewDate;
					let year = date.getFullYear();
					return `${year}년`;
				}
				, days(dp) {
					let date = dp.viewDate;
					let year = date.getFullYear();
					let month = date.getMonth() + 1;
					return `${year}년 ${month}월`;
				}
			}
		};

		// 보기 모드
		if (_my.isEmpty(options.view) == false) {
			let view = options.view;
			let format = [];

			switch (view) {
				case "days": format.push("dd")
				case "months": format.push("MM")
				case "years": format.push("yyyy")
			}
			format = format.reverse().join("-");

			_options.view = view;
			_options.minView = view;
			_options.maxView = view;
			_options.dateFormat = format;
			_options.locale.dateFormat = format;
		}


		let dp = new AirDatepicker($dateEl[0], _options);
		$dateEl[0].datepicker = dp;

		// 중복 초기화 방지
		$dateEl.addClass("hasDatepicker");

		// inputmask
		let mask = _options.dateFormat.replace(/\w/g, "9");
		$dateEl.inputmask(mask);

		// input 입력 시 날짜 이동
		$(document).off("change", "#" + $dateEl[0].id)
			.on("change", "#" + $dateEl[0].id, function() {
				let date = new Date($(this).val());

				if (!isNaN(date)) {
					dp.setViewDate(date)
					dp.selectDate(date);
				}
			});

		// input에 값이 입력 되어 있는 경우는 초기 값을 설정해준다.
		if (_my.isEmpty($dateEl.val()) == false) { // datepicker
			let date = moment($dateEl.val(), dp.opts.dateFormat.toUpperCase());
			dp.setViewDate(date)
			dp.selectDate(date);
		}
	}
	// 날짜 기간 유효성 설정
	_my.datepickerRange = function($startEl, $endEl, options) {
		// 달력 선택 이벤트
		$startEl[0].datepicker.opts.onSelect = function(dt) {
			let endDp = $endEl[0].datepicker;
			endDp.update({ ...endDp.ops, minDate: dt.date });
		};
		$endEl[0].datepicker.opts.onSelect = function(dt) {
			let startDp = $startEl[0].datepicker;
			startDp.update({ ...startDp.opts, maxDate: dt.date });
		};

		// 달력 입력 이벤트
		$startEl.add($endEl).off("change").change(function(e) {
			let startStr = $startEl.val();
			let startDp = $startEl[0].datepicker;
			let startDate = new Date(startStr);

			let endStr = $endEl.val();
			let endDp = $endEl[0].datepicker;
			let endDate = new Date(endStr);

			let inputDp = this.datepicker;
			let inputStr = $(this).val()
			let inputDate = new Date(inputStr);

			// 유효한 날짜일 때만 비교
			if (isNaN(startDate)) { // 시작일이 없을때
				startDp.clear();
				endDp.update({ ...endDp.opts, minDate: false });
			} else if (isNaN(endDate)) { // 종료일이 없을때
				endDp.clear();
				startDp.update({ ...startDp.opts, maxDate: false });

			} else if (!isNaN(startDate) && !isNaN(endDate)) { // 날짜가 입력 된 경우 기간을 초과하는지
				if (startDate > endDate) {
					if ($(this).is($startEl)) {
						$startEl.val(endStr);
						startDp.setViewDate(endDate)
						startDp.selectDate(endDate);
					} else {
						$endEl.val(startStr);
						endDp.setViewDate(startDate)
						endDp.selectDate(startDate);
					}
					return;
				}
			} else if (!isNaN(inputDate)) { // 날짜 선택
				inputDp.setViewDate(inputDate)
				inputDp.selectDate(inputDate);
			}
		});
	}

	// form data 초기화
	_my.clearFormData = function(formId) {
		$("#" + formId).find("input, select, textarea, img").each(function(i, el) {
			const $el = $(el);
			if ($el.hasClass("excludeFormClear")) return true;

			let tag = ($el.prop("tagName") || "").toLowerCase();
			let type = ($el.attr("type") || "").toLowerCase();

			if (tag === 'input') {
				if (type === 'radio') {
					$el.prop("checked", false);

				} else if (type === 'checkbox') {
					$el.prop("checked", false);

				} else if (type === 'hidden') {
					$el.remove();

				} else {
					let dp = $el[0].datepicker;
					if (_my.isEmpty(dp) == false) { // datepicker
						dp.clear();

					} else {
						$el.val(""); // text, hidden, number, date 등
					}

				}

			} else if (tag === 'select') {
				$el.val("").trigger('change.select2'); // 단일 또는 배열 모두 가능

			} else if (tag === 'textarea') {
				$el.val("");
			} else if (tag === 'img') {
				$(".upload-box").show();
				$(".uploaded-info").hide();

				$el.attr("src", "");
			}
		})
	}
	// form data 설정
	_my.setFormData = function(formId, data, image) {
		image = image || {};
		_my.clearFormData(formId);

		$.each(data, function(key, value) {
			const $el = $($("#" + formId + " [name='" + key + "']")[0]);
			let tag = ($el.prop("tagName") || "").toLowerCase();
			let type = ($el.attr("type") || "").toLowerCase();

			// 이미지 미리보기(UPLOAD, DELETE FORM)
			if (_my.isEmpty(image[key]) == false) {
				tag = "img";
				type = "preview";
			}

			if (tag === 'input') {
				if (type === 'radio') {
					$("#" + formId + " [name='" + key + "'][value='" + value + "']").prop("checked", true);

				} else if (type === 'checkbox') {
					(value ?? "").split("|").forEach(_value => {
						$("#" + formId + " [name='" + key + "'][value='" + _value + "']").prop("checked", true);
					});

				} else {
					let dp = $el[0].datepicker;
					if (_my.isEmpty(dp) == false) { // datepicker
						let date = moment(value, dp.opts.dateFormat.toUpperCase());
						$el.val(value);
						dp.setViewDate(date)
						dp.selectDate(date);

					} else {
						$el.val(value); // text, hidden, number, date 등
					}

				}

			} else if (tag === 'select') {
				if ($el.attr("multiple")) {
					value = value.split("|");
				}
				$el.val(value).trigger('change.select2'); // 단일 또는 배열 모두 가능

			} else if (tag === 'textarea') {
				$el.val(value);
			} else if (tag === 'img') {
				if (type == "preview") {
					let fileKey = $.extend({
						ULD_FILE_NM: "ULD_FILE_NM"
						, FILE_SZ: "FILE_SZ"
						, ORGNL_FILE_NM: key
						, FILE_EXTN_NM: "FILE_EXTN_NM"
						, WDTH_SZ: "WDTH_SZ"
						, VRTC_SZ: "VRTC_SZ"
					}, image[key].fileKey);
					
					if(_my.isEmpty(data[fileKey.ULD_FILE_NM]) == false) {
						Common.viewImage({
							uploadInfoId: image[key].uploadInfoId
							, uploadBoxId: image[key].uploadBoxId
							, file: {
								ULD_FILE_NM: data[fileKey.ULD_FILE_NM]
								, FILE_SZ: data[fileKey.FILE_SZ]
								, ORGNL_FILE_NM: data[fileKey.ORGNL_FILE_NM]
								, FILE_EXTN_NM: data[fileKey.FILE_EXTN_NM]
								, WDTH_SZ: data[fileKey.WDTH_SZ]
								, VRTC_SZ: data[fileKey.VRTC_SZ]
							}
						});
					}

				} else {
					if (_my.isEmpty(value) == false) {
						value = window.cmmVO.CONTEXT_PATH + "/file/image?path=" + value;
					} else {
						value = ""; // 이미지 안보이도록 처리
					}
					$el.attr("src", value);
				}
			} else { // 없는경우 hidden으로 관리
				$("#" + formId).append(
					$("<input>", {
						type: "hidden"
						, name: key
						, value: value
						, "aria-hidden": "true"
						, role: "presentation"
					})
				);
			}
		});
	}
	// select2 image
	_my.select2ViewImage = function(option) {
		if (!option.id) return option.text;

		const imgSrc = $(option.element).data('img');

		if (imgSrc) {
			return $(`
				<span style="display: flex; align-items: center;">
					<img src="${imgSrc}" style="width: 24px; margin-right: 8px;">
					${option.text}
				</span>
			`);
		}
		return option.text;
	}
	// alert
	_my.alert = function(options) {
		let type = options.type || "alert";
		let head = options.head || "알림"
		let headIcon = options.headIcon || "info";
		let msg = options.msg || "메세지를 입력하세요";

		if (headIcon == "error") {
			head = '<i class="fas fa-exclamation-circle me-2"></i>' + head;
		} else {
			head = '<i class="fas fa-exclamation-circle me-2"></i>' + head;
		}
		$("#" + type + "Head").html(head);
		$("#" + type + "Msg").html(msg);


		const alertEl = $("#" + type + "Toast");
		const alert = new bootstrap.Toast(alertEl, { autohide: false });

		// 확인버튼
		if (typeof options.confirmCallback === "function") {
			$("#" + type + "BtnConfirm").removeClass('d-none');
			$("#" + type + "BtnConfirm").off().on("click", function() {
				$("#" + type + "Overlay").addClass('d-none');
				options.confirmCallback();
			});

		} else {
			$("#" + type + "BtnConfirm").addClass('d-none');
		}
		// 닫기, X 버튼
		alertEl.off().on('hidden.bs.toast', function() {
			$("#" + type + "Overlay").addClass('d-none');
		});

		$("#" + type + "Overlay").removeClass('d-none');
		alert.show();
	}
	// json을 data-inputmask 형식으로 변경
	_my.parseJsonToMask = function(obj) {
		if (_my.isEmpty(obj)) return null;

		return Object.entries(obj)
			.map(([key, value]) => {
				let val = typeof value === 'string' ? `'${value}'` : value;
				return `'${key}': ${val}`;
			})
			.join(', ') + '"'

	}
	// data-inputmask을 json 형식으로 변경
	_my.parseMaskToJson = function(str) {
		if (_my.isEmpty(str)) return null;

		// 1. 따옴표 정리: 'key': 'value' → "key": "value"
		let json = str
			.replace(/([\s{,]*)'([^']+)'\s*:/g, '$1"$2":')     // 키를 "key"로
			.replace(/:\s*'([^']+)'/g, ': "$1"');              // 값을 "value"로

		// 2. 중괄호로 감싸기
		json = `{${json}}`;

		// 3. JSON.parse 시도
		try {
			return JSON.parse(json);
		} catch (e) {
			console.error("파싱 실패:", e);
			return null;
		}
	}
	
	
	
	//form data to plainObject
	$.fn.serializeObject = function() {
		const obj = {};
		const serialized = this.serializeArray();
	
		serialized.forEach(({ name, value }) => {
			const $field = $(`[name="${name}"]`);
			value = value || null;
	
			const isMultiSelect = $field.is("select[multiple]");
			const isCheckbox = $field.is(":checkbox");
	
			// 배열 처리 대상일 경우
			if (isCheckbox || isMultiSelect) {
				if (!obj[name]) {
					obj[name] = [];
				}
				if (!Common.isEmpty(value)) {
					obj[name].push(value);
				}
			} else {
				let inputmask = Common.parseMaskToJson($field.data("inputmask"));
				if (Common.isEmpty(inputmask) == false && value) { // inputmask 값 처리
					if (inputmask.alias == "numeric") {
						value = value.replace(/[^0-9.+-]/g, '')
					}
	
				}
				obj[name] = value;
			}
		});
	
		const filteredObj = Object.fromEntries(
			Object.entries(obj).filter(([key, value]) => {
				return !(Array.isArray(value) && value.includes("ALL"));
			})
		);
	
		return filteredObj;
	};
	
	return _my;
}());