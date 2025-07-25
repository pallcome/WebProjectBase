// DataTable 기본값 설정
$.fn.DataTable.ext.pager.numbers_length = 13; // 페이징 표시 개수
$.fn.dataTable.Buttons.defaults.dom.button.className = "btn"

const DataTable = (function() {
	const _my = {createList : []};
	
	// 생성자
	_my.init = function(param) {
		let targetId = param.targetId;
		
		let options = {
			processing: true // 로딩 인디케이터
			, serverSide: false // 서버사이드 처리
			, lengthChange: false // 페이지당 개수 선택 제거
			, searching: false // 내장 검색창 제거
			, destroy: true // 기존 테이블 제거 후 재생성 허용
			, autoWidth: false // 열 너비 자동 계산 OFF (렌더링 속도 개선)
			// 페이지
			, pagingType: "full_numbers" // 페이징 UI 종류
			, pageLength: param.pageLength ? param.pageLength : 10 // 페이지당 항목 수 늘려서 클릭 수 줄임
			// 정렬
			, order : [] // 첫번째 컬럼 정렬 off
			, ordering: true // 정렬 ON (단, 데이터 정렬을 너무 자주 하지 않도록)
			, deferRender: false // 화면에 보이는 것만 렌더
			// 스크롤
			, scroller: true // 가상 스크롤 처리 (추가 로딩 느낌)
			, scrollX: true // 고정 높이로 스크롤 처리 (boolean or "~~~px")
			, scrollY: false // 고정 높이로 스크롤 처리 (10건) (boolean or "~~~px")
			, dom : `
				<'row tb-head'
					<'col-sm-6 dt-info-left'i> 
					<'col-sm-6 text-end dt-info-right'B> 
				>
				<'row'<'col-sm-12'tr>>
				<'row'
					<'col-sm-12 d-flex mt-3 justify-content-center'<'dataTables_paginate'>p
				>
			`
			// UI 문구
			, language: {
				paginate: {
					first: "처음"
					, last: "끝"
					, next: "다음"
					, previous: "이전"
				}
				, info: "총 <span class='total-num'>_TOTAL_</span>건"
				, infoEmpty: "총 <span class='total-num'>0</span>건"
				, zeroRecords: "조회된 결과가 없습니다"
				, emptyTable: "조회된 결과가 없습니다"
				, processing: "불러오는 중..."
				, lengthMenu: "_MENU_ 개 보기"
			}
			, initComplete : function(settings, result) {
				_my.initInput(targetId);
				
				if(typeof param.initCallback === "function") {
					param.initCallback(result, settings);
				}
				
				// 셀 클릭 이벤트
				if(typeof param.cellClick === "function") {
					$(document).on("click", `#${targetId} .cell-select`, function(e) {
						let row = $("#" + targetId).DataTable().row(this);
						param.cellClick(row.data(), row);
					});
				}
			}
		};
		// UI
		if(param.UI) {
			const ui = param.UI;
			let dom = "";
			
			if(ui.head !== false) {
				dom += `<'row tb-head'`; // 헤더
				
				const headLeft = ui.head_left !== false;
				const headRight = ui.head_right !== false;
				if ((headLeft && !headRight) || (!headLeft && headRight)) { // 둘중 한개만 있는 경우
					if(ui.head_left !== false) {
						dom += `<'col-sm-12 dt-info-left'i>`; // 왼쪽 정보
					}else if(ui.head_right !== false) {
						dom += `<'col-sm-12 text-end dt-info-right'B>`; // 오른쪽 버튼
					}
				}else {
					if(headLeft) {
						dom += `<'col-sm-6 dt-info-left'i}>`; // 왼쪽 정보
					}
					if(headRight) {
						dom += `<'col-sm-6 text-end dt-info-right'B>`; // 오른쪽 버튼
					}
				}
				dom += `>`;
			}
			
			dom += `<'row'<'col-sm-12'tr>>`; // 본문
			
			if(ui.paging !== false) {
				dom += `<'row'<'col-sm-12 d-flex mt-3 justify-content-center'<'dataTables_paginate'>p>`; // 페이징
			}else {
				options.pageLength = -1; // 페이징 
			}
			
			options.dom = dom;
		}
		
		// url
		if(param.url) {
			options.ajax = {
				url : param.url
				, data : function(d) {
					return JSON.stringify($.extend(d, window.cmmVO, param.data));
				}
				, type : "POST"
				, dataType : "json"
				, contentType: "application/json; charset=utf-8"
				, dataSrc : "data"
			}
		}
		
		// 행 선택
		let selectOption = {style : "single", info : false, selector: 'td.cell-select'}
		options.select = selectOption;
		
		// 행 번호
		if(param.visibleRowNo) {
			param.columns.unshift({data : null, title : "No.", width : "50px", orderable: false, className : "text-center rowNo no-export"});
			options.drawCallback = function(settings) {
				const dataTable = this.api();
				let pageInfo = dataTable.page.info();
				let num = pageInfo.recordsDisplay - pageInfo.start; // ✅ 역순 시작 번호 계산
			
				dataTable.rows({page: 'current'}).every(function () {
					let node = this.node();
					if(node) {
						$(node).find('td.rowNo').html(num--);
					}
				});
			}
		}
		// 행 선택
		if(param.checkedRows) {
			param.columns.unshift({data : "null", title : "", width : "50px", orderable: false, className : "text-center rowCheck no-export"
				, render : function(data, type, rowData, meta) {
					return `
						<div class="form-check form-check-inline me-0 align-middle">
							<input type="checkbox" class="form-check-input check-lg" name="CHECK_ROW_${targetId}"/>
						</div>
					`;
				}
			});
			
			// 행 선택 콜백
			$(document).off('change', `[name='CHECK_ROW_${targetId}']`)
			.on('change', `[name='CHECK_ROW_${targetId}']`, function(e) {
				param.rowSelectCallback(e);
			});
		}
		// 정렬
		const order = param.order;
		if(typeof order === "boolean") {
			options.ordering = order;
		}else if(order) {
			options.order = order;
		}
		// 행 생성 콜백
		if(typeof param.rowCallback === "function") {
			options.rowCallback = param.rowCallback;
		}
		
		// 버튼 정의
		let _buttons = param.buttons || [];
		let btnClass = "btn ";
		_buttons = _buttons.map(button => {
			if(button.type == "excel") { // 엑셀 다운로드
				return {
					extend: "excelHtml5"
					, text: '<i class="fas fa-file-excel me-2"></i>EXCEL 다운로드'
					, title: null
					, filename : button.fileName
					, className : btnClass + "btn-secondary"
					, exportOptions: {
						columns: ":visible:not(.no-export)" // 보이는 컬럼만
					}
				};
			}else {
				return {
					text: button.text
					, className : btnClass + (button.className || "")
					, attr : {
						id : button.id
					}
					, action: button.clickCallback
				};
			}
		});
		options.buttons = _buttons;
		
		
		// data 속성을 name과 동일하게 지정
		param.columns = param.columns.map(col => 'data' in col ? {...col, name: col.data} : col);
		options.columns = param.columns;
		
		// 수정 시 데이터 반영
		if(param.updateEditCell) {
			delete options.select;
		}
		
		const dataTable = $("#" + targetId).DataTable(options);
		// setRowsCallback
		if(typeof param.setRowsCallback === "function") { // setRows 후 callback
			dataTable.settings()[0].setRowsCallback = param.setRowsCallback
		}
		// 
		if(typeof param.valid === "function") { // valid
			dataTable.settings()[0].valid = param.valid
		}
		dataTable.settings()[0].title = param.title ?? $("#title").text();
		
		_my.createList.push(dataTable);
		
		return dataTable;
	}
	// dataTable 조회
	_my.getDataTable = function(targetId) {
		let dataTable;
		if(Common.isEmpty(targetId)) {
			dataTable = _my.createList[0];
		}else {
			dataTable = _my.createList.find(function(dt) {
				return $(dt.table().node()).attr("id") === targetId;
			});
		}
		
		return dataTable;
	}
	// 컬럼 리사이즈
	_my.dataTableResize = function() {
		if(Common.isEmpty(_my.createList)) return;
		
		_my.createList.forEach(dataTable => {
			dataTable.columns.adjust();
		});
	}
	// 선택 행 조회
	_my.getSelectedRows = function(targetId) {
		const dataTable = _my.getDataTable(targetId);
		targetId = $(dataTable.table().node()).attr("id")
		
		return $(`[name='CHECK_ROW_${targetId}']:checked`)
				.map(function () {
					const $tr = $(this).closest('tr');
					return dataTable.row($tr).data();
				})
				.toArray();
	}
	// 선택 행 설정
	_my.setSelectedRows = function(options, targetId) {
		const dataTable = _my.getDataTable(targetId);
		_my.clearSelectedRows(dataTable);
		
		const matchList = [];
		const list = dataTable.rows().data().toArray(); // 전체 데이터 가져옴
		const where = options.where;
		const whereType = (options.whereType || "AND").toUpperCase();
		
		for (let i = 0; i < list.length; i++) {
			const rowData = list[i]; // 한 줄의 데이터 (Object 또는 Array)
			
			if(whereType === "OR") {
				match = where.some(condition => {
					return Object.entries(condition).some(([key, val]) => rowData[key] == val);
				});
			}else { // and
				match = where.every(condition => {
					return Object.entries(condition).every(([key, val]) => rowData[key] == val);
				});
			}
			
			if (match) {
				matchList.push(rowData);
				// i번째 행의 tr
				$(dataTable.row(i).node()).addClass("selected");
			}
		}
		
		return matchList;
	}
	// 선택 행 해제
	_my.clearSelectedRows = function(targetId) {
		const dataTable = _my.getDataTable(targetId);
		dataTable.$('tr.selected').removeClass('selected');
	}
	// render
	_my.render = function(options) {
		const data = options.data ?? "";
		const type = options.type;
		const rowData = options.rowData;
		const meta = options.meta;
		const editType = (options.editType || "").toLowerCase();
		const columnName = meta.settings.aoColumns[meta.col].data;
		
		// 속성(필수)
		const autocomplete = `autocomplete="off"`;
		const dataName = `data-name="${columnName}"`;
		const dataRowNum = `data-row="${meta.row}"`;
		// 속성(옵션)
		const required = options.required ? "required" : "";
		const validText = options.validText ? `validText="${options.validText}"` : "";
		const disabled = options.disabled ? `disabled` : "";
		
		if(editType == "img") { // 이미지
			const fileExtn = options.fileExtn;
			return Common.isEmpty(data) ? "" : `<img src="${window.cmmVO.CONTEXT_PATH}/file/image?path=${data}.${fileExtn}"/>`;
			
		}else if(editType == "button") { // 버튼
			let color = (options.color || "").toLowerCase();
			let icon = (options.icon || "").toLowerCase();
			let iconMargin = data ? "me-2" : "";
			let className = options.className || "";
			let style = options.style || "";
			if(style){
				let styleStr = Object.entries(style).map(([key, value]) => `${key}:${value}`).join('; ');
				style = `style="${styleStr}"`;
			}
			
			if(color == "info") {
				color = "btn-info";
			}else if(color == "danger") {
				color = "btn-danger";
			}
			if(icon == "edit") {
				icon = `<i class="fas fa-pen ${iconMargin}"></i>`;
			}else if(icon == "delete") {
				icon = `<i class="fa-solid fa-trash ${iconMargin}"></i>`;
			}else if(icon == "plus") {
				icon = `<i class="fas fa-search-plus ${iconMargin}"></i>`;
			}
			
			return `<button class="btn ${color} ${className}" ${dataRowNum} ${style}>${icon}${data}</button>`;
			
		}else if(editType == "input") {
			let mask = "";
			let className = options.className || "";
			
			if(options.mask) {
				mask = 'data-inputmask="' + Common.parseJsonToMask(options.mask);
			}
			const readonly = options.readonly ? "readonly" : "";
			
			return `<input type="text" value="${data}" class="form-control ${className} ${readonly}" ${dataRowNum} ${dataName} ${required} ${validText} ${mask} ${readonly}/>`;
			
		}else if(editType == "radio") {
			const checked = data == options.checked ? "checked" : "";
			
			return `<div class="form-check form-check-inline">
						<input type="radio" id="${columnName}_${meta.row}" value="${options.checked}" data-un-checked="${options.unChecked}" class="form-check-input" ${dataRowNum} ${dataName} ${required} name="${columnName}" ${validText} ${checked}/>
					</div>`
			
		}else if(editType == "date") {
			const endDate = options.endDate;
			
			let pickerOptions = JSON.stringify(options.pickerOptions || {});
			if(Common.isEmpty(pickerOptions) == false) {
				pickerOptions = `data-date-picker-options='${pickerOptions}'`;
			}
			
			return `<div class="calendar-item">
						<label for="${columnName}_${meta.row}"><i class="fa-solid fa-calendar-days"></i></label>
						<input type="text" id="${columnName}_${meta.row}" value="${data}" class="calendar-input " ${dataRowNum} ${dataName} ${required} ${validText} ${endDate ? `endDate="${endDate}_${meta.row}"` : ""} ${pickerOptions} ${autocomplete} ${disabled}/>
					</div>`;
					
		}else if(editType == "select") {
			const _options = options.options ?? [];
			
			return `<select class="form-select" name="${columnName}_${meta.row}" ${dataRowNum} ${dataName} ${required} ${validText} ${disabled}>
						${_options.map(item => `<option value="${item.value}" ${item.value == data ? 'selected' : ''}>${item.text}</option>`).join("")}
					</select>`;
		}
	}
	// 데이터 가져오기
	_my.getRowDataList = function(targetId) {
		const dataTable = _my.getDataTable(targetId);
		return dataTable.rows().data().toArray();
	}
	// 수정 된 데이터 가져오기
	_my.getEditRowDataList = function(targetId) {
		const dataTable = _my.getDataTable(targetId);
		const rows = {deleteRows : [], saveRows : []};
	
		dataTable.rows().every(function(rowIdx, tableLoop, rowLoop) {
			const rowData = { ...this.data() }; // 원본 복사
			let rowType = $(this.node()).hasClass("delete") ? "deleteRows" : "saveRows";
	
			// 해당 row의 셀들을 순회하면서 input/select 등 값 반영
			$(this.node()).find(":input[data-name]").each(function() {
				const $input = $(this);
				const name = $input.data("name");
				const value = $input.val() || null;
	
				// 라디오일 경우 선택된 것만 반영
				if ($input.is(':radio')) {
					if ($input.prop('checked')) {
						rowData[name] = value;
					}else {
						rowData[name] = $input.data("unChecked");
					}
				}else { // 그 외 input/select/textarea는 그대로 값 반영
					rowData[name] = value;
				}
			});
	
			rows[rowType].push(rowData);
		});
	
		return rows;
	};
	// 컬럼 visible
	_my.visible = function(columns, visible, targetId) {
		if(Common.isEmpty(columns)) return;
		
		const dataTable = _my.getDataTable(targetId);
		if(typeof columns === "string") {
			columns = [columns];
		}
		
		columns.forEach(name => {
			dataTable.column(`${name}:name`).visible(visible);
		});
		
	}
	// 행 추가
	_my.addRows = function(rowData, targetId) {
		const dataTable = _my.getDataTable(targetId);
		
		dataTable.rows.add(rowData).draw(false);
		_my.initInput(targetId ?? $(dataTable.table().node()).attr("id"))
	}
	// 행 삭제
	_my.removeRows = function(rowIdxs, targetId) {
		const dataTable = _my.getDataTable(targetId);
		
		// 삭제 대상 row들만 바로 선택해서 처리
		dataTable.rows(rowIdxs).nodes().to$().addClass("delete").hide();
	}
	// input 초기화
	_my.initInput = function(targetId) {
		// datepicker 초기화
		let endDateList = [];
		$.each($(`#${targetId} .calendar-input:not(".hasDatepicker")`), function() {
			let $start = $(this);
			let options = $(this).data("datePickerOptions");
			
			if(endDateList.includes($start[0].id)) return true;
			
			if(Common.isEmpty($start) == false) {
				Common.datepicker($start, options);
			}
			
			var $end = $("#" + $(this).attr("endDate"));
			if(Common.isEmpty($end) == false) {
				endDateList.push($end[0].id);
				
				Common.datepicker($end, options);
				
				Common.datepickerRange($start, $end);
			}
		});
		
		// mask 적용
		Inputmask().mask($(":input[data-inputmask]"));
	}
	// DataTable 데이터 설정
	_my.setRows = function(result, targetId) {
		const dataTable = _my.getDataTable(targetId);
		dataTable.clear();
		dataTable.rows.add(result.rows.data).draw(true);
		_my.dataTableResize(targetId);
		_my.initInput(targetId);
		
		const setting = dataTable.settings();
		if(typeof setting[0].setRowsCallback === "function") {
			setting[0].setRowsCallback(result, setting);
		}
	}
	// DataTable 데이터 설정
	_my.setColumnData = function(result, targetId) {
		const row = result.row ?? null;
		const column = result.column ?? null;
		const data = result.data;
		
		if(row == null || column == null) return;
		$(`:input[data-row='${row}'][data-name="${column}"]`).val(data);
	}
	// 유효성 검사
	_my.valid = function(targetId) {
		const dataTable = _my.getDataTable(targetId);
		let errors = [];
		
		// 필수만 체크
		let rowNum = 1;
		dataTable.rows().every(function(rowIdx, tableLoop, rowLoop) {
			const rowData = { ...this.data() }; // 원본 복사
			const $tr = $(this.node());
			
			if($tr.hasClass("delete")) return; // 삭제된 행은 제외
	
			// 해당 row의 셀들을 순회하면서 필수값 체크
			$tr.find(":input[data-name][required]").each(function() {
				const $input = $(this);
				const name = $input.data("name");
				const rowIdx = $input.data("row");
				let label = $input.attr("validText");
				
				// 해당 컬럼의 헤더 텍스트 가져오기
				const tdIndex = $input.closest('td').index(); // 몇 번째 컬럼인지 (0부터 시작)
				const $th = $(dataTable.table().node()).find('thead th').eq(tdIndex);
				label = (label || $th.text()) + `(${rowNum}행)`;
				
				const target = `:input[data-name='${name}'][required][data-row='${rowIdx}']`;
				const msg = "필수 값입니다.";
				
				// 라디오 경우 전체 중 1개(없으면 전부 required 표시)
				if ($input.is(':radio')) {
					const checked = $(dataTable.table().node()).find(`:radio[data-name='${name}']:checked`);
					if(checked.length == 0) {
						errors.push({
							target : target
							, label : label
							, message : [msg]
						});
					}
					
				}else if(Common.isEmpty($input.val())) { // 그 외 input/select/textarea는 그대로 값 반영
					errors.push({
						target : target
						, label : label
						, message : [msg]
					});
				}
			});
			rowNum++;
		});
		// custom valid 함수 호출
		dataTable.settings()[0].valid(dataTable, errors);
		
		return {
			valid : errors.length == 0
			, title : dataTable.settings()[0].title
			, errors : errors
		};;
	}
	return _my;
}());