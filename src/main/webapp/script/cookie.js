function setCookie(name, value, days) { // 쿠키를 생성한다. 일일 단위 (시간이 아님)
	var today = new Date();
	today.setDate(today.getDate() + days);
	document.cookie = name + "=" + encodeURIComponent(value) + "; path=/; expires=" + today.toGMTString() + ";";
}

function getCookie(name) { // 쿠키를 가져온다.
	name += "=";
	var cookie = document.cookie;
	var idx = cookie.indexOf(name);
	var is_pop = '';
	if (cookie && idx >= 0) {
		var tmp = cookie.substring(idx, cookie.length);
		var deli = tmp.indexOf(";");
		if (deli > 0) {
			is_pop = tmp.substring(name.length, deli);
		} else {
			is_pop = tmp.substring(name.length);
		}
	}	
	return decodeURIComponent(is_pop);
}

function delCookie(name) { // 쿠키를 삭제한다.
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() - 1);
	document.cookie = name + "= " + "; expires=" + expireDate.toGMTString() + "; path=/";
}