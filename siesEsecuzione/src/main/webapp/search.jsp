<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="it.mig.sies.model.ResponseData"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<!-- MEV 42558 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Risultati della ricerca</title>
        <link rel="stylesheet" type="text/css" href="table.css" />
    </head>
    <body>
    	<center>
        	<table id="box-table-a" border="1">
	            <!-- MEV 42558 - Modificata label e gestione output -->
	            <c:choose>
				  	<c:when test="${not empty responseDataList}">
					  	<thead>
			                <tr>
			                    <th colspan="4" align="left" bgcolor="#000000">
			                        <font size="+1" color="#000000">
			                            Risultati della ricerca
			                        </font>
			                    </th>
			                </tr>
			                <tr>
			                    <th scope="col">Esito</th>
			                    <th scope="col">Operazione</th>
			                    <th scope="col">Data operazione</th>
			                    <th scope="col">Estratto</th>
			                </tr>
		            	</thead>
		            	<tbody>
					  	<%
					  		@SuppressWarnings("unchecked")
		                 	List<ResponseData> responseDataList = (List<ResponseData>) request.getAttribute("responseDataList");
		                 	Iterator<ResponseData> mIterator = responseDataList.iterator();
		                 	int mrow = 0;
		                 	while (mIterator.hasNext()) {
		                     	ResponseData responseData = mIterator.next();
		              	%>
				              	<tr>
				                 	<td><%= responseData.getEsito() %></td>
				                 	<td><%= responseData.getOperazione() %></td>
				                 	<td><%= responseData.getDataOperazione() %></td>
				                 	<td><% if ((!responseData.isCompleted())||(responseData.getEstratto()==null)) {%>
				                        	<font color="red">ND</font>
	                     				<%	} else{%>
				                        	<font color="green">
				                        		<img height="25" width="25" src="images/pdf_icon.jpg"/> <a href="load?id=<%= responseData.getId() %>">Estratto</a>
			                        		</font>
				                        <%}%>
				                 	</td>
				              	</tr>
		              	<%
		                    	mrow++;
		                 	}
		              	%>
		              </tbody>
				  </c:when>
				  <c:otherwise>
				  	<thead>
						<tr>
							<th colspan="4" align="left" bgcolor="#000000">
								<%-- MEV 16: modificata dicitura (ex SIUS) --%>
								<font size="+1" color="#000000"> Non risulta effettuata alcuna trasmissione da SIES al SIC</font>
							</th>
						</tr>
					</thead>
				  </c:otherwise>
				</c:choose>
            </table>
            <br/>
    	</center>
	</body>
</html>