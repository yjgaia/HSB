<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
	<head>
		<title>HSB</title>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
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
			$.getJSON('${fn:indexOf(result.url, "?") == -1 ? result.url.concat(".json") : result.url.substring(0, fn:indexOf(result.url, "?")).concat(".json").concat(result.url.substring(fn:indexOf(result.url, "?")))}', function(obj) {
				$('#json').text(JSON.stringify(obj, null, 4));
			});
			$.get('${fn:indexOf(result.url, "?") == -1 ? result.url.concat(".xml") : result.url.substring(0, fn:indexOf(result.url, "?")).concat(".xml").concat(result.url.substring(fn:indexOf(result.url, "?")))}', function(xml) {
				$('#xml').text(formatXml(xml));
			}, 'text');
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
		<h3>결과</h3>
		<textarea>${result}</textarea>
		<h3>JSON</h3>
		<pre id="json"></pre>
		<h3>XML</h3>
		<pre id="xml"></pre>
	</body>
</html>
