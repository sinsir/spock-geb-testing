package com.sullivan.gareth;

public interface HtmlContent {

	String TITLE = "Name And Age Submission Form";
	
	String EXAMPLE_FORM = "<html>" + "\r\n"
			+"	<head>" + "\r\n"
			+"		<title>"+TITLE+"</title>" + "\r\n"
			+"	</head>" + "\r\n"
			+"    <body>" + "\r\n"
			+"        <form action=\"#\" th:action=\"@{/}\" th:object=\"${person}\" method=\"post\" id=\"signupForm\">" + "\r\n"
			+"            <table>" + "\r\n"
			+"                <tr>" + "\r\n"
			+"                    <td>Name:</td>" + "\r\n"
			+"                    <td><input type=\"text\" th:field=\"*{name}\" id=\"myName\"/></td>" + "\r\n"
			+"                    <td th:if=\"${#fields.hasErrors('name')}\" th:errors=\"*{name}\">Name Error</td>" + "\r\n"
			+"                </tr>" + "\r\n"
			+"                <tr>" + "\r\n"
			+"                    <td>Age:</td>" + "\r\n"
			+"                    <td><input type=\"text\" th:field=\"*{age}\" id=\"myAge\"/></td>" + "\r\n"
			+"                    <td th:if=\"${#fields.hasErrors('age')}\" th:errors=\"*{age}\">Name Error</td>" + "\r\n"
			+"                </tr>" + "\r\n"
			+"                <tr>" + "\r\n"
			+"                    <td><button type=\"submit\" id=\"mySubmitButton\">Submit</button></td>" + "\r\n"
			+"                </tr>" + "\r\n"
			+"            </table>" + "\r\n"
			+"        </form>" + "\r\n"
			+"    </body>" + "\r\n"
			+"</html>";
}
