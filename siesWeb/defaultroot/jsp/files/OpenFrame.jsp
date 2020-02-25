<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>

<html>
  	<head>
    <title>Login S.I.E.S.</title>
    <script language="Javascript">
	function OpenFrame() {
        var theLink="<%=IWebConstants.PG_FRAMESET%>";
        var desktop=window.open(theLink, null,"toolbar=no,location=no,status=yes,menubar=no,scrollbars=yes,resizable=no");
        desktop.window.moveTo(0,0);
        desktop.window.resizeTo(screen.availWidth,screen.availHeight);
        desktop.focus();
        // [SG]: 20190214 --> aggiunta chiusura popup automatica
        self.open('', '_self').close();
	}
    </script>
  	</head>
  	<body onLoad="javascript:OpenFrame();"/>
</html>