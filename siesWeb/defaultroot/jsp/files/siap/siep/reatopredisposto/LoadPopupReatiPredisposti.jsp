<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.reatopredisposto.action.ICostantiReatoPredisposto" %>
<%@ page import="siap.siep.reatopredisposto.model.ReatoPredispostoModel" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>

<jsp:useBean id="reatopredisposto" scope="request" class="java.util.Vector" />
<jsp:useBean id="stringacampi" scope="request" class="java.util.Vector" />

<html>
<head>
  <title> [S.I.E.S.] - Reati Predisposti - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
    function insNorme(str) {
    	
    	formName = window.parent.opener.document.<%=request.getParameter("formname")%>;

		pulisciCampi();
		
    	arrNorme = str.split("<%=ICostantiReatoPredisposto.SEP_NORME%>");
		
		//alert("LarrNorme=" + arrNorme.length);
		
		i = 0;			
		for (c=0; c < arrNorme.length; c++) {
			
			arrCampi = arrNorme[c].split("<%=ICostantiReatoPredisposto.SEP_CAMPI%>");
			
			if (!testCheck(arrCampi)) {

				for (cf=0; cf < formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).options.length; cf++) {
					
					if (formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).options[cf].value == arrCampi[0]) {
						formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).selectedIndex=cf;
					}									
				}
				
				for (cf=0; cf < formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).options.length; cf++) {
					
					if (formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).options[cf].value == arrCampi[4]) {
						formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).selectedIndex=cf;
					}									
				}			
				
				formName.<%=ICostantiReato.CAMPO_ANNO_FONTE%>(i).value=arrCampi[1];
				formName.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>(i).value=arrCampi[2];
				formName.<%=ICostantiReato.CAMPO_ARTICOLO%>(i).value=arrCampi[3];
				formName.<%=ICostantiReato.CAMPO_COMMA%>(i).value=arrCampi[5];
				formName.<%=ICostantiReato.CAMPO_LETTERA%>(i).value=arrCampi[6];
				formName.<%=ICostantiReato.CAMPO_NUMERO%>(i).value=arrCampi[7];
				
				i++;
			}

		}
      
      window.parent.close();
    }

    function testCheck(arrCampi) {
    	
    	ret = false;
    	
		if (arrCampi[0] == "01") {
		
			if (arrCampi[3] == "110" && arrCampi[1] == "" && arrCampi[2] == "" && arrCampi[4] == "-" 
				&& arrCampi[5] == "" && arrCampi[6] == "" && arrCampi[7] == "") {
				
				formName.cablati2(0).checked = true;
				ret = true;
			}
			
			if (arrCampi[3] == "56" && arrCampi[1] == "" && arrCampi[2] == "" && arrCampi[4] == "-" 
				&& arrCampi[5] == "" && arrCampi[6] == "" && arrCampi[7] == "") {
				
				formName.cablati2(1).checked = true;
				ret = true;
			}
			
			if (arrCampi[3] == "81" && arrCampi[1] == "" && arrCampi[2] == "" && arrCampi[4] == "-" 
				&& arrCampi[5] == "1" && arrCampi[6] == "" && arrCampi[7] == "") {
				
				formName.cablati2(2).checked = true;
				ret = true;
			}
			
			if (arrCampi[3] == "81" && arrCampi[1] == "" && arrCampi[2] == "" && arrCampi[4] == "-" 
				&& arrCampi[5] == "2" && arrCampi[6] == "" && arrCampi[7] == "") {
				
				formName.cablati2(3).checked = true;
				ret = true;
			}									
		}
		
		return ret;

    }
    
    function pulisciCampi() {
		
		formName = window.parent.opener.document.<%=request.getParameter("formname")%>;
		    	
		for (i=0; i < 5; i++) {
			
			formName.cablati2(i).checked = false;
			formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).selectedIndex=0;
			formName.<%=ICostantiReato.CAMPO_ANNO_FONTE%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_ARTICOLO%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).selectedIndex=0;
			formName.<%=ICostantiReato.CAMPO_COMMA%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_LETTERA%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_NUMERO%>(i).value="";
		}
		
		for (i=0; i < 31; i++) {
			
			formName.cablati(i).checked = false;
			
		}		
    }    
    </script>  
</head>
  <body class="corpo">
    <table>
      <tr>
        <td class=LBG>Elenco Reati Predisposti</td>
      </tr>
    </table>
    <Table width="100%">
<%
      	Iterator itx = reatopredisposto.iterator();
		int cont = 0;
		String str = new String();
		
      	while ( itx.hasNext()) {
      		
	        ReatoPredispostoModel lReato = (ReatoPredispostoModel)itx.next();
			
	        if (lReato.getProgrNorma().intValue() == 1) { %>
	            <tr>
	            <td class=l><%=lReato.getNomeElemento()%></td>
	        <%
	        	str = (String)stringacampi.get(cont);
	        	cont++;
	        %>    
	        	<td class=c><a href="Javascript:insNorme('<%=StringUtils.cStrForJS(str)%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
	        	</tr>
	        <%
	        }
      	}
%>
    </table>
</html>