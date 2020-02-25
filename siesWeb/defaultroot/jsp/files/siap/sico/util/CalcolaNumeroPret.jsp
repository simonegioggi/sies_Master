<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <%@ page import="f3b.web.IWebConstants" %>
<head>
  <title>[S.I.E.S.] - Conversione Numeri Fascicoli Pretura</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  function trim(inputString) {
     // Removes leading and trailing spaces from the passed string. Also removes
     // consecutive spaces and replaces it with one space. If something besides
     // a string is passed in (null, custom object, etc.) then return the input.
     if (typeof inputString != "string") { return inputString; }
     var retValue = inputString;
     var ch = retValue.substring(0, 1);
     while (ch == " ") { // Check for spaces at the beginning of the string
        retValue = retValue.substring(1, retValue.length);
        ch = retValue.substring(0, 1);
     }
     ch = retValue.substring(retValue.length-1, retValue.length);
     while (ch == " ") { // Check for spaces at the end of the string
        retValue = retValue.substring(0, retValue.length-1);
        ch = retValue.substring(retValue.length-1, retValue.length);
     }
     while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
        retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
     }
     return retValue; // Return the trimmed string back to the user
  } // Ends the "trim" function

  //==================================
  //
  //==================================
  function Decode(id){
    id=id.toUpperCase();
    a = id.charCodeAt(0);
    
    if((a-64)>0 && (a-64)<27){
      decStr = '' + (a-64); 
    }
    else if((a-18)>30 && (a-18)<40){
      decStr = '' + (a-18); 
    }
    else{
      decStr = '00'
    }
  
    if(decStr.length==1){
      decStr='0'+decStr
    } 
    return decStr;  
  }

  //===================================
  //
  //===================================
  function Verify()
  {
    if (document.f.anno.value=="" || document.f.numero.value=="")
      alert("I campi Anno e Numero sono obbligatori");
    else if (document.f.lettera.value=="")
      Converti();
    else
      ConvertiLettera();
  }
  
  //===================================
  //
  //===================================
  function Converti()
  {
    var new_prog;
    var anno=trim(document.f.anno.value);
    var num=trim(document.f.numero.value);
    new_prog = (800000 + parseInt(num))+'';
    document.f.ris.value=anno+'/'+new_prog;
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=anno;
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=new_prog;
    window.parent.close();
  }

  //===================================
  //
  //===================================
  function ConvertiLettera()
  {
    var tmp='';
    var new_prog;
    var anno=trim(document.f.anno.value);
    var num=trim(document.f.numero.value);
    var let=trim(document.f.lettera.value);
  
    if (let.length==1)
    {
      tmp=Decode(let);
      tmp+='0000';
    }
    else if (let.length==2)
    {
      tmp=Decode(let.charAt(0))+ '' + Decode(let.charAt(1));
      tmp+='00';
    }
    else if (let.length==3)
    {
      tmp=Decode(let.charAt(0)) + '' + Decode(let.charAt(1)) + '' + Decode(let.charAt(2));
    }
    
    //new_prog=(900000+parseInt(num))+tmp;
    new_prog=(1700000+parseInt(num))+tmp;
    document.f.ris.value=anno+'/'+new_prog;
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=anno;
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=new_prog;
    window.parent.close();
  }
</script>
</head>


<body class="corpo">
<table>
      <tr>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Conversione Numeri Fascicoli Pretura</font>
      </td>
   </tr>
 </table>
<form name="f" method="post">
  <table>
  <tr><td class=Titolo colspan=2>Fascicolo Pretura</td></tr>
  <tr><td class=l>Anno</td><td><input type="text" name="anno" size="4" maxlength="4" value=""></td></tr>
  <tr><td class=l>Numero</td><td><input type="text" name="numero" size="8" maxlength="12" value=""></td></tr>
  <tr><td class=l>Lettera</td><td><input type="text" name="lettera" size="3" maxlength="3" value=""></td></tr>
  <tr><td class=l colspan=2><input type="button" name="go" value="Trova Equivalente in SIES" onclick="Javascript:Verify();"></tr>
  </table>
  <input type="hidden" name="ris" value="">
</form>
<div align=left style="visibility:hidden" id="result">
</div>
</body>
</html>