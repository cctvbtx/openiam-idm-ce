<!-- a list of all available groups -->

<%@ page language="java" %>
<%@ page import="java.util.*,org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="org.openiam.idm.srvc.grp.dto.Group" %>
<%@ page import="org.openiam.webadmin.busdel.base.JSPUtil" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<jsp:include page="groupsearch.jsp" flush="true" />

<%
	//Collection policyList = (Collection) request.getAttribute("policyList");
	List groupList = (List) request.getAttribute("groupList");
	DynaValidatorForm grpForm = (DynaValidatorForm) request.getAttribute("groupForm");
	String mode = (String) grpForm.get("mode");
	
%> 

	<body>
	<br>
		<table cellspacing=2 cellpadding=2 width="400" align="center">
		<html:form action = "/security/group.do?method=removeGroup" >
		<html:hidden property="mode" value="<%=mode%>" /> 
			<tr>
    		<td colspan="3" class="title" >	
					<strong>Groups</strong>
   			</td>
			</tr>	
			
			
			<tr>
		 		   <td colspan="3" align="center" bgcolor="8397B4" >
		 		    <font></font>
		 		   </td>
		    </tr> 
		      
		    <tr>
		 		   <td>&nbsp;</td>
		    </tr> 
		    
		    
			
			<% 
			if (groupList != null){
				if (!groupList.isEmpty()) {
			    	int x = 0;
				    Iterator groupListIterator = groupList.iterator();
				    if (groupListIterator != null) {
					while (groupListIterator.hasNext()) {
					Group groupValue = (Group)groupListIterator.next();
					
			%>
			
					  <%
					     if ((x%2) != 0) {
					       
					   %>
					   <tr class="tddark">
					   <% } else {%>
					   <tr class="tdlight">
					   <%}%>
			      <td colspan="2" >
            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="groupId" value="<%=groupValue.getGrpId()%>"/>
                  <a href="security/group.do?method=editGroup&groupId=<%=groupValue.getGrpId()%>" class="link">
                         <%=groupValue.getGrpName()%> <!--  Add Child Group --> <br>
                  </a>
                  </td>
					<td></td>
					</tr>
					
      	<%
      	  x++;
      	 }
      	 }
      	%>
       
			  
			 			 
			<%} else {%>
				<tr >
					<td colspan="2">No Groups Found</td>
				</tr>
			<%}
			}
			%>
			
			<tr>
		    	  <td>&nbsp;</td>
		    </tr>
		 
		    <tr>
		 		   <td colspan="3" align="center" bgcolor="8397B4" >
		 		    <font></font>
		 		   </td>
		    </tr>   
			
			<tr>
				<td>
  					<a href="security/group.do?method=addGroup">New Group</a>  
  		  </td>
			  <td align="right"  colspan="2">
			    	<html:submit property="submit" value="Delete" />			
				</td>
			</tr>
  	</table>
	</html:form> 
 
	<table align="center" width="400">
 		<% if(mode.equalsIgnoreCase("add") || mode.equalsIgnoreCase("edit")) { %>     
			<tr><td colspan="2">
    			
    		<jsp:include page="group.jsp">
    	 			<jsp:param name="mode" value="<%=mode%>" />   			
				</jsp:include>

  		</td></tr>
 		<%}%>
	</table> 
	    
 </body>
