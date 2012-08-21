<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
	<head>
		<title>HSB</title>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/json2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/form2js.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/js2form.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/cookie.js"></script>
		<script type="text/javascript">
		
		function formatXml(xml) {
		    var formatted = '';
		    var reg = /(>)(<)(\/*)/g;
		    xml = xml.replace(reg, '$1\r\n$2$3');
		    var pad = 0;
		    jQuery.each(xml.split('\r\n'), function(index, node) {
		        var indent = 0;
		        if (node.match( /.+<\/\w[^>]*>$/ )) {
		            indent = 0;
		        } else if (node.match( /^<\/\w/ )) {
		            if (pad != 0) {
		                pad -= 1;
		            }
		        } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
		            indent = 1;
		        } else {
		            indent = 0;
		        }

		        var padding = '';
		        for (var i = 0; i < pad; i++) {
		            padding += '    ';
		        }

		        formatted += padding + node + '\r\n';
		        pad += indent;
		    });

		    return formatted;
		}
		
		$(function() {
			
			if (getCookie('method') !== '') {
				js2form($('#form')[0], {
					method: getCookie('method')
					, url: getCookie('url')
					, params: getCookie('params')
				});
			}
			
			$('#form').submit(function() {
				var o = form2js(this);
				var p = eval('(' + o.params + ')');
				
				setCookie('method', o.method, 30);
				setCookie('url', o.url, 30);
				setCookie('params', o.params, 30);
				
				$.ajax({
					url: o.url.indexOf('?') === -1 ? o.url + '.json' : o.url.substring(0, o.url.indexOf('?')) + '.json' + o.url.substring(o.url.indexOf('?'))
					, type: o.method
					, data: p
					, success: function(json) {
						$('#json').text(JSON.stringify(json, null, 4));
						
						$.ajax({
							url: o.url.indexOf('?') === -1 ? o.url + '.xml' : o.url.substring(0, o.url.indexOf('?')) + '.xml' + o.url.substring(o.url.indexOf('?'))
							, type: o.method
							, data: p
							, dataType: 'text'
							, success: function(xml) {
								$('#xml').text(formatXml(xml));
							}
						});
						
					}
				});
				
				return false;
			});
			$('#form').submit();
		});
		</script>
		<style>
			textarea {
				width: 100%;
				height: 100px;
			}
		</style>
	</head>
	
	<body>
		<form id="form">
			<input id="get" type="radio" name="method" value="GET" checked="checked"><label for="get">GET</label>
			<input id="post" type="radio" name="method" value="POST"><label for="post">POST</label>
			<input id="put" type="radio" name="method" value="PUT"><label for="post">PUT</label>
			<input id="delete" type="radio" name="method" value="DELETE"><label for="delete">DELETE</label>
			<textarea name="url">http://localhost:8080/HSB/user/account</textarea>
			<textarea name="params">{username: 'test'}</textarea>
			<button>LOAD</button>
		</form>
		<table>
			<tr>
				<td valign="top">
					<h3>JSON</h3>
					<pre id="json"></pre>
				</td>
				<td width="50"></td>
				<td valign="top">
					<h3>XML</h3>
					<pre id="xml"></pre>
				</td>
			</tr>
		</table>
	</body>
</html>
