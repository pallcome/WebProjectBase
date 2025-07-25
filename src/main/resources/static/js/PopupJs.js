const Popup = (function() {
	const _my = {};
	const popupList = [];
	
	// 팝업 자동 Close
	window.addEventListener("beforeunload", () => {
		popupList.forEach(item => {
			try {
				item.popup.close();
			} catch (e) {}
		});
	});
	
	_my.fixQuery = function(url) {
		const firstIdx = url.indexOf('?');
		if (firstIdx === -1) return url; // ? 없음
	
		const before = url.slice(0, firstIdx + 1);
		const after = url.slice(firstIdx + 1).replace(/\?/g, '&');
		return before + after;
	}
	
	_my.open = function(options) {
		const id = options.id;
		let url = _my.fixQuery(window.cmmVO.CONTEXT_PATH + options.url + `?id=${id}`);
		
		let spec = {width : 800, height : 800, status : "yes",resizable : "yes",scrollbars : "yes", ...options.options};
		
		if(!spec.left || !spec.top) {
			// 현재 브라우저 창 기준 좌표 + 창 크기
			const screenX = window.screenX ?? window.screenLeft; // 좌측 위치
			const screenY = window.screenY ?? window.screenTop;  // 상단 위치
			const outerWidth = window.outerWidth;
			const outerHeight = window.outerHeight;
			
			// 팝업을 현재 브라우저 창 정중앙에 띄우기
			const left = screenX + (outerWidth - spec.width) / 2;
			const top = screenY + (outerHeight - spec.height) / 2;
			
			spec.left = left;
			spec.top = top;
		}
		spec = Object.entries(spec).map(([key, value]) => `${key}=${value}`).join(",");
		
		const openCallback = options.openCallback; // 팝업한테 데이터 전송
		const closeCallback = options.closeCallback; // 부모한테 데이터 전송
		
		_my.remove(id); // 한번 열었던 팝업의 객체 삭제
		
		const popup = window.open(url, id, spec);
		if(!popup || popup.closed || typeof popup.closed == 'undefined'){
			alert('해당 브라우저의 팝업 차단 기능이 설정되어 있습니다. 차단 해제 후 사이트를 이용하여 주십시오.');
		}else{
			popupList.push({id : id, popup :popup, openCallback : openCallback, closeCallback : closeCallback});
			popup.focus();
		}
	}
	// 팝업 찾기
	_my.find = function(id) {
		return popupList.find(item => item.id === id);
	}
	// 팝업 지우기
	_my.remove = function(id) {
		const index = popupList.findIndex(item => item.id === id);
		if(index != -1) {
			popupList.splice(index, 1);
		}
	}
	// 팝업한테 데이터 전송
	_my.openCallback = function(id) {
		const obj = _my.find(id);
		if(!obj || typeof obj.openCallback !== "function") return;
		
		return obj.openCallback();
	}
	// 부모한테 데이터 전송
	_my.closeCallback = function(id, result) {
		const obj = _my.find(id);
		if(!obj || typeof obj.closeCallback !== "function") return;
		
		return obj.closeCallback(result, obj.popup);
	}
	
	return _my;
}());
window.Popup = Popup;
