<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<html>
<head>
<title>Reading RSS</title>
</head>

<body>
	<%=Thread.currentThread().getContextClassLoader().getResource("org/apache/xpath/XPathException.class")%>
	<br>

	<c:import var="news" url="http://feeds.wired.com/wired/index" />

	<x:parse var="doc" xml="${news}" />

	<x:out select="$doc/rss/channel/title" />
	<br>
	<x:out select="$doc/rss/channel/pubDate" />
	<br>
	<x:out select="$doc/rss/channel/description" />
	<br>

	<br>

	<x:forEach var="story" select="$doc/rss/channel/item">
		<x:out select="title" />
		<br>
	</x:forEach>

</body>
</html>